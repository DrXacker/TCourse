package com.example.TCourse.controller;

import com.example.TCourse.dto.TokenDto;
import com.example.TCourse.dto.UserDto;
import com.example.TCourse.exception.UnauthorizedException;
import com.example.TCourse.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
        return authService.registerUser(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> authorizeUser(@RequestBody UserDto user) throws UnauthorizedException {
        return authService.authorizeUser(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return authService.getUser(id);
    }
}