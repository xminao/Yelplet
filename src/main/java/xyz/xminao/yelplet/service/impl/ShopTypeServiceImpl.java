package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.ShopType;
import xyz.xminao.yelplet.mapper.ShopTypeMapper;
import xyz.xminao.yelplet.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

}
