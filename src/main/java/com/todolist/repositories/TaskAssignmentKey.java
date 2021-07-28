package com.todolist.repositories;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class TaskAssignmentKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "TASK_ID")
    private Long taskId;

    @Column(name = "USER_ID")
    private Long userId;


}
