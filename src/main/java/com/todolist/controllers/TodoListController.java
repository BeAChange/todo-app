package com.todolist.controllers;

import com.todolist.dto.TodoListDto;
import com.todolist.entities.Task;
import com.todolist.entities.TodoList;
import com.todolist.services.TodoListService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/todoLists")
public class TodoListController {

    private TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @ApiOperation(value = "Create New Todo List")
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
    public TodoListDto createTodoList(@RequestHeader(name = "Authorization", required = false) String token, @RequestBody @Valid TodoListDto todoListDto) {
        TodoList addedTodoList = todoListService.createTodoList(todoListDto);
        TodoListDto resTodoListDto = TodoListDto.builder().build();
        BeanUtils.copyProperties(addedTodoList, resTodoListDto);
        return resTodoListDto;
    }

    @ApiOperation(value = "Get Todo List By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/{todoListId}")
    public TodoListDto getAllTasksForGivenTodoList(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable("todoListId") Long todoListId) {
        TodoList foundTodoList = todoListService.listAllTasks(todoListId);
        TodoListDto resTodoListDto = new TodoListDto();
        BeanUtils.copyProperties(foundTodoList, resTodoListDto);
        return resTodoListDto;
    }

    @ApiOperation(value = "Delete Todo List By Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{todoListId}")
    public void deleteTask(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable("todoListId") Long todoListId) {
        todoListService.deleteTodoList(todoListId);
    }

    @ApiOperation(value = "Group By Todo list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/tasksGroupByTodoList")
    public Map<Long, Set<Task>> tasksGroupByTodoList(@RequestHeader(name = "Authorization", required = false) String token) {
        Map<Long, Set<Task>> tasksGroupByTodoListId = todoListService.groupTasksByTodoList();
        return tasksGroupByTodoListId;
    }
}
