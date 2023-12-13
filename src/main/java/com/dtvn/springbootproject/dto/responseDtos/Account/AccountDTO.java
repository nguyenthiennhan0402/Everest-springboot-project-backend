package com.dtvn.springbootproject.dto.responseDtos.Account;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private int accountId;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String address;
    private String phone;
}
