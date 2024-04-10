package com.logicea.logiceacardsproject.dto.request.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logicea.logiceacardsproject.enums.CardStatus;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginationFilterRequest {

    private int page = 0;
    private int pageSize = 10;
    private String sortBy = "name";
    private String sortOrder = "ASC";
}