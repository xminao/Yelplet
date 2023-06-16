package xyz.xminao.yelplet.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.transaction.annotation.Transactional;
import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.Shop;
import xyz.xminao.yelplet.mapper.ShopMapper;
import xyz.xminao.yelplet.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.xminao.yelplet.utils.CacheClient;
import xyz.xminao.yelplet.utils.RedisData;

import javax.annotation.Resource;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static xyz.xminao.yelplet.utils.RedisConstants.CACHE_SHOP_KEY;
import static xyz.xminao.yelplet.utils.RedisConstants.CACHE_SHOP_TTL;


@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result queryById(Long id) {
        // 解决缓存穿透问题
        Shop shop = cacheClient.queryWithPassThrough(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);

        // 互斥锁解决缓存击穿
//        Shop shop = cacheClient.queryWithMutex(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);;

        // 逻辑过期解决缓存击穿
//        Shop shop = cacheClient.queryWithLogicalExpire(CACHE_SHOP_KEY, id, Shop.class, this::getById, CACHE_SHOP_TTL, TimeUnit.MINUTES);;

        if (shop == null) {
            return Result.fail("商铺不存在");
        }
        return Result.ok(shop);
    }

    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("商铺ID无效");
        }
        // 更新数据库
        updateById(shop);
        // 删除缓存解决双写问题
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }



    public void saveShop2Redis(Long id, Long expireSec) {
        // 查询店铺数据
        Shop shop = getById(id);
        // 封装逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireSec));
        // 写入redis
        stringRedisTemplate.opsForValue().set(CACHE_SHOP_KEY + id, JSONUtil.toJsonStr(redisData));
    }
}
