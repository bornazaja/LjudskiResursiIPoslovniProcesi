package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BrojTelefonaValidator implements ConstraintValidator<BrojTelefona, String>{

    @Override
    public void initialize(BrojTelefona a) {

    }

    @Override
    public boolean isValid(String brojTelefona, ConstraintValidatorContext cvc) {
        return brojTelefona != null && brojTelefona.matches("\\d{8,10}");
    }

}
