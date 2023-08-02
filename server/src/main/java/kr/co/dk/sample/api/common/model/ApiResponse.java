package kr.co.dk.sample.api.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse<T> implements Serializable {

    private Boolean success;
    private String message;
    private Integer code;
    private T data;

    public ApiResponse(Boolean success) {
        this.success = success;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResponse(Boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(Boolean success, Integer code, T data) {
        this.success = success;
        this.data = data;
    }

    public ApiResponse(Boolean success, Integer code) {
        this.success = success;
        this.code = code;
    }

    public ApiResponse(Boolean success, String message, Integer code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public ApiResponse(ErrorCode errorCode) {
        this.success = false;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
    }

    public ApiResponse(Boolean success, T data, String message, Integer code) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ApiResponse(Boolean success, T data, ErrorCode errorCode) {
        this.success = success;
        this.data = data;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
    }

    public ApiResponse(Boolean success, ErrorCode errorCode) {
        this.success = success;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
    }

    @Override
    public String toString() {
        if (this.data != null) {
            return "{ \"success\":" + this.success + "," +
                    "\"message\":\"" + this.message + "\"," +
                    "\"code\":" + this.code + "," +
                    "\"data\":" + this.data + "}";
        } else {
            return "{ \"success\":" + this.success + "," +
                    "\"message\":\"" + this.message + "\"," +
                    "\"code\":" + this.code + "}";
        }


    }

}
