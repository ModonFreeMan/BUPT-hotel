package com.backend.validation;

import java.util.regex.Pattern;
import com.backend.anno.CustomerId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerIdValidation implements ConstraintValidator<CustomerId,String> {
    //身份证校验规则
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("^\\d{17}(\\d|X|x)$");
    /**
     *
     * @param value 要校验的数据
     * @return bool类型
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return ID_CARD_PATTERN.matcher(value).matches();
    }
}
