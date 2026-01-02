package cn.zc.common.core.util;

import cn.zc.common.core.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JwtUtils {
    /**
     * ⽣成令牌
     *
     * @param claims 数据
//     * @param secret 密钥
     * @return 令牌
     */
//    public static String createToken(Map<String, Object> claims,String secret)
//    {
//        // 使用 secret 创建密钥对象
//        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        return Jwts.builder()
//                .setClaims(claims)
//                .signWith(key, SignatureAlgorithm.HS256) // 使用固定密钥和算法签名
//                .compact();
//    }
    public static String createToken(Map<String, Object> claims, String secret)
    {
        String token =
                Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,
                        secret).compact();
        return token;
    }
    /**
     * 从令牌中获取数据
     *
//     * @param token 令牌
//     * @param secret 密钥
     * @return 数据
     */
    public static Claims parseToken(String token, String secret) {
        return
                Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
//    public static Claims parseToken(String token, String secret) {
//        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key) // 使用同一个密钥进行验证
//                .build()
//                .parseClaimsJws(token) // 解码和验证 JWT
//                .getBody(); // 获取有效载荷中的声明
//        return claims;
//    }

    public static String getUserKey(Claims claims) {
        return toStr(claims.get(JwtConstants.LOGIN_USER_KEY));
    }

    public static String getUserId(Claims claims) {
        return toStr(claims.get(JwtConstants.LOGIN_USER_ID));
    }

    private static String toStr(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }


//    public static String createToken(Map<String, Object> claims, String secret)
//    {
//        String token =
//                Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,
//                        secret).compact();
//        return token;
//    }
//    /**
//     * 从令牌中获取数据
//     *
//     * @param token 令牌
//     * @param secret 密钥
//     * @return 数据
//     */
//    public static Claims parseToken(String token, String secret) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
}
