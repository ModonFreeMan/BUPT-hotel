package com.backend.anno;

import com.backend.validation.CustomerIdValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented//元注解
@Target(ElementType.FIELD)//元注解
@Retention(RetentionPolicy.RUNTIME)//元注解
@Constraint(
        validatedBy = {CustomerIdValidation.class}
)
public @interface CustomerId {
    //提供校验失败后的提示信息
    String message() default "身份证格式不正确";

    //指定分组
    Class<?>[] groups() default {};

    //负载 获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
