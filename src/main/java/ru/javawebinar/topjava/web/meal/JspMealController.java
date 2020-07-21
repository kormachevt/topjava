package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(@Autowired MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/filter")
    public String getFiltered(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int id = getId(request);
        delete(id);
        return "redirect:/meals";
    }

//    @GetMapping("/create")
//    public String getCreateForm(Model model) {
//        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
//        model.addAttribute("meal", meal);
//        model.addAttribute("action", "create");
//        return "mealForm";
//    }

    @GetMapping("/form")
    public String getForm(HttpServletRequest request, Model model) {
        final Meal meal = isCreate(request)
                ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000)
                : get(getId(request));
        model.addAttribute("meal", meal);
        model.addAttribute("action", "update");
        return "mealForm";
    }

    @PostMapping
    public String post(HttpServletRequest request) {
        if (isCreate(request)) {
            create(getMeal(request));
        } else {
            update(getMeal(request), getId(request));
        }
        return "redirect:/meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private boolean isCreate(HttpServletRequest request){
        return StringUtils.isEmpty(request.getParameter("id"));
    }

    private Meal getMeal(HttpServletRequest request) {
        return new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

    }
}
