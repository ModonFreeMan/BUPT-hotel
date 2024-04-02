package com.backend.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;

//统一响应结果
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;//业务状态码
    private String message;//提示信息
    private T data;//响应数据

    //成功响应(带数据)
    public static <E> Result<E> success(E data) {
        return new Result<>(0, "操作成功", data);
    }

    //成功响应(不带数据)
    public static Result success() {
        return new Result(0, "操作成功", null);
    }

    //错误响应
    public static Result error(String message) {
        return new Result(1, message, null);
    }


}
