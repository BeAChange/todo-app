package com.todolist.controllers;

import com.todolist.dto.TaskDto;
import com.todolist.entities.Task;
import com.todolist.exception.TodoException;
import com.todolist.model.SlackMessageResponse;
import com.todolist.model.TaskAssignmentRequest;
import com.todolist.model.TaskAssignmentResponse;
import com.todolist.services.TaskAssignmentService;
import com.todolist.services.TaskService;
import com.todolist.services.impl.EmailService;
import com.todolist.services.impl.SlackService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/tasks")
public class TaskController {

    private TaskService taskService;
    private TaskAssignmentService taskAssignmentService;
    private EmailService emailService;
    private SlackService slackService;


    @Autowired
    public TaskController(TaskService taskService, TaskAssignmentService taskAssignmentService, EmailService emailService,
                          SlackService slackService) {
        this.taskService = taskService;
        this.taskAssignmentService = taskAssignmentService;
        this.emailService = emailService;
        this.slackService = slackService;
    }

    @ApiOperation(value = "Create New Task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createNewTask(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "Task DTO", value = "Object to create", required = true) @RequestBody @Valid TaskDto taskDto) {
        Task addedTask = taskService.createTask(taskDto);
        if (Objects.nonNull(addedTask)) {
            BeanUtils.copyProperties(addedTask, taskDto);
        }
        return taskDto;
    }

    @ApiOperation(value = "Get All Task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public List<Task> getAllTasks(@RequestHeader(name = "Authorization", required = false) String token) {
        return taskService.getAllTasks();
    }

    @ApiOperation(value = "Get Task By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public Task getByTaskId(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "id", value = "Task ID", required = true) @PathVariable("id") Long id) {
        return taskService.findById(id);
    }

    @ApiOperation(value = "Update Task By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PatchMapping(value = "/{id}")
    public Task updateTask(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "id", value = "Task ID", required = true) @PathVariable("id") Long id, @ApiParam(name = "Task DTO", value = "Object to update", required = true) @RequestBody @Valid TaskDto updatedTaskDto) {
        updatedTaskDto.setId(id);
        return taskService.updateTask(updatedTaskDto);
    }

    @ApiOperation(value = "Delete Task By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public TaskDto deleteTask(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "id", value = "Task ID", required = true) @PathVariable("id") Long id) {
        Task deleted = taskService.deleteById(id);
        return convertFromUserEntityToDto(deleted);
    }

    @ApiOperation(value = "Get Task By Status : NS | IP | CO ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(params = {"status"})
    public List<Task> getTaskByStatus(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "status", value = "Status Code", required = true) @RequestParam("status") String statusCode) {
        return taskService.findTasksByStatus(statusCode);
    }

    @ApiOperation(value = "Assign Task To Registered User ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(value = "/assign")
    public TaskAssignmentResponse assignTask(@RequestHeader(name = "Authorization", required = false) String token, @ApiParam(name = "Task Assignment DTO", value = "Object to create", required = true) @RequestBody @Valid TaskAssignmentRequest taskAssignmentRequest) {
        return taskService.assignTask(taskAssignmentRequest);
    }

    @ApiOperation(value = "Get All Assigned Task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/assignedTasks")
    public List<TaskAssignmentResponse> getAllAssignedTasks(@RequestHeader(name = "Authorization", required = false) String token) {
        return taskAssignmentService.getAllTasksWithAssignedUsers();
    }

    @ApiOperation(value = "Send Task Due Email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    //@GetMapping("/sendTaskDueEMail")
    public String sendTaskDueMails(@RequestHeader(name = "Authorization", required = false) String token, @RequestParam("emailTo") String emailTo, @RequestParam("emailSubject") String emailSubject, @RequestParam("noOfDays") String noOfDueDays) {
        /*
        String recipient = emailTo;
        String sub = emailSubject != null ? emailSubject : "Tasks Due Mail";
        try {
            List<Task> taskList = taskService.findAllTaskWithDueDate(Integer.parseInt(noOfDueDays));
            StringBuilder sb = new StringBuilder();
            taskList.forEach(task -> {
                sb.append(task.getId() + ":" + task.getDescription()+"\n");
            });
            String content = message + "\n" + sb.toString();
            emailService.sendEmail(recipient, sub, content);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new TodoException("Could not email. Reason:" + e.getMessage());
        }
        */
        return "WORK IN PROGRESS";
    }

    @ApiOperation(value = "Send Task Due Messages to Slack Channel")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/sendTaskDueMessage")
    public SlackMessageResponse sendDueMessage(@RequestHeader(name = "Authorization", required = false) String token, @RequestParam("channelId") String channelId, @RequestParam("message") String message, @RequestParam("noOfDays") String noOfDueDays) {
        try {
            List<Task> taskList = taskService.findAllTaskWithDueDate(Integer.parseInt(noOfDueDays));
            StringBuilder sb = new StringBuilder();
            taskList.forEach(task -> {
                sb.append(task.getId() + ":" + task.getDescription());
            });
            String content = message;//+ "" + sb.toString();
            return slackService.postMessage(channelId, content).get();
        } catch (Exception e) {
            throw new TodoException("Could not send message. Reason:" + e.getMessage());
        }
    }

    private TaskDto convertFromUserEntityToDto(Task task) {
        return TaskDto.builder().id(task.getId())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdDate(task.getCreatedDate())
                .build();
    }
}
