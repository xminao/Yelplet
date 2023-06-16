package xyz.xminao.yelplet.service;

import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IShopService extends IService<Shop> {

    // 根据商户ID查询商户
    Result queryById(Long id);

    Result update(Shop shop);

}
