package top.peachyao.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

/**
 * <p>
 * JWT工具类
 * </p>
 *
 * @author Peachyao
 * @since 2026-03-24
 */
@Component
public class JwtUtils {
    private static long expireTime;
    private static String secretKey;
    private static SecretKey key;  // 缓存 SecretKey 对象，避免重复转换

    @Value("${token.secretKey}")
    public void setSecretKey(String secretKey) {
        JwtUtils.secretKey = secretKey;
        // 当 secretKey 被设置时，立即生成 SecretKey 对象
        JwtUtils.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Value("${token.expireTime}")
    public void setExpireTime(long expireTime) {
        JwtUtils.expireTime = expireTime;
    }

    // 判断token是否存在
    public static boolean judgeTokenIsExist(String token) {
        return token != null && !"".equals(token) && !"null".equals(token);
    }

    // 生成token
    public static String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, Jwts.SIG.HS512)   // 使用缓存的 SecretKey 和 HS512 算法
                .compact();
    }

    // 生成带角色权限的token
    public static String generateToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        StringBuilder sb = new StringBuilder();
        for (GrantedAuthority authority : authorities) {
            sb.append(authority.getAuthority()).append(",");
        }
        return Jwts.builder()
                .setSubject(subject)
                .claim("authorities", sb)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    // 生成自定义过期时间token
    public static String generateToken(String subject, long expireTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    // 获取tokenBody同时校验token是否有效（无效则会抛出异常）
    public static Claims getTokenBody(String token) {
        // 清理 token 前缀（原逻辑保留，注意可能缺少空格，但保持行为一致）
        String cleanToken = token.replace("Bearer", "").trim(); // 加入 trim 去除可能的前后空格
        return Jwts.parser()
                .verifyWith(key)          // 使用 SecretKey 验证签名
                .build()
                .parseSignedClaims(cleanToken)
                .getPayload();            // 获取 Claims
    }
}