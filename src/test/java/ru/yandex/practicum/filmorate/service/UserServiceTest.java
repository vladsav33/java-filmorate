package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {


    private final UserStorage inMemoryUserStorage = Mockito.mock(InMemoryUserStorage.class);

    private final UserService userService = new UserService(inMemoryUserStorage);

    private User user = User.builder()
            .email("test1@test.test")
            .login("login1")
            .name("name1")
            .birthday(LocalDate.of(2000, 1, 1))
            .build();

    private User otherUser = User.builder()
            .email("test2@test.test")
            .login("login2")
            .name("name2")
            .birthday(LocalDate.of(2005, 1, 1))
            .build();

    private final int userId = 1;
    private final int otherUserId = 10;
    private final int commonFriendId = 100;
    private final int otherFriendId = 1000;

    @BeforeEach
    void setFriends() {
        user.setFriends(new HashSet<>());
        otherUser.setFriends(new HashSet<>());
    }

    @Test
    void addFriendWhenUserIsNull() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.empty());
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.addFriend(userId, otherUserId));
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void addFriendWhenOtherUserIsNull() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.addFriend(userId, otherUserId));
        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(otherUserId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + otherUserId + " не найден.");
    }

    @Test
    void addFriend() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));

        userService.addFriend(userId, otherUserId);

        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(otherUserId);
        assertEquals(1, user.getFriends().size());
        assertEquals(Set.of(otherUserId), user.getFriends());
    }

    @Test
    void removeFriendWhenUserIsNull() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.empty());
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.removeFriend(userId, otherUserId));
        verify(inMemoryUserStorage).getById(userId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + userId + " не найден.");
    }

    @Test
    void removeFriendWhenOtherUserIsNull() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.removeFriend(userId, otherUserId));
        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(otherUserId);
        assertEquals(exception.getMessage(), "Пользователь с ID = " + otherUserId + " не найден.");
    }

    @Test
    void removeFriendWhenZeroFriends() {
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));

        userService.removeFriend(userId, otherUserId);

        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(otherUserId);
        assertEquals(0, user.getFriends().size());
    }

    @Test
    void removeFriend() {
        user.getFriends().add(otherUserId);
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));

        userService.removeFriend(userId, otherUserId);

        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(otherUserId);
        assertEquals(0, user.getFriends().size());
    }

    @Test
    void getCommonFriends() {
        user.getFriends().add(commonFriendId);
        otherUser.setFriends(Set.of(commonFriendId, otherFriendId));
        when(inMemoryUserStorage.getById(userId)).thenReturn(Optional.of(user));
        when(inMemoryUserStorage.getById(otherUserId)).thenReturn(Optional.of(otherUser));
        when(inMemoryUserStorage.getById(commonFriendId)).thenReturn(Optional.of(user));

        Collection<User> commonFriends = userService.getCommonFriends(userId, otherUserId);

        verify(inMemoryUserStorage).getById(userId);
        verify(inMemoryUserStorage).getById(commonFriendId);
        verify(inMemoryUserStorage, never()).getById(otherFriendId);
        assertEquals(1, commonFriends.size());
        assertEquals(List.of(user), commonFriends);
    }
}