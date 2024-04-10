package com.logicea.logiceacardsproject.security.jwt;

import com.logicea.logiceacardsproject.exception.JwtExpiredTokenException;
import com.logicea.logiceacardsproject.exception.JwtMulformedTokenException;
import com.logicea.logiceacardsproject.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractJwtFromRequestHeader(request);
        Claims claims = null;

        if (accessToken == "") {
            throw new JwtMulformedTokenException("Missing or mulformed access token. " +
                    "Valid access token must be supplied in the Request Header");
        }
        try {
            claims = jwtTokenProviderService.getClaimsFromJwtToken(accessToken);
        } catch (JwtExpiredTokenException e) {
            throw new JwtExpiredTokenException("Invalid or expired token");
        }

        if (accessToken != null && !claims.isEmpty()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (claims.getSubject() != null && authentication == null) {
                UserDetails userDetails = userService.loadUserByEmail(claims.getSubject());

                if (jwtTokenProviderService.isValidateToken(accessToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        }
        filterChain.doFilter(request, response);
    }


    /**
     * Extracts JWT token from the request header received from the Http Request
     *
     * @param request
     * @return token
     */
    private String extractJwtFromRequestHeader(final HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
