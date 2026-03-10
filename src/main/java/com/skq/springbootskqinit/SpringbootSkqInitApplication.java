package com.skq.springbootskqinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.skq.springbootskqinit.mapper")
@SpringBootApplication
// 代码里用到了@Async注解实现异步方法调用，启动类必须添加@EnableAsync注解，否则@Async注解会完全失效
@EnableAsync
public class SpringbootSkqInitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSkqInitApplication.class, args);
    }

}
