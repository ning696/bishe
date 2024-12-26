package cn.zc.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("cn.zc.system.mapper")
public class OjSystemApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(OjSystemApplication.class, args);
    }
}
