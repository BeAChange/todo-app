package com.todolist.services;

import com.todolist.dto.TodoListDto;
import com.todolist.entities.Task;
import com.todolist.entities.TodoList;

import java.util.Map;
import java.util.Set;

public interface TodoListService {
    TodoList createTodoList(TodoListDto todoListDto);

    TodoList listAllTasks(Long todoListId);

    TodoList deleteTodoList(Long todoListId);

    Map<Long, Set<Task>> groupTasksByTodoList();
}
