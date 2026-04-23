package com.example.blog.service;

import com.example.blog.dto.LoginRequest;
import com.example.blog.dto.LoginResponse;
import com.example.blog.dto.RegisterRequest;
import com.example.blog.entity.User;
import com.example.blog.exception.BusinessException;
import com.example.blog.mapper.UserMapper;
import com.example.blog.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_withNewUser_shouldSucceed() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("newuser");
        req.setPassword("password123");

        when(userMapper.selectByUsername("newuser")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        assertDoesNotThrow(() -> authService.register(req));
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void register_withExistingUser_shouldThrowException() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("existinguser");
        req.setPassword("password123");

        User existingUser = new User();
        existingUser.setUsername("existinguser");
        when(userMapper.selectByUsername("existinguser")).thenReturn(existingUser);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.register(req));
        assertEquals(400, exception.getCode());
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void login_withValidCredentials_shouldReturnToken() {
        LoginRequest req = new LoginRequest();
        req.setUsername("testuser");
        req.setPassword("password123");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQzBZN0UfGNEsKYGs5qC5V.5Zq2G");

        when(userMapper.selectByUsername("testuser")).thenReturn(user);
        when(jwtUtil.generate(1L, "testuser")).thenReturn("mocked.jwt.token");

        LoginResponse response = authService.login(req);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void login_withInvalidUsername_shouldThrowException() {
        LoginRequest req = new LoginRequest();
        req.setUsername("nonexistent");
        req.setPassword("password123");

        when(userMapper.selectByUsername("nonexistent")).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.login(req));
        assertEquals(401, exception.getCode());
        assertEquals("用户名或密码错误", exception.getMessage());
    }
}
