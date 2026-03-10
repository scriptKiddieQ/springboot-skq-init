package com.skq.springbootskqinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.skq.springbootskqinit.mapper")
@SpringBootApplication
public class SpringbootSkqInitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSkqInitApplication.class, args);
    }

}
