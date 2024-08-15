package com.violet.common.advice;

import com.violet.common.domain.Result;
import com.violet.common.enums.ResponseCode;
import com.violet.common.exception.ParameterException;
import com.violet.common.exception.ServiceException;
import com.violet.common.exception.UserLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {
    /**
     * 用户登录异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(UserLoginException.class)
    public Object handleUserLoginException(UserLoginException e) {
        log.error(e.getMessage());
        return Result.Builder.buildErrorResult(e.getMessage(), e.getCode());
    }

    /**
     * 请求的参数异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ParameterException.class)
    public Object handleParameterException(ParameterException e) {
        log.error(e.getMessage());
        return Result.Builder.buildErrorResult(e.getMessage(), e.getCode());
    }

    /**
     * 服务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public Object handleServerException(ServiceException e) {
        log.error(e.getMessage());
        return Result.Builder.buildErrorResult(e.getMessage(), e.getCode());
    }

    /**
     * 其它异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        log.error(e.getMessage());
        System.out.println(e.getMessage());
        return Result.Builder.buildErrorResult(e.getMessage(), ResponseCode.SERVICE_ERROR);
    }
}
