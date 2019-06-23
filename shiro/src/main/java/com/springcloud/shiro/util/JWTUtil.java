package com.springcloud.shiro.util;

import com.springcloud.shiro.application.ApplicationYml;
import com.springcloud.shiro.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JWTUtil {

    @Autowired
    private static ApplicationYml applicationYml;

    /**
     * 用户登录后生成jwt
     * 使用HS.256算法，私匙使用用户密码
     * @param ttlMillis 过期时间
     * @param userModel 登录成功的user对象
     * @return
     */
    public static String createJWT(Long ttlMillis,UserModel userModel){

        /**
         * 指定签名的时候使用的签名算法（header部分），jjwt已对此封装
         */
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        /**
         * 生成jwt的时间
         */
        Long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        /**
         * 创建payload的私有声明，根据业务需要添加
         */
        Map<String,Object> map = new HashMap<>();
        map.put("id",userModel.getId());
        map.put("name",userModel.getName());
        map.put("password",userModel.getPassword());

        /**
         * 生成签名时使用的密钥secret，本地封装，一般可从配置文件读取，切记不可外露，外露之后意味着客户端可以自我签发jwt
         */
        String key = applicationYml.getSecretKey();

        /**
         * 生成签发人
         */
        String subject = userModel.getName();

        /**
         * 为payload添加各种标准声明和私有声明
         */
        JwtBuilder jwtBuilder = Jwts.builder()
                /**
                 * 先设置自定义的私有声明，如果写在标准声明之后会覆盖标准声明
                 */
                .setClaims(map)
                /**
                 * 设置JWT ID，JWT的唯一标识，一般不可重复，主要用来作为一次性token，回避重放攻击
                 */
                .setId(UUID.randomUUID().toString())
                /**
                 * JWT签发时间
                 */
                .setIssuedAt(now)
                /**
                 * 代表此JWT主体，即它的所有者，json格式字符窜，可以存放userId，roleId作为用户的唯一标识
                 */
                .setSubject(subject)
                /**
                 * 设置签名算法和签名所有的密钥
                 */
                .signWith(signatureAlgorithm,key);

                if (ttlMillis >= 0){
                    Long expMillis = nowMillis + ttlMillis;
                    Date exp = new Date(expMillis);
                    jwtBuilder.setExpiration(exp);
                }
                return jwtBuilder.compact();
    }

    /**
     * 校验token
     * @param token
     * @param userModel
     * @return
     */
    public static Boolean verify(String token, UserModel userModel){
        try {
            /**
             * 获取defaultJwtParser
             */
            Claims claims = Jwts.parser()
                    /**
                     * 设置签名的密钥
                     */
                    .setSigningKey(applicationYml.getSecretKey())
                    /**
                     * 设置需要解析的jwt
                     */
                    .parseClaimsJws(token).getBody();

                    if (claims.get("password").equals(userModel.getPassword())){
                        return true;
                    }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return null;
    }

    /**
     * token解密
     * @param token 加密后的token
     * @param secret 生成签名的密钥
     * @return
     */
    public static Claims parseJWT(String token,String secret){

        /**
         * 道道defaultJwtParser
         */
        Claims claims = Jwts.parser()
                /**
                 * 设置签名的密钥
                 */
                .setSigningKey(secret)
                /**
                 * 设置需要解密的jwt
                 */
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
