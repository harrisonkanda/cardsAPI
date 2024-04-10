package com.logicea.logiceacardsproject.controller;


import static com.logicea.logiceacardsproject.constants.ConfigurationConstant.API_VERSION_URL;
import static com.logicea.logiceacardsproject.constants.ConfigurationConstant.AUTH_API_URI;

import com.logicea.logiceacardsproject.dto.request.UserLoginDto;
import com.logicea.logiceacardsproject.dto.response.ErrorsResponseDto;
import com.logicea.logiceacardsproject.dto.response.TokenResponse;
import com.logicea.logiceacardsproject.security.jwt.JwtTokenProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login-API", description = "Login & Token APIs")
@RequestMapping(API_VERSION_URL + AUTH_API_URI)
@RestController
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProviderService jwtTokenProviderService;

    @Autowired
    public AuthController(final AuthenticationManager authenticationManager, final JwtTokenProviderService jwtTokenProviderService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    @Operation(summary = "For Login and Token Generation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = TokenResponse.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "401",description = "Unauthorized", content = { @Content(schema = @Schema()) })
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto userLoginRequest, BindingResult result) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProviderService.generateAccessToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok().body(TokenResponse.builder().token(token).status("Success").build().toString());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorsResponseDto.builder()
                    .message(exception.getMessage()).cause(exception.getCause().getLocalizedMessage()).status("Failed").build().toString());
        }
    }
}
