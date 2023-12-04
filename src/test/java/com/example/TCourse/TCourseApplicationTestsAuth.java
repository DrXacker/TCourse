package com.example.TCourse;

import com.example.TCourse.dto.TokenDto;
import com.example.TCourse.dto.UserDto;
import com.example.TCourse.service.AuthService;
import com.example.TCourse.service.UserService;
import com.example.TCourse.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class TCourseApplicationTestsAuth {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void registerUser_WhenValidUserDto_ReturnsOkResponse() {
        // Arrange
        UserDto userDto = new UserDto("mishauser", "password123", "misha", "user");

        // Act
        ResponseEntity<String> response = authService.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
        verify(userService).registerUser(userDto);
    }

    @Test
    public void authorizeUser_ValidCredentials_ReturnsTokenDto() throws Exception {
        // Arrange
        UserDto user = new UserDto("mishauser", "password123", "misha", "user");
        String token = "generated_token";
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        // Act
        ResponseEntity<TokenDto> response = authService.authorizeUser(user);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        verify(userService).loadUserByUsername(user.getUsername());
        verify(jwtTokenUtil).generateToken(userDetails);
    }

    @Test
    public void authorizeUser1_InvalidCredentials_ThrowsException() throws InstantiationError {
        // Arrange
        UserDto user = new UserDto("mishauser", "password123", "misha", "user");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(AuthenticationException.class);

        // Act and Assert
        assertThrows(InstantiationError.class, () -> authService.authorizeUser(user));
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        verifyZeroInteractions(userService, jwtTokenUtil);
    }

    @Test
    void authorizeUser_InvalidCredentials_ThrowsException() throws Exception {
        // Создаем заглушку для AuthenticationException
        AuthenticationException authenticationException = mock(AuthenticationException.class);

        // Устанавливаем поведение заглушки
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(authenticationException);

        // Проверяем, что исключение выбрасывается при вызове метода
        Exception exception = assertThrows(Exception.class, () -> authService.authorizeUser(new UserDto()));
        assertEquals("Invalid email/password", exception.getMessage());

        // Проверяем вызовы методов
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void getUser_WhenExistingUserId_ReturnsOkResponseWithUserDto() {
        // Arrange
        Long userId = 1L;
        UserDto userDto = new UserDto("mishauser", "password123", "misha", "user");

        when(userService.getUserById(userId)).thenReturn(Optional.of(userDto));

        // Act
        ResponseEntity<UserDto> response = authService.getUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService).getUserById(userId);
    }

    @Test
    public void getUser_WhenNonExistingUserId_ThrowsException() {
        // Arrange
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userService).getUserById(userId);
    }
}