package com.mindtrack.mind_track_api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.mindtrack.mind_track_api")
@EnableJpaRepositories(basePackages = "com.mindtrack.mind_track_api.repository")
public class MindTrackApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindTrackApiApplication.class, args);
    }
}