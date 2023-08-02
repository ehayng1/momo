package kr.co.dk.sample.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(value = {"kr.co.dk.sample.api"})
@SpringBootApplication
public class ApiApplication {

    //    public static final String APPLICATION_LOCATIONS = "spring.config.location=classpath:application.yml,classpath:aws.yml";
    public static final String APPLICATION_LOCATIONS = "spring.config.location=classpath:aws.yml";

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}
