package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

public interface MPAStorage {

    public Collection<MPA> get();

    public Optional<MPA> getById(int id);

}
