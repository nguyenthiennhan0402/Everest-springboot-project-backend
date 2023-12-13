package com.dtvn.springbootproject.dto.responseDtos.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    private String access_token;
    private String refresh_token;
    private int code;
    private String message;
    private String username;
    private long access_token_expires_in;
}
