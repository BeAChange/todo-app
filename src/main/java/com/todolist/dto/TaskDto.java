package com.todolist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.todolist.util.Constants;
import com.todolist.util.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private Long id;

    @Length(min = Constants.MIN_LENGTH_TASKNAME, max = Constants.MAX_LENGTH_TASKNAME)
    private String name;

    @Length(max = Constants.MAX_LENGTH_DESCRIPTION)
    private String description;

    private String status;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createdDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date dueDate;

    private BigDecimal totalTimeSpent;

    private BigDecimal prvTotalTimeSpent;
}
