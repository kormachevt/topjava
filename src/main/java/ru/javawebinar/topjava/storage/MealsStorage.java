package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsStorage {
    Meal get(int id);

    List<Meal> getAll();

    void add(Meal meal);

    void update(Meal meal);

    void delete(int id);
}
