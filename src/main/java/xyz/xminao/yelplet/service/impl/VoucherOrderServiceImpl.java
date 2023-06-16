package xyz.xminao.yelplet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.SeckillVoucher;
import xyz.xminao.yelplet.entity.VoucherOrder;
import xyz.xminao.yelplet.mapper.VoucherOrderMapper;
import xyz.xminao.yelplet.service.ISeckillVoucherService;
import xyz.xminao.yelplet.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.xminao.yelplet.utils.RedisIdWorker;
import xyz.xminao.yelplet.utils.UserHolder;

import javax.annotation.Resource;
import java.time.LocalDateTime;


@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisIdWorker redisIdWorker;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 查询优惠券
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        //-----------------------------------------------------
        // 判断秒杀是否开始
        if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
            return Result.fail("秒杀尚未开始");
        }
        // 判断秒杀时候结束
        if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
            return Result.fail("秒杀已经结束");
        }
        //------------------------------------------------------
        // 判断库存够不够
        if (voucher.getStock() < 1) {
            return Result.fail("库存不足");
        }
        // 扣除库存
        boolean success = seckillVoucherService.update()
                .setSql("stock=stock - 1")
                .eq("voucher_id", voucherId).gt("stock", 0)
                .update();
        //--------------------------------------------------------
        // 创建订单
        VoucherOrder voucherOrder = new VoucherOrder();
        // 订单id
        long orderId = redisIdWorker.nextId("order");
        voucherOrder.setVoucherId(orderId);
        // 用户id
        Long userId = UserHolder.getUser().getId();
        voucherOrder.setUserId(userId);
        // 代金券id
        voucherOrder.setVoucherId(voucherId);
        save(voucherOrder);

        return Result.ok(orderId);
    }
}
