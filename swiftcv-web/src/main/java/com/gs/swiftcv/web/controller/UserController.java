package com.gs.swiftcv.web.controller;

import com.gs.swiftcv.core.common.Result;
import com.gs.swiftcv.repository.entity.User;
import com.gs.swiftcv.service.api.UserService;
import com.gs.swiftcv.service.model.UserLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            return Result.success();
        } else {
            return Result.fail("注册失败");
        }
    }

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody UserLoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            return Result.fail("用户名和密码不能为空");
        }
        User user = userService.getUserByUsername(request.getUsername());
        if (user != null && request.getPassword().equals(user.getPassword())) {
            // 这里可以添加生成token的逻辑
            return Result.success(user);
        } else {
            return Result.fail("用户名或密码错误");
        }
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam Long id) {
        User user = userService.getById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.fail("用户不存在");
        }
    }
}
