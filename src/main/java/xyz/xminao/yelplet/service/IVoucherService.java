package xyz.xminao.yelplet.service;

import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.Voucher;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IVoucherService extends IService<Voucher> {

    Result queryVoucherOfShop(Long shopId);

    void addSeckillVoucher(Voucher voucher);
}
