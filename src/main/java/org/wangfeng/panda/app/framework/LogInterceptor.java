package org.wangfeng.panda.app.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.wangfeng.panda.app.util.IpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        try {
            String apiName = ((HandlerMethod) handler).getMethod().getName();
            String className = ((HandlerMethod) handler).getMethod().getDeclaringClass().getName();
            log.info("enter,call method  {} , class {} ,ip {} ", apiName, className, IpUtils.getIpFromRequest(httpServletRequest));
        } catch (Exception enterException) {

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        try {
            String apiName = ((HandlerMethod) handler).getMethod().getName();
            String className = ((HandlerMethod) handler).getMethod().getDeclaringClass().getName();
            log.info("exit ,call method  {} , class {} ,ip {}  ", apiName,className, IpUtils.getIpFromRequest(httpServletRequest));
        } catch (Exception exitException) {

        }
    }
}
