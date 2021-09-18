package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.control.Label;
import javax.validation.ConstraintViolation;

public final class FormUtils {

    private FormUtils(){
        
    }
    
    public static <T> void showErrorMessage(String field, Set<ConstraintViolation<T>> constraintViolations, Label errorLabel) {
        Boolean hasFieldErrors = constraintViolations.stream().anyMatch(x -> x.getPropertyPath().toString().equals(field));
        if (hasFieldErrors) {
            List<String> poruke = constraintViolations.stream().filter(x -> x.getPropertyPath().toString().equals(field)).map(x -> x.getMessage()).collect(Collectors.toList());
            errorLabel.setText(String.join(System.lineSeparator(), poruke));
        }
    }
}
