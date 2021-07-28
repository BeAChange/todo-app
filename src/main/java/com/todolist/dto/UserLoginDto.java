package com.todolist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDto implements Serializable {
    private static final long serialVersionUID = -2491821272900553882L;

    @ApiModelProperty(value = "Id Field")
    private Long id;

    @ApiModelProperty(value = "Name Field")
    private String name;

    @ApiModelProperty(value = "Login Field")
    private String login;

    @ApiModelProperty(value = "Password Field")
    private String password;

}
