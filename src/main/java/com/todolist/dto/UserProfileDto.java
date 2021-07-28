package com.todolist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.todolist.util.Constants;
import com.todolist.util.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

import static com.todolist.util.Constants.REGEX_USER_NAME;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {

    private Long id;

    @NotNull
    @Pattern(regexp = REGEX_USER_NAME)
    @Length(min = Constants.MIN_LENGTH_USERNAME, max = Constants.MAX_LENGTH_USERNAME)
    private String userName;

    @Length(max = Constants.MAX_LENGTH_NAMEPART)
    private String firstName;

    @Length(max = Constants.MAX_LENGTH_NAMEPART)
    private String middleName;

    @Length(max = Constants.MAX_LENGTH_NAMEPART)
    private String lastName;

    @Length(max = Constants.MAX_LENGTH_EMAIL)
    @Email
    private String email;

    @Length(max = Constants.MAX_LENGTH_ROLEID)
    private String roleId;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date createdDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    private Date modifiedDate;
}
