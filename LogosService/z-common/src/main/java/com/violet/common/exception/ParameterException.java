package com.violet.common.exception;

import com.violet.common.enums.ResponseCode;

public class ParameterException extends CommonException {
    public ParameterException(String message) {
        super(ResponseCode.BAD_REQUEST, message);
    }
}
