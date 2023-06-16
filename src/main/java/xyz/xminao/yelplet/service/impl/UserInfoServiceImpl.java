package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.UserInfo;
import xyz.xminao.yelplet.mapper.UserInfoMapper;
import xyz.xminao.yelplet.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
