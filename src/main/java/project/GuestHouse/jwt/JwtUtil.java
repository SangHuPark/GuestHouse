package project.GuestHouse.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public static String createToken(String nickname, String secretKey, long expireTimeMs) {
        Claims claims = Jwts.claims();
        // Jwts 클래스에서 제공하는 일종의 Map 자료구조
        claims.put("nickname",nickname);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey) // 어떤 암호화 방식으로 사인할지, key로 잠금
                .compact();
    }

    public static boolean isExpired(String token, String secretKey) {
        Date expirationDate = extractClaims(token, secretKey).getExpiration();
        return expirationDate.before(new Date());
    }

    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public static String getNickname(String token, String secretKey) {
        return extractClaims(token, secretKey).get("nickname", String.class);
    }
}
