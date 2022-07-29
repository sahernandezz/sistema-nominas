package com.example.sistemanominas.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class ObjectDto {
    private Optional<Object> object = Optional.empty();
    private String message = "";

    public ObjectDto(Optional<Object> Object) {
        this.object = Object;
    }

    public ObjectDto(Optional<Object> Object, String message) {
        this.object = Object;
        this.message = message;
    }

    public ObjectDto(String message) {
        this.message = message;
    }
}
