package ru.javawebinar.topjava.storage;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsMemoryStorage implements MealsStorage {
    private static final Logger log = getLogger(MealsMemoryStorage.class);
    private final ExecutorService executorService = Executors.newFixedThreadPool(200);
    private Map<Integer, Meal> mealsById = new ConcurrentHashMap<>();
    private AtomicInteger atomicInt = new AtomicInteger(0);

    @Override
    public Meal get(int id) {
        return mealsById.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealsById.values());
    }

    @Override
    public void add(Meal meal) {
        int id = atomicInt.getAndIncrement();
        meal.setId(id);
        mealsById.put(id, meal);
    }

    @Override
    public void update(Meal meal) {

        mealsById.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealsById.remove(id);
    }
}