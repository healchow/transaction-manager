package com.healchow.transaction.detail.response;

import lombok.Getter;

/**
 * The response info of API
 */
@Getter
public class Response<T> {

    private static final Integer SUCCESS_CODE = 100;
    private static final Integer ERROR_CODE = 200;

    private Integer code;
    private String message;
    private T data;

    public Response() {
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> success() {
        return new Response<>(SUCCESS_CODE, null, null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(SUCCESS_CODE, null, data);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(ERROR_CODE, message, null);
    }

    public static <T> Response<T> fail(Integer code, String message) {
        return new Response<>(code, message, null);
    }

    public Response<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }
}
