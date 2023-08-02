package kr.co.dk.sample.api.config.security;


import io.jsonwebtoken.*;
import kr.co.dk.sample.api.common.model.ErrorCode;
import kr.co.dk.sample.api.config.security.dto.JwtTokenValidation;
import kr.co.dk.sample.api.util.SEED_KISA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final Logger log = LogManager.getLogger(JwtTokenProvider.class);
    private static final String JWT_TYPE = "jwtType";
    private static final String REFRESH_TYPE = "refresh";
    private static final String ACCESS_TYPE = "access";
    private static final String SECOND_LOGIN_TYPE = "second_login";
    private static final String mbIdx = "mb_idx";
    private static final String mbId = "mb_id";
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.user.accessToken.expirationInMinute}")
    private int userAccessTokenExpiration;
    @Value("${jwt.user.refreshToken.expirationInMinute}")
    private int userRefreshTokenExpiration;

    public String createUserAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createUserLoginAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createUserConfirmdAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createUserConfirmdRefreshToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, REFRESH_TYPE, userRefreshTokenExpiration);
    }

    private String generateToken(Map<String, Object> jwtMap, String jwtType, int tokenExpirationInMinute) {

        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + (tokenExpirationInMinute * 1000 * 60));
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.MINUTE, tokenExpirationInMinute);
        Date exp = cal.getTime();
        HashMap claims = new HashMap<>();
        claims.put(JWT_TYPE, jwtType);
        claims.put(mbIdx, jwtMap.get(mbIdx));

        // 암호화 위치
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        return jwtToken;
    }

    public Long getUserIdFromJWT(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            return Long.parseLong(claims.get(mbIdx).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserIdFromJWTString(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(mbIdx).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getJwtInfoRefresh(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getJwtInfo(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        } else {
            return null;
        }
        token = token.trim();
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getJwtInfo(String token) {
        try {
//            token = new String(SEED_KISA.DECRYPT(token));
            token = token.trim();
            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JwtTokenValidation validateToken(String authToken) {
        JwtTokenValidation jwtTokenValidation = new JwtTokenValidation();
        jwtTokenValidation.setSuccess(false);
        jwtTokenValidation.setCode(ErrorCode.CODE_1103);

        if (authToken == null) {
            jwtTokenValidation.setCode(ErrorCode.CODE_1104);
            return jwtTokenValidation;
        }

        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            jwtTokenValidation.setSuccess(true);
            jwtTokenValidation.setCode(null);

            if (claims.getBody().get(JWT_TYPE).toString().equalsIgnoreCase(ACCESS_TYPE)) {
                jwtTokenValidation.setAccessToken(true);
            } else if (claims.getBody().get(JWT_TYPE).toString().equalsIgnoreCase(REFRESH_TYPE)) {
                jwtTokenValidation.setRefreshToken(true);
            }

            return jwtTokenValidation;

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature\r\n");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token\r\n");
        } catch (ExpiredJwtException ex) {
            jwtTokenValidation.setCode(ErrorCode.CODE_1101);
            log.error("Expired JWT token\r\n");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token\r\n");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.\r\n");
        }

        return jwtTokenValidation;
    }
}
