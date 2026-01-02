package cn.zc.security.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.zc.common.core.constants.Constants;
import cn.zc.common.core.constants.HttpConstants;
import cn.zc.common.core.util.ThreadLocalUtil;
import cn.zc.security.service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Token 拦截器
 * 从请求头中提取 Token，解析后存储到 ThreadLocal，并延长 Token 有效期
 * 
 * @author campus-hiring-system
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            return true;
        }
        Claims claims = tokenService.getClaims(token, secret);
        if (claims != null) {
            Long userId = tokenService.getUserId(claims);
            String userKey = tokenService.getUserKey(claims);
            ThreadLocalUtil.set(Constants.USER_ID, userId);
            ThreadLocalUtil.set(Constants.USER_KEY, userKey);
            tokenService.extendToken(claims);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        ThreadLocalUtil.remove();
    }

    /**
     * 从请求头中提取 Token
     * 
     * @param request HTTP请求
     * @return Token字符串（去除 Bearer 前缀）
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(HttpConstants.AUTHENTICATION);
        if (StrUtil.isNotEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            token = token.replaceFirst(HttpConstants.PREFIX, "");
        }
        return token;
    }
}
















