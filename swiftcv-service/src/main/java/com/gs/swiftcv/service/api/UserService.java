package com.gs.swiftcv.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gs.swiftcv.repository.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    // 可以添加自定义的业务方法

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 是否注册成功
     */
    boolean register(User user);
}