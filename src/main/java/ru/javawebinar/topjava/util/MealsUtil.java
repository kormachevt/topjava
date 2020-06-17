package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "1Завтрак", 500, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "1Обед", 1000, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "1Ужин", 500, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "1Еда на граничное значение", 100, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "1Завтрак", 1000, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "1Обед", 500, 1),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "1Ужин", 410, 1),

            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "2Завтрак", 500, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "2Обед", 1000, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "2Ужин", 500, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "2Еда на граничное значение", 100, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "2Завтрак", 1000, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "2Обед", 500, 2),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "2Ужин", 410, 2)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals,
                                              int caloriesPerDay,
                                              LocalDate startDate,
                                              LocalDate endDate,
                                              LocalTime startTime,
                                              LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate.plusDays(1))
                && DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .sorted(Comparator.comparing(MealTo::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
