package com.khiemtran.springsecurity.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.TimeZone;

@SpringBootApplication
@Configuration
@EnableWebSecurity
@EnableJpaAuditing
@EntityScan(basePackages = "com.khiemtran.springsecurity.demo.model")
public class DemoApplication {
  @PostConstruct()
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }
}
