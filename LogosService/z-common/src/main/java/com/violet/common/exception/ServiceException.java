package com.violet.common.exception;

import com.violet.common.enums.ResponseCode;

public class ServiceException extends CommonException{
    public ServiceException(String message) {
        super(ResponseCode.SERVICE_ERROR, message);
    }
}
