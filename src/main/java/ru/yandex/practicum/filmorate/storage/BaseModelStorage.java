package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public abstract class BaseModelStorage<T extends BaseModel> {
    private final Map<Integer, T> models = new HashMap<>();
    private int idCounter;

    public Collection<T> get() {
        return models.values();
    }

    public T create(T baseModel) {
        baseModel.setId(++idCounter);
        models.put(baseModel.getId(), baseModel);
        log.debug("Создана модель: {}", baseModel);
        return baseModel;
    }

    public T update(T baseModel) {
        if (!models.containsKey(baseModel.getId())) {
            return null;
        }
        models.put(baseModel.getId(), baseModel);
        log.debug("Обновлена модель: {}", baseModel);
        return baseModel;
    }
}
