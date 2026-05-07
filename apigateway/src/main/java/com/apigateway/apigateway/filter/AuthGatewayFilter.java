package com.apigateway.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.UUID;

@Component
public class AuthGatewayFilter extends OncePerRequestFilter {

    // Misma secret que ms-auth
    private static final String SECRET = 
        "bXlTdXBlclNlY3JldEtleVBhcmFHcnVwb0NvcmRpbGxlcmEyMDI1RFNZMTEwNg==";

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String correlationId = UUID.randomUUID().toString();

        // Agregar X-Correlation-Id a todas las requests
        request.setAttribute("X-Correlation-Id", correlationId);
        response.setHeader("X-Correlation-Id", correlationId);

        // Rutas públicas — no requieren JWT
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token requerido\"}");
            return;
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Agregar X-User-Id al header para que los microservicios lo reciban
            String username = claims.getSubject();
            String rol = (String) claims.get("rol");

            response.setHeader("X-User-Id", username);
            response.setHeader("X-User-Rol", rol != null ? rol : "");

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
        }
    }
}