package com.todolist.services;

import com.todolist.model.TaskAssignmentResponse;

import java.util.List;

public interface TaskAssignmentService {
    List<TaskAssignmentResponse> getAllTasksWithAssignedUsers();
}
