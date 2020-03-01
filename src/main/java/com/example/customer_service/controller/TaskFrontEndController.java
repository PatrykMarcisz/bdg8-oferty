package com.example.customer_service.controller;

import com.example.customer_service.model.Category;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Controller
public class TaskFrontEndController {
    private TaskService taskService;
    private UserService userService;
    @Autowired
    public TaskFrontEndController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/addTask")
    public String addTask(Authentication auth, Model model){
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
    ){
        if(bindingResult.hasErrors()){
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
    ){
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("info",
                "zlecenie "+taskService.getTaskById(taskId).get().getContent()+" zostało przyjęte " +
                   "(termin realizacji: "+ LocalDate.now().plusDays(7) +")");
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        // przypisanie takska do koszyka zalogowanego użytkownika
        taskService.addTaskToUserBasket(auth, taskId);
        return "index";
    }
    @GetMapping("/delete_task&{task_id}")
    public String deleteTaskById(@PathVariable("task_id") Long taskId){
        taskService.updateTaskStatusById(taskId, false);
        return "redirect:/";
    }

}
