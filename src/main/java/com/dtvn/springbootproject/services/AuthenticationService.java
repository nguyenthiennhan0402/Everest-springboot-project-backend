package com.dtvn.springbootproject.services;

import com.dtvn.springbootproject.dto.requestDtos.Auth.AuthenticationRequestDTO;
import com.dtvn.springbootproject.dto.responsedtos.Auth.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO login(AuthenticationRequestDTO request);
}
