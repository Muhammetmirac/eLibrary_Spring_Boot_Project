package com.runners.controller;

import com.runners.dto.request.LoginRequest;
import com.runners.dto.request.RegisterRequest;
import com.runners.dto.response.LibraryResponse;
import com.runners.dto.response.LoginResponse;
import com.runners.dto.response.ResponseMessage;
import com.runners.security.jwt.JwtUtils;
import com.runners.service.UserService;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {


    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;


    public UserJwtController(JwtUtils jwtUtils, UserService userService, @Lazy AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    //============= register ============
    @PostMapping("/register")
    public ResponseEntity<LibraryResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        userService.saveUser(registerRequest);
        LibraryResponse response = new LibraryResponse(ResponseMessage.USER_REGISTER_MESSAGE_RESPONSE, true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //============= login ============

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.jwtTokenGenerator(userDetails);

        LoginResponse loginResponse = new LoginResponse(jwtToken);
        return ResponseEntity.ok(loginResponse);
    }

}
