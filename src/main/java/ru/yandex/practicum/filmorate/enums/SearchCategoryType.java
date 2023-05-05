package ru.yandex.practicum.filmorate.enums;

import org.springframework.core.convert.converter.Converter;

public enum SearchCategoryType {
    TITLE,
    DIRECTOR;

    public static class StringToEnumConverter implements Converter<String, SearchCategoryType> {
        @Override
        public SearchCategoryType convert(String source) {
            return SearchCategoryType.valueOf(source.toUpperCase());
        }
    }
}
