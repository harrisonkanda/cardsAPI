package com.logicea.logiceacardsproject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Getter
@Setter
public class ResponseBodyWrapperDto<T> {
    @JsonProperty("statusDesc")
    private String statusDesc;

    private T data;

    public ResponseBodyWrapperDto(String statusDesc, T data) {
        this.statusDesc = statusDesc;
        this.data = data;
    }
}
