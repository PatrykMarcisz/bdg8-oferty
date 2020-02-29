package com.example.customer_service.service;

import com.example.customer_service.model.Category;
import com.example.customer_service.model.Task;
import com.example.customer_service.model.User;
import com.example.customer_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    public List<Task> getAllTasksOrderByPublicationDateDesc(){
        return taskRepository.findAll(
                Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
    public void addTask(String content, Double price, Set<Category> categories, User user){
        Task task = new Task(categories, content, price, user);
        task.setPublicationDate(LocalDateTime.now());
        taskRepository.save(task);
    }
    public Optional<Task> getTaskById(Long taskId){
        return taskRepository.findById(taskId);
    }

}
