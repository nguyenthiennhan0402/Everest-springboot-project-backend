package com.dtvn.springbootproject.dto.requestDtos.Account;

import com.dtvn.springbootproject.exceptions.ErrorException;
import lombok.*;
import org.springframework.web.ErrorResponse;

import javax.validation.constraints.*;

import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.utils.RegularExpression.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequestDTO {
    @NotBlank(message = ERROR_FIRSTNAME_REQUIRED)
    @Pattern(regexp = NAME_REGEX, message = ERROR_FIRSTNAME_INVALID)
    private String firstname;

    @NotBlank(message = ERROR_LASTNAME_REQUIRED)
    @Pattern(regexp = NAME_REGEX, message = ERROR_LASTNAME_INVALID)
    private String lastname;

    @NotBlank(message = ERROR_EMAIL_REQUIRED)
    @Pattern(regexp = EMAIL_REGEX, message = ERROR_EMAIL_INVALID)
    private String email;

    @NotBlank(message = ERROR_PASSWORD_REQUIRED)
    @Size(min = 8, message = ERROR_MIN_PASSWORD_LENGTH)
    @Pattern.List({
            @Pattern(regexp = ".*\\d.*", message = ERROR_PASSWORD_MISSING_DIGIT),
            @Pattern(regexp = ".*[a-z].*", message = ERROR_PASSWORD_MISSING_LOWERCASE),
            @Pattern(regexp = ".*[A-Z].*", message = ERROR_PASSWORD_MISSING_UPPERCASE),
            @Pattern(regexp = ".*[@#$%^&+=!].*", message = ERROR_PASSWORD_MISSING_SPECIAL_CHARACTER),
            @Pattern(regexp = "\\S+", message = ERROR_PASSWORD_CONTAINS_WHITESPACE)
    })
    private String password;


    @NotBlank(message = ERROR_ROLE_REQUIRED)
    private String role;

    @NotBlank(message = ERROR_ADDRESS_REQUIRED)
    @Pattern(regexp = ADDRESS_REGEX, message = ERROR_ADDRESS_INVALID)
    private String address;

    @NotBlank(message = ERROR_PHONE_REQUIRED)
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = ERROR_PHONE_FORMAT_INVALID)
    private String phone;
}
