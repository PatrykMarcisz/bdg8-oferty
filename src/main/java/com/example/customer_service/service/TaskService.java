package com.example.customer_service.service;

import com.example.customer_service.model.Task;
import com.example.customer_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private TaskRepository taskRepository;
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasksOrderByPublicationDateDesc(){
        return null;
    }
    public Boolean addTask(String content, Double price){
        return false;
    }
    public Task getTaskById(Long taskId){
        return null;
    }
}
