package com.todolist.services;

import com.todolist.dto.TaskDto;
import com.todolist.entities.Task;
import com.todolist.exception.TodoNotFoundException;
import com.todolist.model.TaskAssignmentRequest;
import com.todolist.model.TaskAssignmentResponse;

import java.util.List;


public interface TaskService {
    Task createTask(TaskDto added);

    Task deleteById(Long id) throws TodoNotFoundException;

    List<Task> getAllTasks();

    Task findById(Long id);

    Task updateTask(TaskDto updated);

    List<Task> findTasksByStatus(String statusCode);

    TaskAssignmentResponse assignTask(TaskAssignmentRequest assignmentRequest);

    List<Task> findAllTaskWithDueDate(Integer noOfDueDays);
}
