package xyz.xminao.yelplet.service.impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.transaction.annotation.Transactional;
import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.SeckillVoucher;
import xyz.xminao.yelplet.entity.User;
import xyz.xminao.yelplet.entity.VoucherOrder;
import xyz.xminao.yelplet.mapper.VoucherOrderMapper;
import xyz.xminao.yelplet.service.ISeckillVoucherService;
import xyz.xminao.yelplet.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.xminao.yelplet.utils.RedisIdWorker;
import xyz.xminao.yelplet.utils.SimpleRedisLock;
import xyz.xminao.yelplet.utils.UserHolder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisIdWorker redisIdWorker;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    // 异步处理线程池
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    // 用于线程池处理的任务
    // 初始化完毕后就会从队列中拿取消息
    private class VoucherOrderHandler implements Runnable {
        @Override
        public void run() {
            try {
                // 从队列中获取订单信息
                VoucherOrder voucherOrder = orderTasks.take();
                // 创建订单

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleVoucherOrder(VoucherOrder voucherOrder) {
            // 获取用户
            Long userId = voucherOrder.getUserId();
            // 创建锁对象

        }
    }

    // 待处理的订单消息队列
    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);

    @Transactional
    public void createVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        Long voucherId = voucherOrder.getVoucherId();
        // 创建锁对象
        RLock lock = redissonClient.getLock("lock:order:" + userId);
        // 尝试获取锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            log.error("不允许重复下单");
            return;
        }

        try {
            // 查询订单
            int count = query().eq("user_id", userId).eq("voucher_id", voucherId).count();
            if (count > 0) {
                log.error("不允许重复下单");
                return;
            }
            // 扣减库存
            boolean success = seckillVoucherService.update()
                    .setSql("stock = stock - 1")
                    .eq("voucher_id", voucherId).gt("stock", 0)
                    .update();
            if (!success) {
                log.error("库存不足");
                return;
            }

            // 库存充足，创建订单
            save(voucherOrder);
        } finally {
            lock.unlock();
        }


    }

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 获取用户
        Long userId = UserHolder.getUser().getId();;
        long orderId = redisIdWorker.nextId("order");
        // 执行lua脚本判断库存和一人一单逻辑
        Long result = stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString(),
                String.valueOf(orderId)
        );
        int res = result.intValue();

        // 判断结果是否为0，即用户可以下单（库存足够，用户没有买过）
        if (res != 0) {
            return Result.fail(res == 1 ? "库存不足" : "不能重复下单");
        }
        // todo：保存到阻塞对垒
        return Result.ok(orderId);
    }
}
