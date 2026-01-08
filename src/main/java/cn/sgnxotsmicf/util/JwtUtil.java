package cn.sgnxotsmicf.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lixiang
 * @CreateDate: 2026/1/5 17:00
 * @Version: 1.0
 * @Description: JWT工具类
 */
public class JwtUtil {

    private final static String secret = "dauw;/dqw=lixiangzhenshauiqi";

    /**
     * 创建JWT Token
     * @param userJson 用户信息Json字符串
     * @return JWT Token
     */
    public static String createToken(String userJson) {
        Map<String, Object> claims_header = new HashMap<>();
        claims_header.put("alg", "HS256");
        claims_header.put("typ", "JWT");
        return JWT.create()
                // 设置Header(头部)
                .withHeader(claims_header)
                // 设置Payload(负载)
                .withClaim("user", userJson)
                // 设置签名(签名算法，密钥)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证JWT Token
     * @param token JWT Token
     * @return 是否验证通过
     */
    public static Boolean verifyToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 解析JWT Token
     * @param token JWT Token
     * @return 用户信息Json字符串
     */
    public static String parseToken(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaim("user").asString();
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
