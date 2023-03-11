package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.HttpMethodException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ValidateService validateService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Вывести всех пользователей");
        return userRepository.get();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid User user, BindingResult bindingResult) {
        log.info("Создаем пользователя: {}", user);
        generateCustomValidateException(user, bindingResult);
        validateService.validateUser(user);
        return userRepository.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user, BindingResult bindingResult) {
        log.info("Обновляем пользователя: {}", user);
        generateCustomValidateException(user, bindingResult);
        validateService.validateUser(user);
        User updatedUser = userRepository.update(user);
        if (updatedUser == null) {
            log.warn("Пользователь с таким ID отсутствует: {}", user);
            throw new HttpMethodException("Пользователь с таким ID отсутствует. Используйте метод POST");
        }
        return updatedUser;
    }

    private void generateCustomValidateException(User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Ошибка в заполнении поля {} - {}. Пользователь - {}", bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage(), user);
            throw new UserValidationException("Ошибка в заполнении поля " + bindingResult.getFieldError().getField());
        }
    }
}
