package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
public class Friendship {
    private int userId;
    private int friendId;
    private boolean confirmed;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", userId);
        values.put("friend_id", friendId);
        values.put("confirmed", confirmed);
        return values;
    }
}
