package xyz.xminao.yelplet.service;

import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IVoucherOrderService extends IService<VoucherOrder> {

    Result seckillVoucher(Long voucherId);
}
