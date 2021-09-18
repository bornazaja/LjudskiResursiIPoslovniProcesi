/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiposaocjenik.StudentskiPosaoCjenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.studentskiugovor.StudentskiUgovorDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author Borna
 */
public class MinimalnaCijenaPoSatuStudentskogUgovoraValidator implements ConstraintValidator<MinimalnaCijenaPoSatuStudentskogUgovora, StudentskiUgovorDto> {

    private String message;

    @Override
    public void initialize(MinimalnaCijenaPoSatuStudentskogUgovora constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(StudentskiUgovorDto t, ConstraintValidatorContext cvc) {
        boolean valid = true;

        if (t.getCijenaPoSatu() != null && t.getStudentskiPosaoCjenik() != null) {
            if (t.getCijenaPoSatu() < t.getStudentskiPosaoCjenik().getCijenaPoSatu()) {
                valid = false;
            }
        }

        if (!valid) {
            cvc.buildConstraintViolationWithTemplate(message).addPropertyNode("cijenaPoSatu").addConstraintViolation().disableDefaultConstraintViolation();
        }

        return valid;
    }

}
