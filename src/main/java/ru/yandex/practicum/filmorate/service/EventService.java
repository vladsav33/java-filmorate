package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.EventStorage;
import ru.yandex.practicum.filmorate.enums.ActionType;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    @Qualifier("eventDbStorage")
    @NonNull
    private final EventStorage eventStorage;

    public List<Event> findByUserId(int userId) {
        return eventStorage.getByUserId(userId);
    }

    public Event createEvent(int userId, ActionType actionType, EventType eventType, long entityId) {
        return eventStorage.create(userId, eventType, actionType, entityId);
    }
}
