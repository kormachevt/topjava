package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("By cycles");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println("By one cycle");
        List<UserMealWithExcess> mealsToByOneCycle = filteredByCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsToByOneCycle.forEach(System.out::println);

        System.out.println("By streams");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println("By stream");
        System.out.println(filteredByStream(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                            int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            int caloriesOfDay = caloriesByDate.getOrDefault(date, 0);
            caloriesOfDay += meal.getCalories();
            caloriesByDate.put(date, caloriesOfDay);
        }

        List<UserMealWithExcess> excessMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            boolean excess = caloriesByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                excessMeals.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                                                       excess));
            }
        }
        return excessMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                             int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDate = meals.stream().collect(Collectors.groupingBy(item -> item.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    boolean excess = caloriesByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                                                  excess);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                           int caloriesPerDay) {
        Map<LocalDate, List<UserMeal>> mealsByDate = new HashMap<>();
        Map<LocalDateTime, UserMealWithExcess> excessMealsByDateTime = new HashMap<>();
        Map<LocalDate, Integer> caloriesByDate = new HashMap<>();
        Map<LocalDate, Boolean> isExcessByDate = new HashMap<>();

        for (UserMeal meal : meals) {
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
                        excessMealsByDateTime.put(userMeal.getDateTime(), new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                    }
                }
            } else {
                if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    excessMealsByDateTime.put(dateTime, new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), currentIsExcess));
                }
            }
        }
        return new ArrayList<>(excessMealsByDateTime.values());
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime,
                                                            int caloriesPerDay) {
        return meals.stream().collect(new ExcessCollector(caloriesPerDay, startTime, endTime));
    }
}
