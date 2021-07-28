package com.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAssignmentRequest {

    @NotNull
    private Long taskId;

    @NotNull
    private Long userId;
}
