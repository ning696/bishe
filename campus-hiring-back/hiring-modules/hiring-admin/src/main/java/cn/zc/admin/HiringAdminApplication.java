package cn.zc.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.zc"})
@MapperScan("cn.zc.admin.mapper")
public class HiringAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiringAdminApplication.class, args);
    }

}

