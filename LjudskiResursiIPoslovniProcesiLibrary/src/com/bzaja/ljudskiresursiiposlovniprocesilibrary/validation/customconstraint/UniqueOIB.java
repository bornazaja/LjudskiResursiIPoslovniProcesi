package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueOIBValidator.class)
@Documented
public @interface UniqueOIB {

    String message() default "{UniqueOIB}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String idField();

    String oibField();
}
