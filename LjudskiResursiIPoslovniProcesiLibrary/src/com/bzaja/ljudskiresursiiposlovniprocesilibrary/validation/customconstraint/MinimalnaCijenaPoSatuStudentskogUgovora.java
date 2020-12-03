/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Constraint(validatedBy = MinimalnaCijenaPoSatuStudentskogUgovoraValidator.class)
@Documented
public @interface MinimalnaCijenaPoSatuStudentskogUgovora {

    String message() default "{MinimalnaCijenaPoSatuStudentskogUgovora}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
