package com.todolist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.todolist.util.JsonDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TASK", schema = "TESTDB")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "TOTAL_TIME_SPENT")
    private BigDecimal totalTimeSpent;

    @Column(name = "PRV_TOTAL_TIME_SPENT")
    private BigDecimal prvTotalTimeSpent;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @JsonIgnore
    @ManyToOne
    @JoinTable(name = "TASK_ASSIGNMENT", schema = "TESTDB",
            joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private UserProfile userProfile;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TODOLIST_TASK", schema = "TESTDB",
            joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "todolist_id", referencedColumnName = "id")})
    private List<TodoList> todoLists;
}
