package com.example.TCourse.service;


import com.example.TCourse.dto.TokenDto;
import com.example.TCourse.dto.UserDto;
import com.example.TCourse.exception.NotFoundException;
import com.example.TCourse.exception.UnauthorizedException;
import com.example.TCourse.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<TokenDto> authorizeUser(@RequestBody UserDto user) throws UnauthorizedException {
        try {
            // authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            // load user details
            UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            // generate JWT token
            String token = jwtTokenUtil.generateToken(userDetails);
            // return token
            return ResponseEntity.ok(new TokenDto(token));
        } catch (AuthenticationException e) {
            // handle authentication errors
            throw new UnauthorizedException("Invalid email/password");
        }
    }

    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }
}
