package com.eduglasses.frontflip.controllers.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeMatchValidator.class)
public @interface FileTypeMatch {

	String message() default "{Wrong file selected}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
