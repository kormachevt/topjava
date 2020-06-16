package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;


@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Integer userId, Meal meal) {
        return repository.save(userId, meal);
    }

    public void delete(int id, Integer userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, Integer userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAll(Integer userId) {
        return repository.getAllFiltered(userId, null, null, null, null);
    }

    public List<Meal> getAllFiltered(Integer userId,
                                     LocalDate startDate,
                                     LocalDate endDate,
                                     LocalTime startTime,
                                     LocalTime endTime) {

        return repository.getAllFiltered(userId, startDate, endDate, startTime, endTime);
    }

    public void update(Integer userId, Meal meal) {
        checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }
}