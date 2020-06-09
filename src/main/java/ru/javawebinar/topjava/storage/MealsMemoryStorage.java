package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealsMemoryStorage implements MealsStorage {
    private Map<Integer, Meal> mealsById = new ConcurrentHashMap<>();
    private AtomicInteger atomicInt = new AtomicInteger(1);

    @Override
    public Meal get(int id) {
        return mealsById.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsById.values());
    }

    @Override
    public Meal add(Meal meal) {
        int id = atomicInt.getAndIncrement();
        meal.setId(id);
        mealsById.put(id, meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        int id = meal.getId();
        if (mealsById.containsKey(id)) {
            mealsById.put(id, meal);
            return meal;
        }
        throw new IllegalArgumentException("Meal not exist");
    }

    @Override
    public void delete(int id) {
        mealsById.remove(id);
    }
}