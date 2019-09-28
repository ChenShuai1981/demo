package com.example.demo.config;

import com.example.demo.dto.MyException;
import com.example.demo.dto.ErrorResult;
import com.example.demo.dto.Result;
import com.example.demo.dto.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.example.demo.config.ResponseResultInterceptor.RESPONSE_RESULT_ANN;

@Slf4j
@ControllerAdvice
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResultAnn != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("进入返回体 重写格式 处理中 ...");
        if (body instanceof ErrorResult) {
            log.info("返回值 异常 包装处理中 ...");
            ErrorResult errorResult = (ErrorResult) body;
            return Result.failure(errorResult.getResultCode(), errorResult.getError());
        }
        return Result.success(body);
    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ErrorResult errorHandler(Exception ex) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setResultCode(ResultCode.FAILURE);
        errorResult.setError(ex);
        return errorResult;
    }

    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public ErrorResult myErrorHandler(MyException ex) {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setResultCode(ex.getResultCode());
//        errorResult.setError(ex);
        return errorResult;
    }
}
