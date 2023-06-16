package xyz.xminao.yelplet.service.impl;

import xyz.xminao.yelplet.entity.Blog;
import xyz.xminao.yelplet.mapper.BlogMapper;
import xyz.xminao.yelplet.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
