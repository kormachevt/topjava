package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealsMemoryStorage;
import ru.javawebinar.topjava.storage.MealsStorage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.StringUtils.isNotEmptyString;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealsStorage storage;

    @Override
    public void init() {
        storage = new MealsMemoryStorage();
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        storage.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String idParam = request.getParameter("id");
        String dateTimeParam = request.getParameter("dateTime");
        String descriptionParam = request.getParameter("description");
        String caloriesParam = request.getParameter("calories");
        int id = isNotEmptyString(idParam) ? Integer.parseInt(idParam) : 0;
        boolean isCreate = id == 0;
        Meal meal = new Meal(LocalDateTime.parse(dateTimeParam), descriptionParam, Integer.parseInt(caloriesParam));
        if (isCreate) {
            storage.add(meal);
        } else {
            meal.setId(id);
            storage.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            List<MealTo> mealsList = MealsUtil.filteredByStreams(storage.getAll(),
                                                                 LocalTime.MIN,
                                                                 LocalTime.MAX,
                                                                 2000);
            request.setAttribute("mealsList", mealsList);
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }

        String idParam = request.getParameter("id");
        int id = isNotEmptyString(idParam) ? Integer.parseInt(idParam) : 0;
        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                Meal meal = id == 0 ? new Meal() : storage.get(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("edit.jsp").forward(request, response);
                return;
            default:
                response.sendRedirect("meals");
        }
    }
}
