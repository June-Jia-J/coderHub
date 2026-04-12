package com.example.blog.dto;

public class CommonResponse<T> {

    private int code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> ok(T data) {
        CommonResponse<T> r = new CommonResponse<>();
        r.setCode(200);
        r.setMessage("成功");
        r.setData(data);
        return r;
    }

    public static <T> CommonResponse<T> fail(int code, String message) {
        CommonResponse<T> r = new CommonResponse<>();
        r.setCode(code);
        r.setMessage(message);
        r.setData(null);
        return r;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
