package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueElementsValidator.class)
@Documented
public @interface UniqueElements {

    String message() default "{UniqueElements}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
