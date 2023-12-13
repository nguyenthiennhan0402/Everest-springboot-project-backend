package com.dtvn.springbootproject.config;

import com.dtvn.springbootproject.exceptions.ErrorException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.dtvn.springbootproject.constants.AuthConstants.*;
import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Info comment:
     * This class is responsible for handling JWT authentication in the Spring Security filter chain.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String userEmail;

        // * Check if the request has the Authorization header with a Bearer token.
        // ? If not, proceed with the filter chain.
        if(authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // * Extract the JWT token from the Authorization header.
        jwt = authHeader.substring(BEARER_PREFIX_LENGTH); //"Bearer " dài 7 kí tự
        userEmail = jwtService.extractUsername(jwt);

        // * Check if there is a userEmail in the token and check if the user is authenticated or not
        // ? When SecurityContextHolder.getContext().getAuthentication() == null means that user is not authenticated
        // ? If not, you will have to go back to UserDetailService to find out if that user is in the database or not
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // * Load user details from the UserDetailsService using the userEmail.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // * Validate the token and authenticate the user if the token is valid.
            // ? If the token is not valid, proceed with the filter chain.
            if(jwtService.isTokenValid(jwt, userDetails)){
                // * Create an authentication token and set it in the SecurityContextHolder.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                throw new ErrorException(ERROR_TOKEN_INVALID, HTTP_FORBIDDEN);
            }

        }
        // * Proceed with the filter chain.
        filterChain.doFilter(request,response);
    }
}
