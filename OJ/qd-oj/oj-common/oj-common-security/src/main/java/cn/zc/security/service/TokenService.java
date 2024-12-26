package cn.zc.security.service;

import cn.hutool.core.lang.UUID;
import cn.zc.common.core.constants.CacheConstants;
import cn.zc.common.core.constants.JwtConstants;
import cn.zc.redis.service.RedisService;
import cn.zc.common.core.domain.LoginUser;
import cn.zc.common.core.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.zc.common.core.util.JwtUtils.getUserKey;

@Slf4j
public class TokenService {
    @Autowired
    private RedisService redisService;
    public  String createToken(String userId, String secret,int identity,String nickName) {
        Map<String, Object> hashMap = new HashMap<>();
        String userKey = UUID.fastUUID().toString();
        hashMap.put(JwtConstants.LOGIN_USER_ID,userId);
        hashMap.put(JwtConstants.LOGIN_USER_Key, userKey);
        String token = JwtUtils.createToken(hashMap, secret);
        //第三方机制中存放敏感信息
        String key = CacheConstants.LOGIN_TOKEN_KEY + userKey;
        LoginUser loginUser = new LoginUser();
        //1为普通用户，2为管理员
        loginUser.setIdentity(identity);
        loginUser.setNickName(nickName);
        redisService.setCacheObject(key, loginUser, CacheConstants.EXP, TimeUnit.MINUTES);
        return token;
    }
    //延长token的有效时间，就是延长redis当中从存储的用于用户身份认证的敏感信息的有效时间    操作redis  token  --》 唯一标识

    //在身份认证通过之后才会调用的，并且在请求到达controller层之前  在拦截器中调用
    //延长token的有效时间，就是延长redis当中从存储的用于用户身份认证的敏感信息的有效时间    操作redis  token  --》 唯一标识
    public void extendToken(String token, String secret) {
//        Claims claims;
//        try {
//            claims = JwtUtils.parseToken(token, secret); //获取令牌中信息  解析payload中信息  存储着用户唯一标识信息
//            if (claims == null) {
//                log.error("解析token：{}, 出现异常", token);
//                return;
//            }
//        } catch (Exception e) {
//            log.error("解析token：{}, 出现异常", token, e);
//            return;
//        }
//        String userKey = JwtUtils.getUserKey(claims);  //获取jwt中的key
        String userKey = getUserKey(token, secret);
        if (userKey == null) {
            return;
        }
        String tokenKey = getTokenKey(userKey);

        //720min  12个小时      剩余  180min 时候对它进行延长
        Long expire = redisService.getExpire(tokenKey, TimeUnit.MINUTES);
        if (expire != null && expire < CacheConstants.REFRESH_TIME) {
            redisService.expire(tokenKey, CacheConstants.EXP, TimeUnit.MINUTES);
        }
    }
    private String getUserKey(String token, String secret){
                Claims claims;
        try {
            claims = JwtUtils.parseToken(token, secret); //获取令牌中信息  解析payload中信息  存储着用户唯一标识信息
            if (claims == null) {
                log.error("解析token：{}, 出现异常", token);
                return null;
            }
        } catch (Exception e) {
            log.error("解析token：{}, 出现异常", token, e);
            return null;
        }
        return JwtUtils.getUserKey(claims);  //获取jwt中的key
    }
    private String getTokenKey(String userKey) {
        return CacheConstants.LOGIN_TOKEN_KEY + userKey;
    }
    //延长token有效时间
//    public void refreshToken(String token, String secret) {
//        Claims claims;
//        try {
//            claims = JwtUtils.parseToken(token, secret); //获取令牌中信息 解析payload中信息
//            if (claims == null) {
//                //由于在过滤器中已经做了判断这里不做处理，如果出错仅作日志记录
//                log.error("拦截器中解析token失败，claims为空");
//                return;
//            }
//        }
//        catch (Exception e) {
//            //由于在过滤器中已经做了判断这里不做处理，如果出错仅作日志记录
//            log.error("拦截器中解析token失败", e);
//            return;
//        }
//        String userKey = getUserKey(token, secret);
//        String key = CacheConstants.LOGIN_TOKEN_KEY + userKey;
//        //获得剩余有效时间
//        Long expire = redisService.getExpire(key, TimeUnit.MINUTES);
//        //刷新token有效时间
//        if(expire!=null&&expire<CacheConstants.REFRESH_TIME){
//            redisService.expire(key, CacheConstants.EXP, TimeUnit.MINUTES);
//        }
//    }

    public LoginUser getLoginUser(String token,String secret) {
        String userKey = getUserKey(token, secret);
        return redisService.getCacheObject(getTokenKey(userKey), LoginUser.class);
    }
}
