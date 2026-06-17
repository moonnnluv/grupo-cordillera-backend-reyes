package com.grupcordillera.msauth.service;

import com.grupcordillera.msauth.entity.Usuario;
import com.grupcordillera.msauth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() { MockitoAnnotations.openMocks(this); }

    @Test
    void loadUserByUsername_usuarioExistente_retornaUserDetails() {
        Usuario u = new Usuario();
        u.setUsername("admin");
        u.setPassword("$2a$10$hash");
        u.setEnabled(true);
        u.setRol("ADMIN_GENERAL");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(u));

        UserDetails result = service.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        verify(usuarioRepository).findByUsername("admin");
    }

    @Test
    void loadUserByUsername_usuarioNoExistente_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("fantasma")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("fantasma"));
    }
}
