package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LozinkaValidator implements ConstraintValidator<Lozinka, String> {

    @Override
    public void initialize(Lozinka a) {

    }

    @Override
    public boolean isValid(String lozinka, ConstraintValidatorContext cvc) {
        return lozinka != null && lozinka.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,})$");
    }

}
