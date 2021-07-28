package com.todolist.services.impl;

import com.todolist.dto.TodoListDto;
import com.todolist.entities.Task;
import com.todolist.entities.TodoList;
import com.todolist.exception.TodoException;
import com.todolist.exception.TodoNotFoundException;
import com.todolist.repositories.TodoListRepository;
import com.todolist.services.TodoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TodoListServiceImpl implements TodoListService {

    @Autowired
    private TodoListRepository todoListRepository;

    @Override
    @Transactional
    public TodoList createTodoList(TodoListDto todoListDto) {
        TodoList todoList = new TodoList();
        BeanUtils.copyProperties(todoListDto, todoList);
        todoList.setCreatedDate(new Date());
        if (Objects.nonNull(todoList.getTasks())) {
            List<Task> tasks = todoList.getTasks().stream()
                    .map(this::setCreatedDate)
                    .collect(Collectors.toList());
            todoList.setTasks(new HashSet<Task>(tasks));
            todoList.setActive(true);
        }
        return todoListRepository.save(todoList);
    }


    private Task setCreatedDate(Task task) {
        task.setCreatedDate(new Date());
        return task;
    }


    @Override
    @Transactional(readOnly = true)
    public TodoList listAllTasks(Long todoListId) {
        TodoList todoList = null;
        Optional<TodoList> optionalTodoList = todoListRepository.findByTodoListId(todoListId);
        if (optionalTodoList.isPresent())
            todoList = optionalTodoList.get();
        return todoList;
    }

    @Override
    @Transactional(rollbackFor = {TodoNotFoundException.class})
    public TodoList deleteTodoList(Long id) throws TodoNotFoundException {
        TodoList deleted = null;
        Optional<TodoList> optionalTodoList = todoListRepository.findByTodoListId(id);
        if (optionalTodoList.isPresent())
            deleted = optionalTodoList.get();
        log.debug("Found the todo list entry: {}", deleted);

        if (Objects.isNull(deleted)) {
            throw new TodoNotFoundException("No record found with given id:" + id);
        }

        try {
            todoListRepository.softDelete(deleted.getId());
        } catch (Exception exp) {
            throw new TodoException("Could not able to remove the todo list. Reason:" + exp.getMessage());
        }
        return deleted;
    }




    @Override
    @Transactional(readOnly = true)
    public Map<Long, Set<Task>> groupTasksByTodoList() {
        List<TodoList> allTodoLists = todoListRepository.findAll();
        Map<Long, Set<Task>> resultMap = new HashMap<>();
        for (TodoList item : allTodoLists) {
            resultMap.put(item.getId(), item.getTasks());
        }
        return resultMap;
    }

}
