package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class ExcessCollector implements Collector<UserMeal, Map<LocalDateTime, UserMealWithExcess>, List<UserMealWithExcess>> {
    private int caloriesPerDay;
    private LocalTime startTime;
    private LocalTime endTime;
    private Map<LocalDate, List<UserMeal>> mealsByDate = new HashMap<>();
    private Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
    private Map<LocalDate, Boolean> isExcessByDate = new HashMap<>();

    public ExcessCollector(int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        this.caloriesPerDay = caloriesPerDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public Supplier<Map<LocalDateTime, UserMealWithExcess>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<LocalDateTime, UserMealWithExcess>, UserMeal> accumulator() {
        return (map, meal) -> {
            LocalDateTime dateTime = meal.getDateTime();
            LocalDate date = dateTime.toLocalDate();
            List<UserMeal> mealsOfDate = mealsByDate.getOrDefault(date, new ArrayList<>());

            int calories = caloriesByDate.getOrDefault(date, 0);
            calories += meal.getCalories();
            caloriesByDate.put(date, calories);

            boolean oldIsExcess = isExcessByDate.getOrDefault(date, false);
            boolean currentIsExcess = calories > caloriesPerDay;
            isExcessByDate.put(date, currentIsExcess);

            mealsOfDate.add(meal);
            mealsByDate.put(date, mealsOfDate);

            if (currentIsExcess && !oldIsExcess) {
                for (UserMeal userMeal : mealsOfDate) {
                    if (isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                        map.put(userMeal.getDateTime(), new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                    }
                }
            } else {
                if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    map.put(dateTime, new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), currentIsExcess));
                }
            }
        };
    }

    @Override
    public BinaryOperator<Map<LocalDateTime, UserMealWithExcess>> combiner() {
        return (map1, map2) -> {
            map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> v1));
            return map1;
        };
    }

    @Override
    public Function<Map<LocalDateTime, UserMealWithExcess>, List<UserMealWithExcess>> finisher() {
        return map -> new ArrayList<>(map.values());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}
