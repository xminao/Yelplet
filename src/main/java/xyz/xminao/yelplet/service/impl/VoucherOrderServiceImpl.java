package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.VoucherOrder;
import xyz.xminao.yelplet.mapper.VoucherOrderMapper;
import xyz.xminao.yelplet.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

}
