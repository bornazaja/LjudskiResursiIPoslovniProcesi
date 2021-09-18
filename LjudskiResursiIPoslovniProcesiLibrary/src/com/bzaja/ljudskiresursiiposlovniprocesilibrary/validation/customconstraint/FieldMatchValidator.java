package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cvc) {
        boolean valid = true;
        try {
            Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            if(firstObj != null && secondObj != null){
                valid = firstObj.equals(secondObj);
            }
            
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }

        if (!valid) {
            cvc.buildConstraintViolationWithTemplate(message).addPropertyNode(firstFieldName).addConstraintViolation().disableDefaultConstraintViolation();
        }
        return valid;
    }

}
