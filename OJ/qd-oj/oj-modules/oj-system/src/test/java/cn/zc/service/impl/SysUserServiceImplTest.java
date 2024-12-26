package cn.zc.service.impl;

import cn.zc.common.core.domain.R;
import cn.zc.common.core.enums.ResultCode;
import cn.zc.redis.service.RedisService;
import cn.zc.common.core.util.JwtUtils;
import cn.zc.system.OjSystemApplication;
import cn.zc.system.domain.po.SysUser;
import cn.zc.system.mapper.SysUserMapper;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = OjSystemApplication.class)
class SysUserServiceImplTest {
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RedisService redisService;
@Test
    public void add() {
        int insert = sysUserMapper.insert(new SysUser("admintest", "123456"));
        R<Void> r = new R<>();
        if (insert > 0){
            r.setCode(ResultCode.SUCCESS.getCode());
            r.setMsg(ResultCode.SUCCESS.getMessage());
        }else {
            r.setCode(ResultCode.FAILED.getCode());
            r.setMsg(ResultCode.FAILED.getMessage());
        }
    }
    @Test
    public void redisTest() {
//        SysUser sysUser = new SysUser();
//        sysUser.setUserId(1L);
//        sysUser.setUserAccount("admin");
//        sysUser.setPassword("123456");
//        redisService.setCacheObject("u", sysUser);
        SysUser u = redisService.getCacheObject("u", SysUser.class);
        System.out.println(u);
    }
    @Test
    public void jwtTest2(){
                Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", 1L);
        // 使用固定的密钥字符串来创建 SecretKey
        String secretKeyString = "UlauytDx8ZBgOuF9LA+sZ/maWCVv4My9aCnVUblsKvs="; // 确保这个密钥足够复杂，并保持私密
//        SecretKey key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String token1 = JwtUtils.createToken(hashMap, key);
//        System.out.println(token1);
//        Claims claims = JwtUtils.parseToken(token1, key);
//        System.out.println(claims);
    }
    @Test
    public void jwtTest1(){
//        SecureRandom random = new SecureRandom();
//        byte[] keyBytes = new byte[32]; // 256 位
//        random.nextBytes(keyBytes);
//        String secret = Base64.getEncoder().encodeToString(keyBytes);
//        System.out.println(secret);
        // 固定的密钥字符串
        String secret = "UlauytDx8ZBgOuF9LA+sZ/maWCVv4My9aCnVUblsKvs="; // 长度至少要满足算法需求

        // 使用 secret 创建密钥对象
//        JwtUtils.createToken()

        System.out.println("JWT is valid!");
    }
    @Test
    public void jwtTest3(){
        // 创建自定义 Claims
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", 1L);
        String secret = "UlauytDx8ZBgOuF9LA+sZ/maWCVv4My9aCnVUblsKvs="; // 长度至少要满足算法需求
        // 验证并解析 JWT
        String token = JwtUtils.createToken(hashMap, secret);
        System.out.println(token);
        Claims claims = JwtUtils.parseToken(token, secret);
        System.out.println("Decoded Claims: " + claims);
    }
}

