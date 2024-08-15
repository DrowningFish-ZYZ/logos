package com.violet.common.exception;

import com.violet.common.enums.ResponseCode;

public class UserLoginException extends CommonException {
    public UserLoginException(String message) {
        super(ResponseCode.FORBIDDEN, message);
    }
}
