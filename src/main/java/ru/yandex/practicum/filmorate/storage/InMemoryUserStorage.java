package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUser;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int idCounter = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public List<User> findAll() {
        log.warn("The list of users returned");
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        user.setId(++idCounter);
        users.put(user.getId(), user);
        log.info("Another user {} is added", user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        users.put(user.getId(), user);
        log.info("User {} is updated", user);
        return user;
    }

    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("Such user was not found");
            throw new NoSuchUser();
        }
        log.info("User {} is retrieved", userId);
        return users.get(userId);
    }

    public User addFriend(int userId, int friendId) {
        User user = getUserById(userId);
        user.getFriends().add(friendId);
        log.info("New friend {} was added", friendId);

        User friend = getUserById(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    public User deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        user.getFriends().remove(friendId);
        log.info("Friend {} was deleted", friendId);

        User friend = getUserById(friendId);
        friend.getFriends().remove(userId);
        return user;
    }

    public List<User> getFriends(int userId) {
        List<User> friends = users.get(userId).getFriends().stream()
                .map(u -> getUserById(u))
                .collect(Collectors.toList());
        if (friends.isEmpty()) {
            log.info("No friends were found");
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> list = getFriends(userId).stream()
                .filter(u -> (getFriends(otherId).contains(u)))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            log.info("No common friends");
        }
        return list;
    }
}
