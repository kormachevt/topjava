package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.toTimestamp;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("date_time", toTimestamp(meal.getDateTime()))
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("user_id", userId);


        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET date_time=:date_time, description=:description, calories=:calories " +
                        "WHERE id=:id AND user_id=:user_id",
                map) == 0) {
            return null;
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("user_id", userId);
        return namedParameterJdbcTemplate.update("DELETE FROM meals WHERE id=:id AND user_id=:user_id", map) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("user_id", userId);

        List<Meal> meals = namedParameterJdbcTemplate.query("SELECT * FROM meals " +
                                                                    "WHERE user_id=:user_id AND id=:id",
                                                            map, ROW_MAPPER);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals " +
                                          "WHERE user_id=? " +
                                          "ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        Timestamp startTs = toTimestamp(startDate);
        Timestamp endTs = toTimestamp(endDate);
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("start_ts", startTs)
                .addValue("end_ts", endTs);
        return namedParameterJdbcTemplate.query("SELECT * FROM meals " +
                                                        "WHERE user_id=:user_id " +
                                                        "AND date_time BETWEEN :start_ts AND :end_ts " +
                                                        "ORDER BY date_time DESC", map, ROW_MAPPER);
    }
}
