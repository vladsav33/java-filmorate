package ru.yandex.practicum.filmorate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.yandex.practicum.filmorate.enums.SearchCategoryType;
import ru.yandex.practicum.filmorate.enums.SortCategoryType;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SearchCategoryType.StringToEnumConverter());
        registry.addConverter(new SortCategoryType.StringToEnumConverter());
    }
}