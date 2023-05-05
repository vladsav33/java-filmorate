package ru.yandex.practicum.filmorate.enums;

import org.springframework.core.convert.converter.Converter;

public enum SortCategoryType {
    YEAR,
    LIKES;

    public static class StringToEnumConverter implements Converter<String, SortCategoryType> {
        @Override
        public SortCategoryType convert(String source) {
            return SortCategoryType.valueOf(source.toUpperCase());
        }
    }
}