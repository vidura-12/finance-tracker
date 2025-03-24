package com.example.controllers;


import com.example.finance_tracker.controllers.AuthController;
import com.example.finance_tracker.dto.AuthRequest;
import com.example.finance_tracker.dto.AuthResponse;
import com.example.finance_tracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks before each test
    }

    @Test
    void testRegisterUser_success() {
        // Arrange
        AuthRequest request = new AuthRequest("testuser", "testpassword");
        String expectedResponse = "User registered successfully";

        when(authService.registerUser(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<String> responseEntity = authController.registerUser(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void testLoginUser_success() {
        // Arrange
        AuthRequest request = new AuthRequest("testuser", "testpassword");
        AuthResponse expectedResponse = new AuthResponse("dummy-jwt-token");

        when(authService.loginUser(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResponse> responseEntity = authController.loginUser(request);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
}
