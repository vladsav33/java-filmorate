package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("baseModelStorage")
@Slf4j
public abstract class BaseModelStorage<T extends BaseModel> {
    private final Map<Integer, T> models = new HashMap<>();
    private int idCounter;

    public Collection<T> get() {
        return models.values();
    }

    public Optional<T> getById(int id) {
        return Optional.ofNullable(models.get(id));
    }

    public T create(T baseModel) {
        baseModel.setId(++idCounter);
        models.put(baseModel.getId(), baseModel);
        log.debug("Создана модель: {}", baseModel);
        return baseModel;
    }

    public Optional<T> update(T baseModel) {
        if (!models.containsKey(baseModel.getId())) {
            return Optional.empty();
        }
        models.put(baseModel.getId(), baseModel);
        log.debug("Обновлена модель: {}", baseModel);
        return Optional.of(baseModel);
    }
}
