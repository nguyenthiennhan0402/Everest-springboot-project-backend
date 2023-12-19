package com.dtvn.springbootproject.services.impl;

import com.dtvn.springbootproject.dto.requestDtos.Auth.AuthenticationRequestDTO;
import com.dtvn.springbootproject.dto.responsedtos.Auth.AuthenticationResponseDTO;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.config.JwtService;
import com.dtvn.springbootproject.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dtvn.springbootproject.constants.AppConstants.*;
import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationServiceException e) {
            return AuthenticationResponseDTO.builder()
                    .code(HTTP_INTERNAL_SERVER_ERROR)
                    .message(ERROR_INTERNAL_SERVER)
                    .build();
        } catch (BadCredentialsException e) {
            return AuthenticationResponseDTO.builder()
                    .code(HTTP_BAD_REQUEST)
                    .message(ERROR_LOGIN_BAD_CREDENTIALS)
                    .build();
        }
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow();
        if(account.isDeleteFlag()) {
            return AuthenticationResponseDTO.builder()
                    .code(HTTP_BAD_REQUEST)
                    .message(ERROR_ACCOUNT_HAS_BEEN_DELETED)
                    .build();
        }
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);

        Date expirationDate = jwtService.getExpirationDate(jwtToken);
        long expiresIn = expirationDate.getTime();

        return AuthenticationResponseDTO.builder()
                .code(HTTP_OK)
                .message(LOGIN_SUCCESS)
                .access_token(jwtToken)
                .refresh_token(refreshToken)
                .username(account.getFirstname() + " " + account.getLastname())
                .access_token_expires_in(expiresIn)
                .build();
    }



//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // * gửi về rft
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.accountRepository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .access_token(accessToken)
//                        .refresh_token(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}
