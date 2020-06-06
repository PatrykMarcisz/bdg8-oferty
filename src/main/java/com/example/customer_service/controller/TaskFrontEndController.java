package com.example.customer_service.controller;

import com.example.customer_service.model.Task;
import com.example.customer_service.model.User;
import com.example.customer_service.service.TaskService;
import com.example.customer_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDate;


@Controller
public class TaskFrontEndController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskFrontEndController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/addTask")
    public String addTask(Authentication auth, Model model) {
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails) auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("task", new Task());                                 // pusty obiekt taska
        model.addAttribute("cats", taskService.getAllTaskCategories());   // lista wszystkich dostępnych w db kategorii
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTask(
            @ModelAttribute @Valid Task task,
            BindingResult bindingResult,
            Authentication auth
    ) {
        if (bindingResult.hasErrors()) {
            return "addTask";
        }
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String loggedEmail = principal.getUsername();
        User loggedUser = userService.getUserByEmail(loggedEmail);
        taskService.addTask(task.getContent(), task.getPrice(), task.getCategories(), loggedUser);
        return "redirect:/";
    }

    @GetMapping("/tasks&{taskId}")
    public String addTaskToBasket(
            Authentication auth,
            @PathVariable("taskId") Long taskId,
            Model model
    ) {
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails) auth.getPrincipal()).getUsername() : "");    // do sprawdzenia właściciela zadania
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        model.addAttribute("isAdmin", userService.hasRole(auth, "ROLE_ADMIN"));         // do sprawdzania uprawnień R_A
        model.addAttribute("isCompany", userService.hasRole(auth, "ROLE_COMPANY"));     // do sprawdzania uprawnień R_C
        model.addAttribute("loggedEmail", auth != null ? ((UserDetails) auth.getPrincipal()).getUsername() : "");
        model.addAttribute("info",
                "zlecenie " + taskService.getTaskById(taskId).get().getContent() + " zostało przyjęte " +
                        "(termin realizacji: " + LocalDate.now().plusDays(7) + ")");
        // przypisanie takska do koszyka zalogowanego użytkownika
        taskService.addTaskToUserBasket(auth, taskId);
        return "index";
    }

    @GetMapping("/delete_task&{task_id}")
    public String deleteTaskById(@PathVariable("task_id") Long taskId) {
        taskService.updateTaskStatusById(taskId, false);
        return "redirect:/";
    }

}
