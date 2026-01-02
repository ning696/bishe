package cn.zc.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.zc"})
public class HiringStudentApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiringStudentApplication.class, args);
    }

}

