package com.identity.configuration;

import com.identity.entity.Role;
import com.identity.entity.User;
import com.identity.repo.RoleRepo;
import com.identity.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import static com.identity.constant.Constants.PreDefineRole.ROLE_ADMIN;
import static com.identity.constant.Constants.PreDefineRole.ROLE_USER;
import static com.identity.constant.Constants.ADMIN_ACCOUNT.ADMIN_PASSWORD;
import static com.identity.constant.Constants.ADMIN_ACCOUNT.ADMIN_USERNAME;

@Slf4j
@Configuration
public class InitialApplicationConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    )
    ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo) {
        log.info("Initializing application with custom configuration.....");
        return args -> {
            if (userRepo.findByUsernameAndActiveTrue(ADMIN_USERNAME).isEmpty()) {

                roleRepo.save(Role.builder()
                        .name(ROLE_USER)
                        .description("Default user role")
                        .build());

                Role adminRole = roleRepo.save(Role.builder()
                        .name(ROLE_ADMIN)
                        .description("Default admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USERNAME)
                        .password(passwordEncoder().encode(ADMIN_PASSWORD))
                        .email("admin@admin.com")
                        .active(true)
                        .roles(roles)
                        .build();

                userRepo.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
