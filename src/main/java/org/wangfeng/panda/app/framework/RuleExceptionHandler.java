package org.wangfeng.panda.app.framework;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wangfeng.panda.app.common.base.AppBaseController;
import org.wangfeng.panda.app.common.exception.RuleRuntimeException;
import org.wangfeng.panda.app.util.IpUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * User: wangfeng
 * Date: 2020/8/24
 */
@Slf4j
@Component
@ControllerAdvice
@RestControllerAdvice
public class RuleExceptionHandler extends AppBaseController {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object defaultExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        log.warn("服务端异常", e);
        log.warn("RequestIP:[{}],RequestURI:[{}],QueryString:[{}],", IpUtils.getIpFromRequest(request),
                request.getRequestURI(), request.getQueryString(), request.getHeader("Authorization"));

        JsonObject object = new JsonObject();
        object.addProperty("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        object.addProperty("message", "服务端异常");

        return object.toString();
    }

    @ExceptionHandler(value = RuleRuntimeException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public Object RuleRuntimeExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        log.warn("服务端异常", e);
        log.warn("RequestIP:[{}],RequestURI:[{}],QueryString:[{}],", IpUtils.getIpFromRequest(request),
                request.getRequestURI(), request.getQueryString(), request.getHeader("Authorization"));

        JsonObject object = new JsonObject();
        object.addProperty("code", HttpStatus.EXPECTATION_FAILED.value());
        object.addProperty("message", e.getMessage());


        return object.toString();
    }








}
