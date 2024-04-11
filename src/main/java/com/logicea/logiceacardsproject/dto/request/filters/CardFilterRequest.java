package com.logicea.logiceacardsproject.dto.request.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logicea.logiceacardsproject.enums.CardStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardFilterRequest {

    private String name;
    private String color;
    private CardStatus status;
    private String dateCreated;
}
