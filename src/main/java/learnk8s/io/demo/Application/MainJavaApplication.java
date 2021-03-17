package learnk8s.io.demo.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EntityScan
public class MainJavaApplication {
  public static void main(String[] args) {
    SpringApplication.run(MainJavaApplication.class, args);
  }
}


