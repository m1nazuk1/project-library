package com.example.springBoot.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;

public class CurrentYearValidator implements ConstraintValidator<CurrentYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value <= YearMonth.now().getYear();
    }
}
