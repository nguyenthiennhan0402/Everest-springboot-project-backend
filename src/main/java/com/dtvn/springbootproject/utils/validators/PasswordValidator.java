package com.dtvn.springbootproject.utils.validators;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.FieldValueLengthConstants.*;

public class PasswordValidator {

    public static PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();
        if (password.isEmpty()) {
            result.addError(ERROR_PASSWORD_REQUIRED);
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            result.addError(ERROR_MIN_PASSWORD_LENGTH);
        }

        if (password.length() >= MAX_PASSWORD_LENGTH) {
            result.addError(ERROR_MAX_PASSWORD_LENGTH);
        }

        if (!password.matches(".*\\d.*")) {
            result.addError(ERROR_PASSWORD_MISSING_DIGIT);
        }

        if (!password.matches(".*[a-z].*")) {
            result.addError(ERROR_PASSWORD_MISSING_LOWERCASE);
        }

        if (!password.matches(".*[A-Z].*")) {
            result.addError(ERROR_PASSWORD_MISSING_UPPERCASE);
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            result.addError(ERROR_PASSWORD_MISSING_SPECIAL_CHARACTER);
        }

        if (password.matches(".*\\s+.*")) {
            result.addError(ERROR_PASSWORD_CONTAINS_WHITESPACE);
        }

        if (password.startsWith(" ") || password.endsWith(" ")) {
            result.addError(ERROR_PASSWORD_START_END_WITH_WHITESPACE);
        }
        return result;
    }

    @Getter
    public static class PasswordValidationResult {
        private final List<String> errors;

        public PasswordValidationResult() {
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }
    }
}
