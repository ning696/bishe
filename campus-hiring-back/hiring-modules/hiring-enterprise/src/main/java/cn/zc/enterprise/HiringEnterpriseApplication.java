package cn.zc.enterprise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.zc"})
@EnableFeignClients(basePackages = {"cn.zc.api.feign"})
public class HiringEnterpriseApplication {

    public static void main(String[] args) {
        SpringApplication.run(HiringEnterpriseApplication.class, args);
    }

}

