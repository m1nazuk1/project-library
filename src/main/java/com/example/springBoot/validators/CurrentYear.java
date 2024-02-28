package com.example.springBoot.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrentYearValidator.class)
@Documented
public @interface CurrentYear {
    String message() default "Год не может быть больше текущего";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
