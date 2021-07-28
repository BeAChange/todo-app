package com.todolist.repositories;

import com.todolist.entities.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    @Query("select o from TodoList o where o.active=true")
    List<TodoList> findAll();

    @Query("select o from TodoList o where o.active=true and o.id = :id")
    Optional<TodoList> findByTodoListId(@Param("id") Long id);

    @Query("update TodoList o set o.active=false where o.id = :id")
    @Modifying
    void softDelete(@Param("id") Long id);
}
