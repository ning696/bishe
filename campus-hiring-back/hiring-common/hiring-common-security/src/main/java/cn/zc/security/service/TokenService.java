package cn.zc.security.service;

import cn.hutool.core.lang.UUID;
import cn.zc.common.core.constants.CacheConstants;
import cn.zc.common.core.constants.JwtConstants;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.util.JwtUtils;
import cn.zc.redis.service.RedisService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.zc.common.core.util.JwtUtils.getUserKey;

/**
 * Token 服务类
 * 负责 Token 的创建、解析、续期等功能
 * 
 * @author campus-hiring-system
 */
@Service
@Slf4j
public class TokenService {

    @Autowired
    private RedisService redisService;

    /**
     * 创建 Token
     * 
     * @param userId   用户ID
     * @param secret   JWT密钥
     * @param identity 用户身份（1:学生, 2:企业, 3:管理员）
     * @param nickName 用户昵称
     * @param headImage 用户头像URL
     * @return Token字符串
     */
    public String createToken(Long userId, String secret, Integer identity, String nickName, String headImage) {
        Map<String, Object> claims = new HashMap<>();
        String userKey = UUID.fastUUID().toString();
        claims.put(JwtConstants.LOGIN_USER_ID, userId);
        claims.put(JwtConstants.LOGIN_USER_KEY, userKey);
        String token = JwtUtils.createToken(claims, secret);

        // 将登录用户信息存储到 Redis
        String tokenKey = getTokenKey(userKey);
        LoginUser loginUser = new LoginUser();
        loginUser.setIdentity(identity);
        loginUser.setNickName(nickName);
        loginUser.setHeadImage(headImage);
        redisService.setCacheObject(tokenKey, loginUser, CacheConstants.EXP, TimeUnit.MINUTES);

        return token;
    }

    /**
     * 延长 Token 的有效时间
     * 当剩余时间小于阈值时，自动延长 Token 有效期
     * 
     * @param claims JWT Claims
     */
    public void extendToken(Claims claims) {
        String userKey = getUserKey(claims);
        if (userKey == null) {
            return;
        }
        String tokenKey = getTokenKey(userKey);

        // 获取剩余有效时间
        Long expire = redisService.getExpire(tokenKey, TimeUnit.MINUTES);
        if (expire != null && expire < CacheConstants.REFRESH_TIME) {
            // 延长 Token 有效期
            redisService.expire(tokenKey, CacheConstants.EXP, TimeUnit.MINUTES);
        }
    }

    /**
     * 获取登录用户信息
     * 
     * @param token  Token字符串
     * @param secret JWT密钥
     * @return 登录用户信息
     */
    public LoginUser getLoginUser(String token, String secret) {
        String userKey = getUserKey(token, secret);
        if (userKey == null) {
            return null;
        }
        return redisService.getCacheObject(getTokenKey(userKey), LoginUser.class);
    }

    /**
     * 删除登录用户信息
     * 
     * @param token  Token字符串
     * @param secret JWT密钥
     * @return true=删除成功，false=删除失败
     */
    public boolean deleteLoginUser(String token, String secret) {
        String userKey = getUserKey(token, secret);
        if (userKey == null) {
            return false;
        }
        return redisService.deleteObject(getTokenKey(userKey));
    }

    /**
     * 从 Claims 中获取用户ID
     * 
     * @param claims JWT Claims
     * @return 用户ID
     */
    public Long getUserId(Claims claims) {
        if (claims == null) {
            return null;
        }
        return Long.valueOf(JwtUtils.getUserId(claims));
    }

    /**
     * 从 Claims 中获取用户Key
     * 
     * @param claims JWT Claims
     * @return 用户Key
     */
    public String getUserKey(Claims claims) {
        if (claims == null) {
            return null;
        }
        return JwtUtils.getUserKey(claims);
    }

    /**
     * 从 Token 中获取用户Key
     * 
     * @param token  Token字符串
     * @param secret JWT密钥
     * @return 用户Key
     */
    private String getUserKey(String token, String secret) {
        Claims claims = getClaims(token, secret);
        if (claims == null) {
            return null;
        }
        return JwtUtils.getUserKey(claims);
    }

    /**
     * 解析 Token 获取 Claims
     * 
     * @param token  Token字符串
     * @param secret JWT密钥
     * @return JWT Claims
     */
    public Claims getClaims(String token, String secret) {
        Claims claims;
        try {
            claims = JwtUtils.parseToken(token, secret);
            if (claims == null) {
                log.error("解析token：{}, 出现异常", token);
                return null;
            }
        } catch (Exception e) {
            log.error("解析token：{}, 出现异常", token, e);
            return null;
        }
        return claims;
    }

    /**
     * 刷新登录用户信息
     * 
     * @param nickName 用户昵称
     * @param headImage 用户头像URL
     * @param userKey 用户Key
     */
    public void refreshLoginUser(String nickName, String headImage, String userKey) {
        String tokenKey = getTokenKey(userKey);
        LoginUser loginUser = redisService.getCacheObject(tokenKey, LoginUser.class);
        if (loginUser != null) {
            loginUser.setNickName(nickName);
            loginUser.setHeadImage(headImage);
            redisService.setCacheObject(tokenKey, loginUser);
        }
    }

    /**
     * 获取 Token 在 Redis 中的 key
     * 
     * @param userKey 用户Key
     * @return Redis key
     */
    private String getTokenKey(String userKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + userKey;
    }
}

















