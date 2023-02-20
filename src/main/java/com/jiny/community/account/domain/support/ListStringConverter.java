package com.jiny.community.account.domain.support;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class ListStringConverter implements AttributeConverter<List<String>,String> {

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        return Optional.ofNullable(list)
                .map(str->String.join(",",str))
                .orElse("");
    }

    @Override
    public List<String> convertToEntityAttribute(String dataString) {
        return Stream.of(dataString.split(","))
                .collect(Collectors.toList());
    }
}
