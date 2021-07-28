package com.todolist.repositories;

import com.todolist.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("select o from UserProfile o where o.active=true")
    List<UserProfile> findAll();

    @Query("select o from UserProfile o where o.active=true and o.userName = :userName")
    UserProfile findByUserName(@Param("userName") String userName);

    @Query("select o from UserProfile o where o.active=true and o.id = :id")
    Optional<UserProfile> findByUserId(@Param("id") Long id);

    @Query("update UserProfile o set o.active=false where o.id = :id")
    @Modifying
    void softDelete(@Param("id") Long id);
}
