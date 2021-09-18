package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {

    @Override
    public void initialize(NotEmptyList a) {

    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext cvc) {
        return !list.isEmpty();
    }

}
