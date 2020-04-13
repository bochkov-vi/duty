package com.bochkov.duty.jpa.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.util.Optional;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Duration attribute) {
        return Optional.ofNullable(attribute).map(d -> Long.valueOf(d.toMinutes()).intValue()).orElse(null);
    }

    @Override
    public Duration convertToEntityAttribute(Integer dbData) {
        return Optional.ofNullable(dbData).map(Duration::ofMinutes).orElse(null);
    }
}
