package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "B", "test_3@email.com", "qwerty123", Role.USER),
            new User(null, "A", "test_2@email.com", "qwerty123", Role.USER),
            new User(null, "C", "test_1@email.com", "qwerty123", Role.USER),
            new User(null, "C", "test_0@email.com", "qwerty123", Role.USER)
    );

}
