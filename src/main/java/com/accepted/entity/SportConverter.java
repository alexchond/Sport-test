package com.accepted.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SportConverter implements AttributeConverter<Sport, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sport sport) {
        if (sport == null) return null;
        return sport.getValue();
    }

    @Override
    public Sport convertToEntityAttribute(Integer value) {
        if (value == null) return null;
        return Sport.fromValue(value);
    }
}
