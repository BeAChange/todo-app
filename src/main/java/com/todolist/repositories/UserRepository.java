package com.todolist.repositories;

import com.todolist.dto.UserLoginDto;
import com.todolist.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.todolist.dto.UserLoginDto(u.id, u.name, u.login, u.password) "
            + "FROM User u "
            + "WHERE u.login = :login")
    UserLoginDto findByLogin(@Param("login") String login);

}
