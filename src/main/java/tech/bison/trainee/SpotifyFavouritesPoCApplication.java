package tech.bison.trainee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpotifyFavouritesPoCApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyFavouritesPoCApplication.class, args);
    }
}
