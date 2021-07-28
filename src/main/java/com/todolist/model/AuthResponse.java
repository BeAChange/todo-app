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
public class AuthResponse {

    @ApiModelProperty(value = "User ID")
    private Long id;

    @ApiModelProperty(value = "User name")
    private String name;

    @ApiModelProperty(value = "User login")
    private String login;
}
