package com.todolist.repositories;


import com.todolist.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select o from Task o where o.active=true")
    List<Task> findAll();

    @Query("select o from Task o where o.active=true and o.id = :taskId")
    Task fetchByTaskId(@Param("taskId") Long taskId);

    @Query("select o from Task o where o.active=true and o.status= :status")
    List<Task> findByStatus(@Param("status") String status);

    @Query("select o from Task o where o.active=true and o.dueDate BETWEEN :from AND :to")
    List<Task> fetchAllTaskBetweenDueDate(@Param("from") java.sql.Timestamp from, @Param("to") java.sql.Timestamp to);

    @Query("update Task o set o.active=false where o.id = :id")
    @Modifying
    void softDelete(@Param("id") Long id);
}
