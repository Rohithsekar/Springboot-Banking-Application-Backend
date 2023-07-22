package com.banking.online_banking.configuration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ArgumentTypeValidator implements ConstraintValidator<ValidArgumentType, Double> {
    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        return (aDouble instanceof Double) && (aDouble!=null);
    }
}