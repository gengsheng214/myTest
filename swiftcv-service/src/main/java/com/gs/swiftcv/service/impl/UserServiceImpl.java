package com.gs.swiftcv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.swiftcv.repository.entity.User;
import com.gs.swiftcv.repository.mapper.UserMapper;
import com.gs.swiftcv.service.api.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类。
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 根据用户名查询用户。
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        // 使用 MyBatis Plus 的 LambdaQueryWrapper 进行条件查询
        return lambdaQuery()
                .eq(User::getUsername, username)
                .one();
    }

    /**
     * 注册用户。
     *
     * @param user 用户信息
     * @return 是否注册成功
     */
    @Override
    public boolean register(User user) {
        // 这里可以添加密码加密等逻辑
        // 保存用户信息
        return save(user);
    }
}
