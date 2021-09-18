package com.bzaja.ljudskiresursiiposlovniprocesijavafx.util;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = "value")
public class ItemDto {

    private String key;
    private String value;

    public ItemDto() {
    }

    public ItemDto(String key) {
        this.key = key;
    }

    public ItemDto(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
