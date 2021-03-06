package ltd.inmind.accelerator.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author shenlanAZ
 * @since 0.2
 */
@Slf4j
public class Jwt {

    private String jwt;

    private String secret;

    private static final long DEFAULT_EXPIRE_TIME = 10 * 60 * 1000;

    private Jwt(){
    }

    private Jwt(String _jwt, String _secret) {
        jwt = _jwt;
        secret = _secret;
    }

    public String getJwt(){
        return jwt;
    }

    private void setJwt(String _jwt){
        jwt = _jwt;
    }

    private void setSecret(String _secret) {
        secret = _secret;
    }

    public static Jwt create(String secret) {
        return create(null, secret);
    }

    public static Jwt create(Map<String, String> claims, String secret) {
        Date expire = new Date(System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);
        return create(claims, secret, expire);
    }

    public static Jwt create(Map<String, String> claims, String secret, Date expire) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTCreator.Builder builder = JWT.create();

        if (Objects.nonNull(claims)){
            claims.forEach(builder::withClaim);
        }

        if (Objects.nonNull(expire)) {
            builder.withExpiresAt(expire);
        }

        Jwt _jwt = new Jwt();
        _jwt.setJwt(builder.sign(algorithm));
        _jwt.setSecret(secret);
        return _jwt;
    }

    public Map<String,String> getClaims(){
        Map<String, String> claims = new HashMap<>();
        try {
            DecodedJWT decoded = JWT.decode(getJwt());
            decoded.getClaims()
                    .forEach((k,claim) -> claims.put(k, claim.asString()));
        }catch (JWTDecodeException e){
            log.info("jwt decode err, jwt: {}", jwt);
        }
        return claims;
    }

    public String getClaim(String key) {
        try {
            DecodedJWT decoded = JWT.decode(getJwt());
            return decoded.getClaim(key).asString();
        } catch (JWTDecodeException e) {
            log.info("jwt decode err, jwt: {}", jwt);
            return null;
        }
    }

    public boolean isValid(){
        if (Objects.isNull(secret))
            return false;
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            //效验TOKEN
            verifier.verify(getJwt());
            return true;
        } catch (JWTVerificationException e) {
            log.error("jwt verify failed", e);
            return false;
        }
    }

    public Jwt changeSecret(String _secret) {
        return new Jwt(jwt, _secret);
    }

    public static Jwt from(String _jwt) {
        return new Jwt(_jwt, null);
    }

    public static Jwt from(String _jwt, String _secret) {
        return new Jwt(_jwt, _secret);
    }

    @Override
    public String toString() {
        return getJwt();
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        Jwt _target = (Jwt) target;

        return Objects.equals(jwt, _target.jwt);
    }

    @Override
    public int hashCode() {
        return jwt != null ? jwt.hashCode() : 0;
    }
}
