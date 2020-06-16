package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> mealsByUserId = repository.putIfAbsent(userId, new ConcurrentHashMap<>());
            if (mealsByUserId == null) {
                repository.get(userId).put(meal.getId(), meal);
            } else {
                mealsByUserId.put(meal.getId(), meal);
            }
            return isOwn(userId, meal) ? meal : null;
        }
        // handle case: update, but not present in storage
        return isOwn(userId, meal) ? repository.get(userId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        Map<Integer, Meal> userMealsById = repository.get(userId);
        if (isOwn(userId, userMealsById.get(id))) {
            return userMealsById.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.get(userId).get(id);
        if (isOwn(userId, meal)) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAllFiltered(Integer userId,
                                     LocalDate startDate,
                                     LocalDate endDate,
                                     LocalTime startTime,
                                     LocalTime endTime) {

        Stream<Meal> stream = repository.get(userId).values().stream();
        if (!(startDate == null && endDate == null && startTime == null && endTime == null)) {
            stream = stream.filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate.plusDays(1)))
                    .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
        }
        return stream.sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList());

    }

    private boolean isOwn(Integer userId, Meal meal) {
        return userId.equals(meal.getUserId());
    }
}

