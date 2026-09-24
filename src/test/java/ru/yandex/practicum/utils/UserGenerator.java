package ru.yandex.practicum.utils;

import ru.yandex.practicum.models.User;

import java.util.UUID;

public class UserGenerator {

    public static User randomUser() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        return new User(
                "test_" + unique + "@yandex.ru",
                "password" + unique,
                "User_" + unique
        );
    }

}
