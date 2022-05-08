package com.jobby.jobs.config;

import com.jobby.jobs.model.User;
import com.jobby.jobs.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            User user = new User(
                    "Zi-long QIU",
                    "zilong.qiu@gmail.com"
            );

            User user2 = new User(
                    "Ding Meng",
                    "ding.meng@gmail.com"
            );

            repository.saveAll(List.of(
                    user,
                    user2
            ));
        };
    }
}
