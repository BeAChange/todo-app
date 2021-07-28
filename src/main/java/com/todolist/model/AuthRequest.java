package com.todolist.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @ApiModelProperty(value = "User login")
    private String login;

    @ApiModelProperty(value = "User password")
    private String password;

}
