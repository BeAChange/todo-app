package com.todolist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.todolist.entities.Task;
import com.todolist.util.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoListDto {

    private Long id;
    private String title;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createdDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date modifiedDate;

    private Set<Task> tasks;

}
