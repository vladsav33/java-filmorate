package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MPAStorage;
import ru.yandex.practicum.filmorate.exception.MPANotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MPAService {
    @Qualifier("mpaDbStorage")
    private final MPAStorage mpaStorage;

    public Collection<MPA> findAll() {
        return mpaStorage.get();
    }

    public MPA findById(int mpaId) {
        return checkMPAId(mpaId);
    }

    private MPA checkMPAId(int id) {
        return mpaStorage.getById(id).orElseThrow(() -> new MPANotFoundException("Рейтинг с ID = " + id + " не найден."));
    }
}
