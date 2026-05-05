package com.grupcordillera.msauth.controller;

import com.grupcordillera.msauth.dto.AuthResponse;
import com.grupcordillera.msauth.dto.LoginRequest;
import com.grupcordillera.msauth.dto.RegisterRequest;
import com.grupcordillera.msauth.entity.Usuario;
import com.grupcordillera.msauth.repository.UsuarioRepository;
import com.grupcordillera.msauth.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow();

        String token = jwtUtils.generateToken(userDetails, usuario.getRol());

        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol()
        );

        return ResponseEntity.ok(new AuthResponse(token, userInfo));
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El username ya está en uso"));
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El email ya está en uso"));
        }

        List<String> rolesValidos = List.of("ADMIN_GENERAL", "ADMIN_SUCURSAL", "VENDEDOR");
        String rol = (request.getRol() != null) ? request.getRol().toUpperCase() : "VENDEDOR";
        if (!rolesValidos.contains(rol)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Rol inválido. Use: ADMIN_GENERAL, ADMIN_SUCURSAL o VENDEDOR"));
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setRol(rol);
        usuario.setEnabled(true);

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Usuario registrado exitosamente"));
    }
}
