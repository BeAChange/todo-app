package com.todolist.services.impl;

import com.todolist.dto.TaskDto;
import com.todolist.entities.Task;
import com.todolist.entities.UserProfile;
import com.todolist.exception.BadRequestException;
import com.todolist.exception.TodoException;
import com.todolist.exception.TodoNotFoundException;
import com.todolist.model.TaskAssignmentRequest;
import com.todolist.model.TaskAssignmentResponse;
import com.todolist.repositories.TaskRepository;
import com.todolist.repositories.UserProfileRepository;
import com.todolist.services.TaskAssignmentService;
import com.todolist.services.TaskService;
import com.todolist.util.PropertyUtil;
import com.todolist.util.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService, TaskAssignmentService {

    private TaskRepository taskRepository;

    private UserProfileRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserProfileRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Task createTask(TaskDto added) {
        Task taskEntity = new Task();
        if (Objects.isNull(added) || Objects.isNull(added.getName()) || added.getName().isEmpty()) {
            throw new BadRequestException("taskName may not be empty while adding it.");
        }
        BeanUtils.copyProperties(added, taskEntity);
        taskEntity.setStatus(TaskStatus.NOT_STARTED.getValue());
        taskEntity.setCreatedDate(new Date());
        taskEntity.setActive(true);
        return taskRepository.save(taskEntity);
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public Task deleteById(Long id) throws TodoNotFoundException {
        Task deleted = taskRepository.fetchByTaskId(id);
        log.debug("Found the task entry: {}", deleted);

        if (Objects.isNull(deleted)) {
            throw new TodoNotFoundException("No record found with given id:" + id);
        }

        try {
            taskRepository.softDelete(deleted.getId());
        } catch (Exception exp) {
            throw new TodoException("Could not able to remove the task. Reason:" + exp.getMessage());
        }
        return deleted;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) throws TodoNotFoundException {
        Task found = null;

        found = taskRepository.fetchByTaskId(id);
        log.debug("Got the user entry by Id: {}", found);

        if (Objects.isNull(found)) {
            throw new TodoNotFoundException("No data exists for the given userId:" + id);
        }
        return found;
    }

    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public Task updateTask(TaskDto updatedDto) {
        String status = updatedDto.getStatus();
        if (!StringUtils.isEmpty(status)) {
            TaskStatus statusEnum = TaskStatus.fromString(status);
            updatedDto.setStatus(statusEnum.getValue());
            log.debug("Status will be changed to: {}", statusEnum.getValue());
        }
        Task found = taskRepository.fetchByTaskId(updatedDto.getId());
        log.debug("Found the task entry: {}", found);

        if (Objects.isNull(found)) {
            throw new TodoNotFoundException("No data exists with given userId:" + updatedDto.getId());
        }

        BigDecimal totalTimeSpent = new BigDecimal(String.valueOf(updatedDto.getTotalTimeSpent()));
        totalTimeSpent = totalTimeSpent.add(new BigDecimal(String.valueOf(updatedDto.getPrvTotalTimeSpent())));
        updatedDto.setTotalTimeSpent(totalTimeSpent);
        BeanUtils.copyProperties(updatedDto, found, PropertyUtil.getNullPropertiesString(updatedDto));
        return taskRepository.save(found);
    }

    @Override
    public List<Task> findTasksByStatus(String statusCode) {
        log.debug("status value received from client:{}", statusCode);
        String trimmedStatus = statusCode.replace("\"", "");
        TaskStatus.fromString(trimmedStatus);
        log.debug("retrieving tasks with:{} status", trimmedStatus);

        List<Task> tasks = taskRepository.findByStatus(trimmedStatus);
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            throw new TodoNotFoundException("No tasks found with given status:" + trimmedStatus);
        }
        return tasks;
    }

    @Override
    public TaskAssignmentResponse assignTask(TaskAssignmentRequest assignmentRequest) {
        Task task = taskRepository.fetchByTaskId(assignmentRequest.getTaskId());
        log.debug("task entry for assignment:{}", task);

        UserProfile user = null;
        Optional<UserProfile> optionalValue = userRepository.findById(assignmentRequest.getUserId());
        if (optionalValue.isPresent())
            user = optionalValue.get();
        log.debug("user entry for assignment:{}", user);

        if (Objects.isNull(task)) {
            throw new TodoNotFoundException("No task found with given id:" + assignmentRequest.getTaskId());
        }

        if (Objects.isNull(user)) {
            throw new TodoNotFoundException("No user found with given id:" + assignmentRequest.getUserId());
        }
        task.setUserProfile(user);

        log.debug("adding the task assignment:{}", assignmentRequest);
        taskRepository.save(task);

        return TaskAssignmentResponse.builder()
                .taskId(task.getId())
                .userId(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }

    @Override
    public List<TaskAssignmentResponse> getAllTasksWithAssignedUsers() {
        List<Task> tasks = getAllTasks();
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            throw new TodoNotFoundException("There are no task exists in the system.");
        }
        return prepareTaskAssignment(tasks);
    }

    @Override
    public List<Task> findAllTaskWithDueDate(Integer dueDays) {
        dueDays = dueDays != null ? dueDays : 1;
        LocalDateTime localDateTime = LocalDateTime.now();

        List<Task> tasks = taskRepository.fetchAllTaskBetweenDueDate(
                java.sql.Timestamp.valueOf(LocalDateTime.now()),
                java.sql.Timestamp.valueOf(LocalDateTime.now().plus(dueDays, ChronoUnit.DAYS))
        );
        if (Objects.isNull(tasks) || tasks.isEmpty()) {
            throw new TodoNotFoundException("No tasks found with given due days:" + dueDays);
        }
        return ((tasks != null && tasks.size() > 0) ? tasks : new ArrayList<Task>());
    }

    private List<TaskAssignmentResponse> prepareTaskAssignment(List<Task> tasks) {
        List<TaskAssignmentResponse> taskAssignmentList = new ArrayList<TaskAssignmentResponse>();
        for (Task task : tasks) {
            if (Objects.nonNull(task.getUserProfile())) {
                TaskAssignmentResponse assignmentResponse = TaskAssignmentResponse.builder()
                        .taskId(task.getId())
                        .userId(task.getUserProfile().getId())
                        .userName(task.getUserProfile().getUserName())
                        .firstName(task.getUserProfile().getFirstName())
                        .lastName(task.getUserProfile().getLastName()).build();
                taskAssignmentList.add(assignmentResponse);
            }
        }
        return taskAssignmentList;
    }

}
