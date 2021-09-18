package com.bzaja.ljudskiresursiiposlovniprocesilibrary.validation.customconstraint;

import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.poslovnipartner.PoslovniPartnerService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.feature.zaposlenik.ZaposlenikService;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.BeanUtils;
import com.bzaja.ljudskiresursiiposlovniprocesilibrary.util.NumberUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueOIBValidator implements ConstraintValidator<UniqueOIB, Object> {

    private static final String ID_POSLOVNI_PARTNER_FIELD = "idPoslovniPartner";
    private static final String ID_ZAPOSLENIK_FIELD = "idZaposlenik";

    private String idField;
    private String oibField;
    private String message;

    @Autowired
    private ZaposlenikService zaposlenikService;

    @Autowired
    private PoslovniPartnerService poslovniPartnerService;

    @Override
    public void initialize(UniqueOIB constraintAnntoation) {
        idField = constraintAnntoation.idField();
        oibField = constraintAnntoation.oibField();
        message = constraintAnntoation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cvc) {
        boolean valid = false;

        Object id = BeanUtils.getPropertyValue(value, idField);
        Object oib = BeanUtils.getPropertyValue(value, oibField);

        Integer idInt = (Integer) id;
        String oibStr = (String) oib;

        if (StringUtils.isNotEmpty(oibStr)) {
            switch (idField) {
                case ID_ZAPOSLENIK_FIELD:
                    valid = isZaposlenikOibUnique(oibStr, idInt);
                    break;
                case ID_POSLOVNI_PARTNER_FIELD:
                    valid = isPoslovniPartnerOibUnique(oibStr, idInt);
                    break;
                default:
                    throw new IllegalArgumentException("This field id is not supported.");
            }
        } else {
            valid = true;
        }

        if (!valid) {
            cvc.buildConstraintViolationWithTemplate(message).addPropertyNode(oibField).addConstraintViolation().disableDefaultConstraintViolation();
        }
        return valid;
    }

    private boolean isZaposlenikOibUnique(String oibStr, Integer idInt) {
        boolean uniqueOib = false;

        if (NumberUtils.isPositive(idInt)) {
            if (zaposlenikService.existsByOibAndIdZaposlenikNot(oibStr, idInt) || poslovniPartnerService.existsByOib(oibStr)) {
                uniqueOib = false;
            } else {
                uniqueOib = true;
            }
        } else {
            if (zaposlenikService.existsByOib(oibStr) || poslovniPartnerService.existsByOib(oibStr)) {
                uniqueOib = false;
            } else {
                uniqueOib = true;
            }
        }

        return uniqueOib;
    }

    private boolean isPoslovniPartnerOibUnique(String oibStr, Integer idInt) {
        boolean uniqueOib = false;

        if (NumberUtils.isPositive(idInt)) {
            if (poslovniPartnerService.existsByOibAndIdPoslovniPartnerNot(oibStr, idInt) || zaposlenikService.existsByOib(oibStr)) {
                uniqueOib = false;
            } else {
                uniqueOib = true;
            }
        } else {
            if (poslovniPartnerService.existsByOib(oibStr) || zaposlenikService.existsByOib(oibStr)) {
                uniqueOib = false;
            } else {
                uniqueOib = true;
            }
        }

        return uniqueOib;
    }
}
