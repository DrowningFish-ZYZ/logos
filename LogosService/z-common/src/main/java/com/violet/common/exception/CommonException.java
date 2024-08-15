package com.violet.common.exception;

import com.violet.common.enums.ResponseCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private ResponseCode code;

    public CommonException(ResponseCode code, String message) {
        super(message);
        this.code = code;
    }

}
