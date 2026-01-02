package cn.zc.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动类
 *
 * @author zc
 */
@SpringBootApplication(
    exclude = {DataSourceAutoConfiguration.class},
    scanBasePackages = {"cn.zc.gateway", "cn.zc.common.core", "cn.zc.redis"}
)
public class HiringGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(HiringGatewayApplication.class, args);
    }
}

