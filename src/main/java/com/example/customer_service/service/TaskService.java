package com.example.customer_service.service;

import com.example.customer_service.model.Basket;
import com.example.customer_service.model.Category;
import com.example.customer_service.model.Task;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.BasketRepository;
import com.example.customer_service.repository.CategoryRepository;
import com.example.customer_service.repository.TaskRepository;
import com.example.customer_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private BasketRepository basketRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository, UserRepository userRepository, BasketRepository basketRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.basketRepository = basketRepository;
    }

    public List<Category> getAllTaskCategories() {
        return categoryRepository.findAll();
    }
    public List<Task> getAllTasksOrderByPublicationDateDesc() {
        return taskRepository.findAll(
                Sort.by(Sort.Direction.DESC, "publicationDate"));
    }

    public void addTask(String content, Double price, Set<Category> categories, User user) {
        Task task = new Task(categories, content, price, user);
        task.setPublicationDate(LocalDateTime.now());
        System.out.println(task.getCategories());
        taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }
    public void addTaskToUserBasket(Authentication auth, Long taskId){
        if(taskRepository.findById(taskId).isPresent()) {
            Task task = taskRepository.findById(taskId).get();
            User user = userRepository.findUserByEmail(((UserDetails) auth.getPrincipal()).getUsername());
            // utworzenie obiektu klasy Basket
            Basket basket = new Basket(LocalDate.now(), LocalDate.now().plusDays(7), task, user);
            // zapis koszyka przez repo
            basketRepository.save(basket);
        }
    }
    public void updateTaskStatusById(Long taskId, Boolean status){
        if(taskRepository.findById(taskId).isPresent()) {
            Task task = taskRepository.findById(taskId).get();
            task.setStatus(status);
            taskRepository.save(task);
        }
    }
    public List<Basket> getBasketsForUser(Authentication auth){
        UserDetails principal = (UserDetails) auth.getPrincipal();
        User user = userRepository.findUserByEmail(principal.getUsername());
        return basketRepository.findAllByUser(user);
    }
}
