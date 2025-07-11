package com.prueba.usuarios.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        boolean longitudValida = password.length() <= 100;
        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneMinuscula = password.matches(".*[a-z].*");
        boolean tieneDosDigitos = password.matches("(.*\\d.*){2,}");

        return tieneMayuscula && tieneMinuscula && tieneDosDigitos && longitudValida;
    }
}
