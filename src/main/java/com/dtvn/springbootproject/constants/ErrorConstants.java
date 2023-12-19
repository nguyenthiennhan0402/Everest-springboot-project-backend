package com.dtvn.springbootproject.constants;

public class ErrorConstants {
    // Required Errors
    public static final String ERROR_EMAIL_REQUIRED = "Email is required";
    public static final String ERROR_PASSWORD_REQUIRED = "Password is required";
    public static final String ERROR_FIRST_NAME_REQUIRED = "First name is required";
    public static final String ERROR_LAST_NAME_REQUIRED = "Last name is required";
    public static final String ERROR_ADDRESS_REQUIRED = "Address is required";
    public static final String ERROR_ROLE_REQUIRED = "Role is required";
    public static final String ERROR_PHONE_REQUIRED = "Phone number is required";

    // Length Errors
    public static final String ERROR_EMAIL_MAX_LENGTH = "Email exceeds maximum allowed length";
    public static final String ERROR_FIRST_NAME_MAX_LENGTH = "First name exceeds maximum allowed length";
    public static final String ERROR_LAST_NAME_MAX_LENGTH = "Last name exceeds maximum allowed length";
    public static final String ERROR_ADDRESS_MAX_LENGTH = "Address exceeds maximum allowed length";
    public static final String ERROR_PHONE_MAX_LENGTH = "Phone number exceeds maximum allowed length";

    // Invalid Errors
    public static final String ERROR_EMAIL_INVALID = "Email is not valid";
    public static final String ERROR_FIRST_NAME_INVALID = "First name is invalid";
    public static final String ERROR_LAST_NAME_INVALID = "Last name is invalid";
    public static final String ERROR_ADDRESS_INVALID = "Address is invalid";

    public static final String ERROR_PHONE_FORMAT_INVALID = "Phone number is not in a valid format";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";

    //Password errors
    public static final String ERROR_MIN_PASSWORD_LENGTH = "Password should be at least 8 characters long";
    public static final String ERROR_MAX_PASSWORD_LENGTH = "Password must only be a maximum of 60 characters long";
    public static final String ERROR_PASSWORD_MISSING_DIGIT = "Password should contain at least one digit";
    public static final String ERROR_PASSWORD_MISSING_LOWERCASE = "Password should contain at least one lowercase letter";
    public static final String ERROR_PASSWORD_MISSING_UPPERCASE = "Password should contain at least one uppercase letter";
    public static final String ERROR_PASSWORD_MISSING_SPECIAL_CHARACTER = "Password should contain at least one special character (@#$%^&+=!)";
    public static final String ERROR_PASSWORD_CONTAINS_WHITESPACE = "Password should not contain whitespace characters";
    public static final String ERROR_PASSWORD_START_END_WITH_WHITESPACE = "Password should not start or end with whitespace characters";

    // Others errors
    public static final String ERROR_USER_NOT_FOUND = "User not found";

    public static final String ERROR_ROLE_NOT_FOUND = "Role not found";
    public static final String ERROR_SAVE_ACCOUNT = "Failed to save the account.";
    public static final String ERROR_TOKEN_INVALID = "Token is invalid or expired";
    public static final String ERROR_CANNOT_RETRIEVE_AUTHENTICATED_USER = "Cannot retrieve authenticated user.";
    public static final String USER_NOT_USER_DETAILS = "Authenticated user is not an instance of UserDetails.";
    public static final String ERROR_LOGIN_BAD_CREDENTIALS = "Email or password is incorrect.";
    public static final String ERROR_INTERNAL_SERVER = "Internal server error.";
    public static final String ERROR_ACCOUNT_HAS_BEEN_DELETED = "This account has been deleted.";

    // Unknown Error
    public static final String ERROR_UNKNOWN = "Undetermined error, please contact administrator for support.";
}
