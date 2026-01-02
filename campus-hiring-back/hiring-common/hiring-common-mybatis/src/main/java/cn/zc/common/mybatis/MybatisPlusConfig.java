package cn.zc.common.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类
 * 注册元数据处理器
 * 
 * @author campus-hiring-system
 */
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MyMetaObjectHandler myMetaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}

















