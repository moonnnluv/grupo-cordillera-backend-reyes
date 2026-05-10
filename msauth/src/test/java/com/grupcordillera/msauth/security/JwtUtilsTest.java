package com.grupcordillera.msauth.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    private static final String SECRET =
        "bXlTdXBlclNlY3JldEtleVBhcmFHcnVwb0NvcmRpbGxlcmEyMDI1RFNZMTEwNg==";
    private static final long EXPIRATION = 3600000L;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "expiration", EXPIRATION);
    }

    private UserDetails buildUser(String username) {
        return User.builder()
            .username(username)
            .password("irrelevant")
            .authorities(Collections.emptyList())
            .build();
    }

    @Test
    void generateToken_retornaTokenNoNulo() {
        String token = jwtUtils.generateToken(buildUser("testuser"));
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getUsernameFromToken_retornaUsernameDelToken() {
        UserDetails user = buildUser("ale.reyes");
        String token = jwtUtils.generateToken(user);
        assertEquals("ale.reyes", jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void generateTokenConRol_getRolFromToken_retornaRolCorrecto() {
        UserDetails user = buildUser("admin");
        String token = jwtUtils.generateToken(user, "ADMIN_GENERAL");
        assertEquals("ADMIN_GENERAL", jwtUtils.getRolFromToken(token));
    }

    @Test
    void getRolFromToken_tokenSinRol_retornaNull() {
        String token = jwtUtils.generateToken(buildUser("vendedor1"));
        assertNull(jwtUtils.getRolFromToken(token));
    }

    @Test
    void validateToken_tokenValido_retornaTrue() {
        String token = jwtUtils.generateToken(buildUser("testuser"));
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_tokenMalformado_retornaFalse() {
        assertFalse(jwtUtils.validateToken("token.invalido.malformado"));
    }

    @Test
    void validateToken_tokenModificado_retornaFalse() {
        String token = jwtUtils.generateToken(buildUser("testuser"));
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertFalse(jwtUtils.validateToken(tampered));
    }
}
