package com.todolist.controllers;

import com.todolist.dto.UserProfileDto;
import com.todolist.entities.UserProfile;
import com.todolist.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserProfileController {

    private UserService userService;

    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Create New User Profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDto createNewUserProfile(@RequestHeader(name = "Authorization", required = false) String token, @RequestBody @Valid UserProfileDto userDto) {
        UserProfile addedUser = userService.createUser(userDto);
        UserProfileDto resUserDto = UserProfileDto.builder().build();
        BeanUtils.copyProperties(addedUser, resUserDto);
        return resUserDto;
    }

    @ApiOperation(value = "Get All User Profiles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public List<UserProfileDto> getAllUsers(@RequestHeader(name = "Authorization", required = false) String token) {
        List<UserProfileDto> allUsers = new ArrayList<UserProfileDto>();
        for (UserProfile user : userService.getAllUsers()) {
            UserProfileDto userDto = UserProfileDto.builder().build();
            BeanUtils.copyProperties(user, userDto);
            System.out.println("userDto:" + userDto);
            allUsers.add(userDto);
        }
        return allUsers;
    }

    @ApiOperation(value = "Get User profile by Id | Username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = "/{id}")
    public UserProfileDto getByIdOrName(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable("id") String idOrUserName) {
        UserProfile foundUser = userService.findUserByIdOrName(idOrUserName);
        UserProfileDto userDto = UserProfileDto.builder().build();
        BeanUtils.copyProperties(foundUser, userDto);
        return userDto;
    }

    @ApiOperation(value = "Update User Profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PatchMapping(value = "/{id}")
    public UserProfileDto updateUser(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable("id") Long id, @RequestBody @Valid UserProfileDto userRequest) {
        UserProfile updatedUser = userService.updateUser(id, userRequest);
        UserProfileDto userDto = UserProfileDto.builder().build();
        BeanUtils.copyProperties(updatedUser, userDto);
        return userDto;
    }

    @ApiOperation(value = "Delete User Profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping(value = "/{id}")
    public UserProfileDto deleteUser(@RequestHeader(name = "Authorization", required = false) String token, @PathVariable("id") Long id) {
        UserProfile deleted = userService.deleteUserById(id);
        return convertFromUserEntityToDto(deleted);
    }

    private UserProfileDto convertFromUserEntityToDto(UserProfile user) {
        return UserProfileDto.builder().id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();

    }
}
