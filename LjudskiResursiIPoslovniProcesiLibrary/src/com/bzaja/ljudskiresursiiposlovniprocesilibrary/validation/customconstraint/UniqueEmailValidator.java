package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

    private static final String ID_POSLOVNI_PARTNER_FIELD = "idPoslovniPartner";
    private static final String ID_ZAPOSLENIK_FIELD = "idZaposlenik";

    private String idField;
    private String emailField;
    private String message;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Override
    public void initialize(UniqueEmail constraintAnntoation) {
        idField = constraintAnntoation.idField();
        emailField = constraintAnntoation.emailField();
        message = constraintAnntoation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cvc) {
        boolean valid = false;

        Object id = BeanUtils.getPropertyValue(value, idField);
        Object email = BeanUtils.getPropertyValue(value, emailField);

        Integer idInt = (Integer) id;
        String emailStr = (String) email;

        if (StringUtils.isNotEmpty(emailStr)) {
            switch (idField) {
                case ID_ZAPOSLENIK_FIELD:
                    valid = isZaposlenikEmailUnique(emailStr, idInt);
                    break;
                case ID_POSLOVNI_PARTNER_FIELD:
                    valid = isPoslovniPartnerEmailUnique(emailStr, idInt);
                    break;
                default:
                    throw new IllegalArgumentException("This field id is not supported.");
            }
        } else {
            valid = true;
        }

        if (!valid) {
            cvc.buildConstraintViolationWithTemplate(message).addPropertyNode(emailField).addConstraintViolation().disableDefaultConstraintViolation();
        }
        return valid;
    }

    private boolean isZaposlenikEmailUnique(String emailStr, Integer idInt) {
        boolean uniqueEmail = false;

        if (NumberUtils.isPositive(idInt)) {
            if (zaposlenikService.existsByEmailAndIdZaposlenikNot(emailStr, idInt) || poslovniPartnerService.existsByEmail(emailStr)) {
                uniqueEmail = false;
            } else {
                uniqueEmail = true;
            }
        } else {
            if (zaposlenikService.existsByEmail(emailStr) || poslovniPartnerService.existsByEmail(emailStr)) {
                uniqueEmail = false;
            } else {
                uniqueEmail = true;
            }
        }

        return uniqueEmail;
    }

    private boolean isPoslovniPartnerEmailUnique(String emailStr, Integer idInt) {
        boolean uniqueEmail = false;

        if (NumberUtils.isPositive(idInt)) {
            if (poslovniPartnerService.existsByEmailAndIdPoslovniPartnerNot(emailStr, idInt) || zaposlenikService.existsByEmail(emailStr)) {
                uniqueEmail = false;
            } else {
                uniqueEmail = true;
            }
        } else {
            if (poslovniPartnerService.existsByEmail(emailStr) || zaposlenikService.existsByEmail(emailStr)) {
                uniqueEmail = false;
            } else {
                uniqueEmail = true;
            }
        }

        return uniqueEmail;
    }
}
