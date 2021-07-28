package com.todolist.entities;

import com.todolist.repositories.TaskAssignmentKey;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "TASK_ASSIGNMENT", schema = "TESTDB")
public class TaskAssignment {

    @EmbeddedId
    private TaskAssignmentKey assignmentKey;

}
