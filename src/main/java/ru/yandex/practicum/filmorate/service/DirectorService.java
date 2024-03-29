package ru.yandex.practicum.filmorate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    @Qualifier("directorDbStorage")
    @NonNull
    private final DirectorStorage directorStorage;

    public List<Director> get() {
        return directorStorage.get();
    }

    public Director getById(int id) {
        return getIfDirectorExists(id);
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        getIfDirectorExists(director.getId());
        return directorStorage.udpate(director).get();
    }

    public void delete(int id) {
        directorStorage.delete(id);
    }

    public Director getIfDirectorExists(int id) {
        return directorStorage.getById(id)
                .orElseThrow(() -> new DirectorNotFoundException(
                        String.format("Режиссер с id = %d отсутствует в БД.", id)));
    }
}