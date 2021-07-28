package com.todolist.controllers;


import com.todolist.model.AuthRequest;
import com.todolist.model.AuthResponse;
import com.todolist.security.AppUserDetails;
import com.todolist.security.AppUserDetailsService;
import com.todolist.security.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/login")
public class AuthController {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "Generate JWT Token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = AuthResponse.class),
            @ApiResponse(code = 204, message = "No records found"),
            @ApiResponse(code = 401, message = "Access denied"),
            @ApiResponse(code = 403, message = "You doesn't have permission"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) {
        AuthResponse retorno = null;
        HttpHeaders responseHeaders = new HttpHeaders();

        try {
            validateRequest(request);
            authenticate(request.getLogin(), request.getPassword());

            final AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(request.getLogin());
            final String token = jwtTokenUtil.generateToken(userDetails);

            responseHeaders.set("Authorization", "Bearer " + token);

            if (userDetails == null || userDetails.getUser() == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Bad credentials");
            }

            retorno = new AuthResponse(userDetails.getUser().getId(), userDetails.getUser().getName(), userDetails.getUser().getLogin());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(retorno);
    }

    private void validateRequest(AuthRequest request) throws Exception {
        if (request.getLogin() == null || "".equals(request.getLogin().trim())) {
            throw new Exception("Login is required");
        }
        if (request.getLogin() == null || "".equals(request.getPassword().trim())) {
            throw new Exception("Password is required");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials");
        }
    }

}