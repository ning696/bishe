package cn.zc.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger 配置类
 * 配置 OpenAPI 文档信息
 * 
 * @author campus-hiring-system
 */
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("校园招聘系统")
                        .description("校园招聘系统接口文档")
                        .version("v1"));
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保 Swagger UI 静态资源可以正确加载
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/");
    }
}