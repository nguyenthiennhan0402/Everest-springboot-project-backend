package com.dtvn.springbootproject.utils.validators;

import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.FieldValueLengthConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;
import static com.dtvn.springbootproject.utils.RegularExpression.*;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    public void validateRegisterRequest(AccountRegisterRequestDTO request) {
        List<ValidationError> validationErrors = new ArrayList<>();

        validateEmail(request.getEmail(), validationErrors);
        validatePassword(request.getPassword(), validationErrors);
        validateName(request.getFirstname(), "Firstname",validationErrors);
        validateName(request.getLastname(), "Lastname",validationErrors);
        validatePhoneNumber(request.getPhone(),validationErrors);
        validateAddress(request.getAddress(),validationErrors);

        if (!validationErrors.isEmpty()) {
            String formattedErrors = validationErrors.stream()
                    .map(error -> error.toString()) // Sử dụng lambda expression
                    .collect(Collectors.joining(", ", "", ""));
            throw new ErrorException(formattedErrors, HTTP_BAD_REQUEST);
        }
    }

    private void validateEmail(String email, List<ValidationError> errors) {
        if (email.isEmpty()) {
            errors.add(new ValidationError(ERROR_EMAIL_REQUIRED));
            return;
        }
        if (email.length() >= MAX_EMAIL_LENGTH) {
            errors.add(new ValidationError(ERROR_EMAIL_MAX_LENGTH));
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            errors.add(new ValidationError(ERROR_EMAIL_INVALID));
        }
    }

    private void validatePassword(String password, List<ValidationError> errors) {
        if (password.isEmpty()) {
            errors.add(new ValidationError(ERROR_PASSWORD_REQUIRED));
        }
        PasswordValidator.PasswordValidationResult validationResult = PasswordValidator.validatePassword(password);

        if (!validationResult.isValid()) {
            List<String> passwordErrors = validationResult.getErrors();
            String formattedErrors = String.join(", ", passwordErrors);
            errors.add(new ValidationError(formattedErrors));
        }
    }

    private void validateName(String name, String fieldName,List<ValidationError> errors) {
        if (name.isEmpty() && "Firstname".contains(fieldName)) {
            errors.add(new ValidationError(ERROR_FIRST_NAME_REQUIRED));
        }
        if (name.isEmpty()  && "Lastname".contains(fieldName)) {
            errors.add(new ValidationError(ERROR_LAST_NAME_REQUIRED));
        }
        if (name.length() > MAX_FIRSTNAME_LENGTH && "Firstname".contains(fieldName)) {
            errors.add(new ValidationError(ERROR_FIRST_NAME_MAX_LENGTH));
        }
        if (name.length() > MAX_LASTNAME_LENGTH && "Lastname".contains(fieldName)) {
            errors.add(new ValidationError(ERROR_LAST_NAME_MAX_LENGTH));
        }

        if ("Firstname".contains(fieldName) && !name.matches(NAME_REGEX)) {
            errors.add(new ValidationError(ERROR_FIRST_NAME_INVALID));
        }
        if ("Lastname".contains(fieldName) && !name.matches(NAME_REGEX)) {
            errors.add(new ValidationError(ERROR_LAST_NAME_INVALID));
        }
    }

    private void validatePhoneNumber(String phoneNumber,List<ValidationError> errors) {
        if (phoneNumber.isEmpty()) {
            errors.add(new ValidationError(ERROR_PHONE_REQUIRED));
        }
        if (phoneNumber.length() > MAX_PHONE_LENGTH) {
            errors.add(new ValidationError(ERROR_PHONE_MAX_LENGTH));
        }
        if (!phoneNumber.matches(PHONE_NUMBER_REGEX)) {
            errors.add(new ValidationError(ERROR_PHONE_FORMAT_INVALID));
        }
    }
    private void validateAddress(String address,List<ValidationError> errors) {
        if (address.isEmpty()) {
            errors.add(new ValidationError(ERROR_ADDRESS_REQUIRED));
        }
        if (address.length() > MAX_ADDRESS_LENGTH) {
            errors.add(new ValidationError(ERROR_ADDRESS_MAX_LENGTH));
        }
        if (!address.matches(ADDRESS_REGEX)) {
            errors.add(new ValidationError(ERROR_ADDRESS_INVALID));
        }
    }

    private static class ValidationError {
        private final String message;

        public ValidationError(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }
}