package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository meals;

    private final CrudUserRepository users;

    public DataJpaMealRepository(CrudMealRepository meals, CrudUserRepository users) {
        this.meals = meals;
        this.users = users;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(users.getOne(userId));
        if (!meal.isNew() && get(meal.id(), userId) == null) {
            return null;
        }
        return meals.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return meals.deleteByIdAndUser(id, users.getOne(userId)) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return meals.findByIdAndUser(id, users.getOne(userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return meals.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return meals.getAllFiltered(userId, startDateTime, endDateTime);
    }
}
