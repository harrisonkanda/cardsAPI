package com.logicea.logiceacardsproject.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logicea.logiceacardsproject.enums.CardStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardResponseDto {
    private UUID cardId;
    private String name;
    private String color;
    private String description;
    public CardStatus status;
    private Date date_created;
}
