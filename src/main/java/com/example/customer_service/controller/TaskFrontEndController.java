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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
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
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String loggedEmail = principal.getUsername();
        User loggedUser = userService.getUserByEmail(loggedEmail);
        Task task = new Task();
        task.setUser(loggedUser);
        model.addAttribute("task", task);                                 // pusty obiekt taska
        model.addAttribute("categories", taskService.getAllTaskCategories());   // lista wszystkich dostępnych w db kategorii
        return "addTask";
    }
    @PostMapping("/addTask")
    public String addTask(
            @ModelAttribute @Valid Task task, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return "addTask";
//        }
        taskService.addTask(task.getContent(), task.getPrice(), new HashSet<>(), null);
        return "redirect:/";
    }
    @GetMapping("/tasks&{taskId}")
    public String addTaskToBasket(
            Authentication auth,
            @PathVariable("taskId") Long taskId,
            Model model
    ){
        model.addAttribute("isLogged", auth != null);
        model.addAttribute("info", "zlecenie zostało przyjęte");
        model.addAttribute("tasks", taskService.getAllTasksOrderByPublicationDateDesc());
        // ???
        return "index";
    }
}
