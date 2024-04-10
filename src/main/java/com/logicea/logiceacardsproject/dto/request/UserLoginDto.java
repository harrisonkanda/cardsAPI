package com.logicea.logiceacardsproject.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginDto {

    @NotNull(message = "Email address cannot be empty")
    @JsonProperty("email")
    private String email;
    @NotNull(message = "Password cannot be empty")
    @JsonProperty("password")
    private String password;
}
