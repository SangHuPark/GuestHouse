package project.GuestHouse.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String createToken(String nickname, String key, long expireTimeMs){
        Claims claims = Jwts.claims();
        claims.put("nickname",nickname);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256,key) // 어떤 암호화방식으로 사인할지, key로 잠금
                .compact();
    }

    public static String getNickname(String token, String secretKey){
        return extractClaims(token, secretKey).get("nickname", String.class);
    }

    public static boolean isExpired(String token, String secretKey){
        Date expirationDate = extractClaims(token, secretKey).getExpiration();
        return expirationDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
