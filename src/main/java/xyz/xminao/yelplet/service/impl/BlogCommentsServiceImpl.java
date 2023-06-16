package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.BlogComments;
import xyz.xminao.yelplet.mapper.BlogCommentsMapper;
import xyz.xminao.yelplet.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
