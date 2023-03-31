package com.gustavo.api.config;

import com.gustavo.api.domain.User;
import com.gustavo.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalConfig {

    private final UserRepository repository;

    @Bean
    public void startDB() {

        User u1 = new User(null, "Rafael", "grlrafael@outlook.com", "123");
        User u2 = new User(null, "Luiz", "luiz@outlook.com", "123");

        repository.saveAll(List.of(u1, u2));
    }
}
