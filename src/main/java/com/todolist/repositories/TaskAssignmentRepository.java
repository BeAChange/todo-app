package com.todolist.repositories;

import com.todolist.entities.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, TaskAssignmentKey> {


    @Modifying
    @Query("delete from TaskAssignment o where o.assignmentKey.taskId=?1")
    void deleteByTaskId(Long taskId);

    @Modifying
    @Query("delete from TaskAssignment o where o.assignmentKey.userId=?1")
    void deleteByUserId(Long userId);
}
