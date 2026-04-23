package com.example.blog.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET = "blog-jwt-secret-key-min-256-bits-for-hs256";
    private static final long EXPIRATION_MS = 86400000;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET, EXPIRATION_MS);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        Long userId = 1L;
        String username = "testuser";

        String token = jwtUtil.generate(userId, username);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void getUserId_shouldReturnCorrectUserId() {
        Long userId = 1L;
        String username = "testuser";
        String token = jwtUtil.generate(userId, username);

        Long extractedUserId = jwtUtil.getUserId(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    void getUsername_shouldReturnCorrectUsername() {
        Long userId = 1L;
        String username = "testuser";
        String token = jwtUtil.generate(userId, username);

        String extractedUsername = jwtUtil.getUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void getUserId_withInvalidToken_shouldReturnNull() {
        Long userId = jwtUtil.getUserId("invalid.token.here");

        assertNull(userId);
    }

    @Test
    void getUsername_withInvalidToken_shouldReturnNull() {
        String username = jwtUtil.getUsername("invalid.token.here");

        assertNull(username);
    }

    @Test
    void generateToken_withDifferentUsers_shouldCreateDifferentTokens() {
        String token1 = jwtUtil.generate(1L, "user1");
        String token2 = jwtUtil.generate(2L, "user2");

        assertNotEquals(token1, token2);
    }
}
