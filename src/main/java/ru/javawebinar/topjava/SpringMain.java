package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll().forEach(System.out::println);

            System.out.println("GET");
            System.out.println(mealRestController.get(1));

            System.out.println("CREATE");
            System.out.println(mealRestController.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500, 2)));

            System.out.println("UPDATE");
//            mealRestController.update(8, new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "REDACTED", 500, 2));
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.getAll().forEach(System.out::println);

            System.out.println("DELETE");
//            mealRestController.delete(8);
            mealRestController.getAll().forEach(System.out::println);

            System.out.println("GET ALL FILTERED");
            System.out.println(mealRestController.getAllFiltered(null, null, null, null).size());
            mealRestController.getAllFiltered(LocalDate.of(2020, 1, 30),
                                              LocalDate.of(2020, 2, 1),
                                              LocalTime.of(0, 0),
                                              LocalTime.of(20, 0)).forEach(System.out::println);
        }
    }
}
