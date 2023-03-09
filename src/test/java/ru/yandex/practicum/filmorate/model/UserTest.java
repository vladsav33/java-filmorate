package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    private static Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void validateCorrectUser() {
        User user = User.builder()
                .id(1)
                .email("test@test.test")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertEquals(0, violationSet.size());
    }

    @Test
    void validateWrongEmail() {
        User user = User.builder()
                .id(1)
                .email("@test.test")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertEquals(1, violationSet.size());
    }

    @Test
    void validateWrongLogin() {
        User user = User.builder()
                .id(1)
                .email("test@test.test")
                .login("")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertEquals(1, violationSet.size());
    }

    @Test
    void validateWrongBirthday() {
        User user = User.builder()
                .id(1)
                .email("test@test.test")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2024, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        assertEquals(1, violationSet.size());
    }
}