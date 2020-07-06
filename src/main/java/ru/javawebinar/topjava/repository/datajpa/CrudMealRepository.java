package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findByIdAndUser(int id, User user);

    @Transactional
    @Modifying
    int deleteByIdAndUser(int id, User user);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("userId") int userId);

    @Query("SELECT m FROM Meal m " +
           "WHERE m.user.id=:userId " +
           "AND m.dateTime >= :startDateTime AND m.dateTime < :endDateTime " +
           "ORDER BY m.dateTime DESC")
    List<Meal> getAllFiltered(@Param("userId") int userId,
                              @Param("startDateTime") LocalDateTime startDateTime,
                              @Param("endDateTime") LocalDateTime endDateTime);
}
