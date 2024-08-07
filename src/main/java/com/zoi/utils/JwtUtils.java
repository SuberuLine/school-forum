package com.zoi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Value("${spring.security.jwt.key}")
    String key;

    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;

    /**
     * 注销token
     * @param headerToken
     * @return deleteToken的执行结果
     */
    public boolean invalidateJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if(token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            String id = jwt.getId();
            // 取出UUID调用方法将其注册至黑名单中
            return deleteToken(id, jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 将登出后的JWT UUID存放到Redis标识为无效token
     * @param uuid 目标JWT
     * @param time 该JWT剩余时间（过期时间）
     * @return 注销是否成功
     */
    private boolean deleteToken(String uuid, Date time) {
        if (this.isInvalidToken(uuid)) return false;
        Date now = new Date();
        long expire = Math.max(time.getTime() - now.getTime(), 0);  // 防止存入负数时长token
        template.opsForValue().set(Const.JWT_BLACK_LIST + uuid, "", expire, TimeUnit.MILLISECONDS);
        return true;
    }

    /**
     * 判断Token是否失效
     * @return Redis中是否有该Token
     */
    private boolean isInvalidToken(String uuid) {
        return Boolean.TRUE.equals(template.hasKey(Const.JWT_BLACK_LIST + uuid));
    }

    /**
     * 创建新的JWT Token
     * @param details 当前用户信息
     * @param id
     * @param username
     * @return 创建成功的JWT
     */
    public String createJwt(UserDetails details, int id, String username){
        Algorithm algorithm = Algorithm.HMAC256(key);
        Date expire = this.expireTime();
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString()) // 添加一个UUID用于记录黑名单，将其作为JWT的ID属性
                .withClaim("id", id)    //配置JWT自定义信息
                .withClaim("name", username)
                .withClaim("authorities", details.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expire)  // 设置过期时间
                .withIssuedAt(new Date()) // 设置创建创建时间
                .sign(algorithm); // 最终签名
    }

    /**
     * 根据Jwt验证并解析用户信息
     * @param headerToken
     * @return 如果是过期令牌则返回null 否则返回解密后信息
     */
    public DecodedJWT resolveJwt(String headerToken) {
        String token = this.convertToken(headerToken);
        if (token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token); // 对JWT令牌进行验证，看看是否被修改
            if (this.isInvalidToken(verify.getId())) return null;
            Date expiresAt = verify.getExpiresAt();
            return new Date().after(expiresAt) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("114514")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Integer toId(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }

    private String convertToken(String headerToken) {
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            return null;
        }
        return headerToken.substring(7);
    }

    public Date expireTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire * 24);
        return calendar.getTime();
    }

}
