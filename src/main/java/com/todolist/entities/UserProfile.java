package com.todolist.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "USER_PROFILE", schema = "TESTDB")
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL", nullable = true)
    private String email;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @JsonIgnore
    @OneToMany
    @JoinTable(name = "TASK_ASSIGNMENT", schema = "TESTDB",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")})
    private Set<Task> assignedTasks;

    public static Builder getBuilder(String userName) {
        return new Builder(userName);
    }

    public static class Builder {

        private UserProfile built;

        public Builder(String userName) {
            built = new UserProfile();
            built.userName = userName;
        }

        public UserProfile build() {
            return built;
        }

        public Builder firstName(String firstName) {
            built.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            built.middleName = middleName;
            return this;
        }

        public Builder lastName(String lastName) {
            built.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            built.email = email;
            return this;
        }

        public Builder roleId(String roleId) {
            built.roleId = roleId;
            return this;
        }

        public Builder createdDate(Date createdDate) {
            built.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(Date modifiedDate) {
            built.modifiedDate = modifiedDate;
            return this;
        }

        public Builder active(boolean active) {
            built.active = active;
            return this;
        }
    }
}
