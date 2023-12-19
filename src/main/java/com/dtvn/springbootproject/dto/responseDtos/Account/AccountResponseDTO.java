package com.dtvn.springbootproject.dto.responsedtos.Account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private int accountId;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String address;
    private String phone;
}
