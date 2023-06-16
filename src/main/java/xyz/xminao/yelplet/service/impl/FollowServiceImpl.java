package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.Follow;
import xyz.xminao.yelplet.mapper.FollowMapper;
import xyz.xminao.yelplet.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

}
