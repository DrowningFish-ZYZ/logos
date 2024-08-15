package com.violet.common.domain;

import com.violet.common.enums.ResponseCode;
import lombok.Data;

@Data
public class Result {
    private Object data;
    private String message;
    private int code;

    private Result() {}
    private Result(Object data, String message, int code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static class Builder {
        private static Result buildResult(Object data, String message, ResponseCode code) {
            return new Result(data, message, code.code);
        }

        public static Result buildErrorResult(String message, ResponseCode code) {
            return buildResult(null, message, code);
        }

        /**
         * 创建一个数据后应该返回的模板内容
         * @return new Result(null, "创建成功", ResponseCode.OK.code);
         */
        public static Result buildCreateResult() {
            return buildCreateResult(null);
        }

        public static Result buildCreateResult(Object data) {
            return buildCreateResult(data, "创建成功");
        }

        public static Result buildCreateResult(Object data, String message) {
            return buildResult(data, message, ResponseCode.OK);
        }


        /**
         * 成功获取数据后的模板
         * @return new Result(vos, "获取成功", ResponseCode.OK.code);
         */
        public static Result buildGetResult(Object data) {
            return buildGetResult(data, "获取成功");
        }

        public static Result buildGetResult(Object data, String message) {
            return buildResult(data, message, ResponseCode.OK);
        }


        /**
         * 成功修改数据后的模板
         * @return new Result(null, "修改成功", ResponseCode.OK.code);
         */
        public static Result buildUpdateResult() {
            return buildUpdateResult(null);
        }

        public static Result buildUpdateResult(Object data) {
            return buildUpdateResult(data, "修改成功");
        }

        public static Result buildUpdateResult(Object data, String message) {
            return buildResult(data, message, ResponseCode.OK);
        }

        /**
         * 成功删除数据后的模板
         * @return new Result(null, "删除成功", ResponseCode.OK.code)
         */
        public static Result buildDeleteResult() {
            return buildDeleteResult(null);
        }

        public static Result buildDeleteResult(Object data) {
            return buildDeleteResult(data, "修改成功");
        }

        public static Result buildDeleteResult(Object data, String message) {
            return buildResult(data, message, ResponseCode.OK);
        }
    }
}
