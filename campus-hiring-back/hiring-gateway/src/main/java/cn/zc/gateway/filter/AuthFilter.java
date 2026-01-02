package cn.zc.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.zc.common.core.constants.CacheConstants;
import cn.zc.common.core.constants.HttpConstants;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.common.core.enums.UserIdentity;
import cn.zc.common.core.util.JwtUtils;
import cn.zc.gateway.properties.IgnoreWhiteProperties;
import cn.zc.redis.service.RedisService;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 网关全局认证过滤器
 * 实现身份认证和权限验证
 * 
 * @author campus-hiring-system
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private IgnoreWhiteProperties ignoreWhite;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private RedisService redisService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();

        // 跳过不需要验证的路径（白名单）
        if (matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }

        // 从请求头中获取 Token
        String token = getToken(request);
        if (StrUtil.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }

        // 解析 Token
        Claims claims;
        try {
            claims = JwtUtils.parseToken(token, secret);
            if (claims == null) {
                return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
            }
        } catch (Exception e) {
            log.error("解析token失败", e);
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }

        // 验证 Redis 中的登录状态
        String userKey = JwtUtils.getUserKey(claims);
        boolean isLogin = redisService.hasKey(getTokenKey(userKey));
        if (!isLogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }

        // 验证用户ID
        String userId = JwtUtils.getUserId(claims);
        if (StrUtil.isEmpty(userId)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 获取登录用户信息
        LoginUser user = redisService.getCacheObject(getTokenKey(userKey), LoginUser.class);
        if (user == null) {
            return unauthorizedResponse(exchange, "用户信息不存在");
        }

        // 根据 URL 前缀验证用户身份权限
        if (url.startsWith("/" + HttpConstants.ADMIN_URL_PREFIX + "/")) {
            // 管理员端接口，只有管理员可以访问
            if (!UserIdentity.ADMIN.getValue().equals(user.getIdentity())) {
                return unauthorizedResponse(exchange, "无权限访问管理员接口");
            }
        } else if (url.startsWith("/" + HttpConstants.STUDENT_URL_PREFIX + "/")) {
            // 学生端接口，只有学生可以访问
            if (!UserIdentity.STUDENT.getValue().equals(user.getIdentity())) {
                return unauthorizedResponse(exchange, "无权限访问学生接口");
            }
        } else if (url.startsWith("/" + HttpConstants.ENTERPRISE_URL_PREFIX + "/")) {
            // 企业端接口，只有企业可以访问
            if (!UserIdentity.ENTERPRISE.getValue().equals(user.getIdentity())) {
                return unauthorizedResponse(exchange, "无权限访问企业接口");
            }
        }

        return chain.filter(exchange);
    }

    /**
     * 查找指定url是否匹配指定匹配规则链表中的任意一个字符串
     *
     * @param url         指定url
     * @param patternList 需要检查的匹配规则链表
     * @return 是否匹配
     */
    private boolean matches(String url, List<String> patternList) {
        if (StrUtil.isEmpty(url) || CollectionUtils.isEmpty(patternList)) {
            return false;
        }
        for (String pattern : patternList) {
            if (isMatch(pattern, url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则匹配
     * 匹配规则中：
     * ? 表示单个任意字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return 是否匹配
     */
    private boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String userKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + userKey;
    }

    /**
     * 从请求头中获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(HttpConstants.AUTHENTICATION);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StrUtil.isNotEmpty(token) && token.startsWith(HttpConstants.PREFIX)) {
            token = token.replaceFirst(HttpConstants.PREFIX, StrUtil.EMPTY);
        }
        return token;
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{}", exchange.getRequest().getPath());
        return webFluxResponseWriter(exchange.getResponse(), msg, ResultCode.FAILED_UNAUTHORIZED.getCode());
    }

    /**
     * 拼装webflux模型响应
     */
    private Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String msg, int code) {
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        R<?> result = R.fail(code, msg);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSON.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return -200;  // 值越小，过滤器就越先被执行
    }
}











