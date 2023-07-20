package com.banking.online_banking.configuration;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ArgumentTypeValidator.class)
public @interface ValidArgumentType {
    public String message() default "Invalid argument type. Please enter a number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
