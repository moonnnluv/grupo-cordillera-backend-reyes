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

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret",
                "bXlTdXBlclNlY3JldEtleVBhcmFHcnVwb0NvcmRpbGxlcmEyMDI1RFNZMTEwNg==");
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);
    }

    private UserDetails user(String username) {
        return User.withUsername(username).password("pass")
                   .authorities(Collections.emptyList()).build();
    }

    @Test
    void generateToken_retornaTokenConTresParts() {
        String token = jwtUtils.generateToken(user("admin"));
        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void getUsernameFromToken_retornaNombreCorrectamente() {
        String token = jwtUtils.generateToken(user("vendedor1"));
        assertEquals("vendedor1", jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void validateToken_tokenValido_retornaTrue() {
        String token = jwtUtils.generateToken(user("admin"));
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void validateToken_tokenInvalido_retornaFalse() {
        assertFalse(jwtUtils.validateToken("no.es.untoken"));
    }

    @Test
    void generateToken_conRol_getRolFromTokenRetornaRolCorrecto() {
        String token = jwtUtils.generateToken(user("jefa"), "ADMIN_SUCURSAL");
        assertEquals("ADMIN_SUCURSAL", jwtUtils.getRolFromToken(token));
    }

    @Test
    void generateToken_conSucursal_getSucursalFromTokenRetornaValorCorrecto() {
        String token = jwtUtils.generateToken(user("jefa"), "ADMIN_SUCURSAL", "SANTIAGO");
        assertEquals("SANTIAGO", jwtUtils.getSucursalFromToken(token));
    }
}
