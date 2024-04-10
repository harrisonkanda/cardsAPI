package com.logicea.logiceacardsproject.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logicea.logiceacardsproject.enums.CardStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardRequestDto {

    @NotBlank(message = "Name must be provided")
    private String name;
    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$")
    private String color;
    private String description;
    private CardStatus status;
}
