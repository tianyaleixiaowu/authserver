package com.maimeng.authserver.global.jwt;

import com.maimeng.authserver.global.util.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

/**
 * Created by ace on 2017/9/10.
 */
public class JWTHelper {
    private static RsaKeyHelper rsaKeyHelper = new RsaKeyHelper();

    /**
     * 密钥加密token
     */
    public static String generateToken(JwtInfo jwtInfo, String priKeyPath, int expire) throws Exception {
        return Jwts.builder()
                .setSubject(jwtInfo.getUserId() + "")
                .claim(Constant.JWT_KEY_USER_ID, jwtInfo.getUserId())
                .claim(Constant.JWT_KEY_NAME, jwtInfo.getName())
                .claim(Constant.JWT_KEY_CREATETIME, jwtInfo.getCreateTime())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKeyPath))
                .compact();
    }

    /**
     * 密钥加密token
     */
    public static String generateToken(JwtInfo jwtInfo, byte priKey[], int expire) throws Exception {
        return Jwts.builder()
                .setSubject(jwtInfo.getUserId())
                .claim(Constant.JWT_KEY_USER_ID, jwtInfo.getUserId())
                .claim(Constant.JWT_KEY_CREATETIME, jwtInfo.getCreateTime())
                .claim(Constant.JWT_KEY_NAME, jwtInfo.getName())
                .setExpiration(DateTime.now().plusSeconds(expire).toDate())
                .signWith(SignatureAlgorithm.RS256, rsaKeyHelper.getPrivateKey(priKey))
                .compact();
    }

    /**
     * 公钥解析token
     */
    public static Jws<Claims> parserToken(String token, String pubKeyPath) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKeyPath)).parseClaimsJws
                (token);
    }

    /**
     * 公钥解析token
     */
    public static Jws<Claims> parserToken(String token, byte[] pubKey) throws Exception {
        return Jwts.parser().setSigningKey(rsaKeyHelper.getPublicKey(pubKey)).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     */
    public static JwtInfo getInfoFromToken(String token, String pubKeyPath) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKeyPath);
        Claims body = claimsJws.getBody();
        return new JwtInfo(body.getSubject(), getObjectValue(body.get(Constant.JWT_KEY_NAME)),
                Long.valueOf(getObjectValue(body.get(Constant.JWT_KEY_CREATETIME))));
    }

    /**
     * 获取token中的用户信息
     *
     */
    public static JwtInfo getInfoFromToken(String token, byte[] pubKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, pubKey);
        Claims body = claimsJws.getBody();
        return new JwtInfo(body.getSubject(), getObjectValue(body.get(Constant.JWT_KEY_NAME)),
                Long.valueOf(getObjectValue(body.get(Constant.JWT_KEY_CREATETIME))));
    }

    private static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}
