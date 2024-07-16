package com.yanchware.assesment;

import com.yanchware.assesment.user.AppUser;
import com.yanchware.assesment.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class AssesmentApplication {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(AssesmentApplication.class, args);
    }


    @Bean
    public CommandLineRunner initializeDb() {
        return (args) -> {
            AppUser user = userRepository.save(AppUser.builder()
                    .id(1L)
                    .username("test_user")
                    .apiSecretKey("$2a$12$6ICY4fR1sfq8GwcMjkdgZuEJRD14l3rAELCxtRUaTftkRJwBpAENW") // secret_key
                    .password("example_pass")
                    .awsAccessKey("aws_access_key")
                    .awsSecretKey("aws_secret_key").build());
            log.info("Initialized user: {}", user);
        };
    }

}
