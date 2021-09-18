package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class OIBValidator implements ConstraintValidator<OIB, String> {

    private boolean ignoreEmptyString;

    @Override
    public void initialize(OIB constraintAnnotation) {
        ignoreEmptyString = constraintAnnotation.ignoreEmptyString();
    }

    @Override
    public boolean isValid(String oib, ConstraintValidatorContext cvc) {
        boolean validOib = oib != null && oib.matches("\\d{11}");

        if (ignoreEmptyString) {
            if (StringUtils.isEmpty(oib)) {
                return true;
            } else {
                return validOib;
            }
        } else {
            return validOib;
        }
    }

}
