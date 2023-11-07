package project.GuestHouse.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email) {
        // 원하는 정보를 담기 위한 Claim 이라는 공간을 jwt 에서 제공.
        Claims claims = Jwts.claims();

        // Jwts 클래스에서 제공하는 일종의 Map 자료구조
        claims.put("email", email);

        long expireTimeMs = 1000 * 60 * 60; // 토큰 유효 시간: 1시간
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis() + expireTimeMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 어떤 암호화 방식으로 사인할지, key로 잠금
                .compact();
    }

    public boolean isExpired(String token) {
        /*try {
            Date now = new Date();
            Date exp = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
            return now.before(exp);
        } catch (Exception e) {
            log.info("error={}", e.getMessage());
            return false;
        }*/

//        return extractClaims(token).getExpiration().before(new Date());
        Date now = new Date();
        Date exp = extractClaims(token).getExpiration();
        return now.before(exp);
    }

    public String getEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    //    private final CustomUserDetailService userDetailService;

    // 토큰에서 email 추출
    /*public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }*/

}