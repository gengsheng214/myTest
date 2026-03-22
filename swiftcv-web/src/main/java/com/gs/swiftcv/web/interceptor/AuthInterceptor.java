package com.gs.swiftcv.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 请求进入控制器前执行。
     *
     * @param request 当前请求
     * @param response 当前响应
     * @param handler 处理器
     * @return 是否放行
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这里可以添加认证逻辑，比如检查token
        // 目前暂时放行所有请求
        return true;
    }

    /**
     * 控制器执行后、视图渲染前执行。
     *
     * @param request 当前请求
     * @param response 当前响应
     * @param handler 处理器
     * @param modelAndView 视图模型
     * @throws Exception 异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 处理完请求后执行
    }

    /**
     * 请求完成后执行清理逻辑。
     *
     * @param request 当前请求
     * @param response 当前响应
     * @param handler 处理器
     * @param ex 异常信息
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后执行
    }
}
