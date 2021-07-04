package org.wangfeng.panda.app.common.exception;

public class RuleRuntimeException extends RuntimeException{

    Integer code;
    String message;

    public RuleRuntimeException(String message) {
        this.message = message;
    }

    public RuleRuntimeException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public RuleRuntimeException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public RuleRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public RuleRuntimeException(Throwable cause) {
        super(cause);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
