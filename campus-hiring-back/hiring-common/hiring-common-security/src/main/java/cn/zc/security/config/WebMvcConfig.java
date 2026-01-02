package cn.zc.security.config;

import cn.zc.security.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 * 注册 Token 拦截器
 * 
 * @author campus-hiring-system
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                // 排除登录和注册路径
                .excludePathPatterns(
                        "/admin/login",
                        "/admin/register",
                        "/student/login",
                        "/student/register",
                        "/enterprise/login",
                        "/enterprise/register",
                        "/doc.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                )
                // 拦截所有其他路径
                .addPathPatterns("/**");
    }
}

















