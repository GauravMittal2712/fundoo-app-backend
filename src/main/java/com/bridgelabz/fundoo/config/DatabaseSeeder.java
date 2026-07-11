package com.bridgelabz.fundoo.config;

import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.entity.enums.Role;
import com.bridgelabz.fundoo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@org.springframework.context.annotation.Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@fundoo.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            log.info("No admin user found. Seeding default admin user...");
            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("AdminPassword@123"))
                    .phoneNumber("9999999999")
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .verified(true)
                    .deleted(false)
                    .build();
            userRepository.save(admin);
            log.info("Default admin user successfully seeded: {}", adminEmail);
        } else {
            log.debug("Admin user already exists. Skipping seeding.");
        }
    }
}
