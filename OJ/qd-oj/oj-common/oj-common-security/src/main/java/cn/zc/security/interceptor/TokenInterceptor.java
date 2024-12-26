package cn.zc.security.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.zc.common.core.constants.HttpConstants;
import cn.zc.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;
    @Value("${jwt.secret}")
    private String secret; //从哪个配置文件中调取就是哪个服务配置文件中的secret
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        tokenService.extendToken(token, secret);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    /**
     * 从请求头中获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token =
                request.getHeader(HttpConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) &&
                token.startsWith(HttpConstants.PREFIX)) {
            token = token.replaceFirst(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }
}
