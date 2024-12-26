package cn.zc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class OjGatewayApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(OjGatewayApplication.class, args);
    }
}
