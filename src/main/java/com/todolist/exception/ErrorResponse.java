package com.todolist.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private boolean success;
    private String message;
    private List<String> errors = new ArrayList<String>();

    public void addFieldError(String path, String message) {
        errors.add(path + ":" + message);
    }
}
