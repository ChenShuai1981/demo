package com.example.demo.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
@Component
public class ResponseResultInterceptor implements HandlerInterceptor {

    public static final String RESPONSE_RESULT_ANN = "RESPONSE_RESULT_ANN";

    private static Cache<HandlerMethod, ResponseResult> rrCache = CacheBuilder.newBuilder().maximumSize(50).build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("---------preHandle--------");
        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseResult responseResult = rrCache.get(handlerMethod, new Callable<ResponseResult>() {
                @Override
                public ResponseResult call() throws Exception {
                    final Class<?> clazz = handlerMethod.getBeanType();
                    final Method method = handlerMethod.getMethod();
                    ResponseResult rr = null;
                    if (clazz.isAnnotationPresent(ResponseResult.class) || method.isAnnotationPresent(ResponseResult.class)) {
                        rr = clazz.getAnnotation(ResponseResult.class);
                    }
                    return rr;
                }
            });
            if (responseResult != null) {
                request.setAttribute(RESPONSE_RESULT_ANN, responseResult);
            }
        }
        return true;
    }

//    //controller执行之后，且页面渲染之前调用
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        System.out.println("---------postHandle--------");
//    }
//
//    //页面渲染之后调用，一般用于资源清理操作
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("---------afterCompletion--------");
//    }
}
