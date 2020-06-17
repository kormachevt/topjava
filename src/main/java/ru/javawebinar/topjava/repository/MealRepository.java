package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    // null if not found, when updated
    Meal save(Integer userId, Meal meal);

    // false if not found
    boolean delete(int id, Integer userId);

    // null if not found
    Meal get(int id, Integer userId);

    List<Meal> getAll(Integer userId);
}
