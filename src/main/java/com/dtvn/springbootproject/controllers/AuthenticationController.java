package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.dto.requestDtos.Auth.AuthenticationRequestDTO;
import com.dtvn.springbootproject.dto.responsedtos.Auth.AuthenticationResponseDTO;
import com.dtvn.springbootproject.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(

            @RequestBody AuthenticationRequestDTO request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
