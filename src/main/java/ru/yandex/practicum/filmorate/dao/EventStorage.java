package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.enums.ActionType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventStorage {

    Optional<Event> getById(int id);

    List<Event> getByUserId(int userId);

    Event create(int userId, EventType eventType, ActionType actionType, long entityId);
}
