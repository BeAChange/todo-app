package com.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentResponse {

    private Long taskId;
    private String taskName;
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
}
