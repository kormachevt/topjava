package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL_02_ID = 100002;
    public static final int NOT_FOUND = 10;
    public static final Meal MEAL_02 = new Meal(MEAL_02_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак_1", 500);
    public static final Meal MEAL_03 = new Meal(100003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед_1", 1000);
    public static final Meal MEAL_04 = new Meal(100004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин_1", 500);
    public static final Meal MEAL_05 = new Meal(100005, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение_1", 100);
    public static final Meal MEAL_06 = new Meal(100006, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак_1", 1000);
    public static final Meal MEAL_07 = new Meal(100007, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед_1", 500);
    public static final Meal MEAL_08 = new Meal(100008, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин_1", 410);

    public static final Meal MEAL_09 = new Meal(100009, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак_2", 500);
    public static final Meal MEAL_10 = new Meal(100010, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед_2", 1000);
    public static final Meal MEAL_11 = new Meal(100011, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин_2", 500);
    public static final Meal MEAL_12 = new Meal(100012, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение_2", 100);
    public static final Meal MEAL_13 = new Meal(100013, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак_2", 1000);
    public static final Meal MEAL_14 = new Meal(100014, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед_2", 500);
    public static final Meal MEAL_15 = new Meal(100015, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин_2", 410);

    public static final LocalDate BEGIN_DATE = LocalDate.of(2020, Month.JANUARY, 30);
    public static final LocalDate END_DATE = LocalDate.of(2020, Month.JANUARY, 31);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Прием пищи", 999);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL_04);
        updated.setDescription("Новый прием пищи");
        updated.setCalories(444);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
