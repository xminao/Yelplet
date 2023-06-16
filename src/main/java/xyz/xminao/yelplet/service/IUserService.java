package xyz.xminao.yelplet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.xminao.yelplet.dto.LoginFormDTO;
import xyz.xminao.yelplet.dto.Result;
import xyz.xminao.yelplet.entity.User;

import javax.servlet.http.HttpSession;


public interface IUserService extends IService<User> {

    // 发送验证码
    Result sendCode(String phone, HttpSession session);

    // 登录
    Result login(LoginFormDTO loginForm, HttpSession session);

    // 注册
    Result sign();

    Result signCount();

}
