package com.grupcordillera.msauth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptVerifyTest {

    @Test
    void verifyHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh9.";

        String[] candidates = {"Admin123!", "admin123!", "Admin123", "password", "admin"};
        for (String pwd : candidates) {
            System.out.println("\"" + pwd + "\" matches: " + encoder.matches(pwd, hash));
        }

        System.out.println("\nNew hash for Admin123!: " + encoder.encode("Admin123!"));
    }
}
