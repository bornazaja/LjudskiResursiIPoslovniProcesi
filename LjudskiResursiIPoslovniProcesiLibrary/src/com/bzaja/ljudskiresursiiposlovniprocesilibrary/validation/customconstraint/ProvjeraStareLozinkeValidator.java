package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ChangePasswordZaposlenikDto;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ProvjeraStareLozinkeValidator implements ConstraintValidator<ProvjeraStareLozinke, ChangePasswordZaposlenikDto> {

    private String message;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Override
    public void initialize(ProvjeraStareLozinke constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(ChangePasswordZaposlenikDto changePasswordZaposlenikDto, ConstraintValidatorContext cvc) {
        boolean valid = true;

        if (changePasswordZaposlenikDto.getIdZaposlenik() != null && changePasswordZaposlenikDto.getStaraLozinka() != null) {
            valid = zaposlenikService.existsByIdZaposlenikAndLozinka(changePasswordZaposlenikDto.getIdZaposlenik(), changePasswordZaposlenikDto.getStaraLozinka());
        }

        if (!valid) {
            cvc.buildConstraintViolationWithTemplate(message).addPropertyNode("staraLozinka").addConstraintViolation().disableDefaultConstraintViolation();
        }
        return valid;
    }

}
