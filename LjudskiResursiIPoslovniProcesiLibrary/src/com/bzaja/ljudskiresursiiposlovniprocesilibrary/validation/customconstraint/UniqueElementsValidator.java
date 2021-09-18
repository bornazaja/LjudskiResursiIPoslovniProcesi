package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueElementsValidator implements ConstraintValidator<UniqueElements, List<?>> {

    @Override
    public void initialize(UniqueElements a) {

    }

    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext cvc) {
        Set<?> set = new HashSet<>(list);
        if (set.size() < list.size()) {
            return false;
        }
        return true;
    }

}
