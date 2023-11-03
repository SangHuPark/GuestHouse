package project.GuestHouse.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    /*@Value("${jwt.secret}")
    private final String secretKey;*/

    private final JwtTokenProvider jwtTokenProvider;

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = jwtTokenProvider.resolveToken(request);
        logger.info("authorization : " + authorization);

        // 토큰이 없거나 Bearer 형태로 보내지 않으면 block
        // 권한이 없는 요청,응답 객체를 필터에 넣어 주어야 한다.
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.error("잘못된 authorization");
            filterChain.doFilter(request, response); // filterChain 에 request, response 를 넘긴다. (request 객체에 인증 되었다고 인증 도장이 찍히는 의미)
            return;
        }

        // token 꺼내기
        // "Bearer {token}" 형식으로 공백을 split 후 1번째를 가져오면 token 만 추출됨
        String token = authorization.split(" ")[1];
        logger.info("token : " + token);

        // token Expired 되었는지
        if(jwtTokenProvider.isExpired(token, secretKey)) {
            logger.error("Token 만료 되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // token 에서 email 꺼내기
        String email = jwtTokenProvider.getEmail(token, secretKey);
        logger.info("email: " + email);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }*/

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 토큰 받아오기
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰이 유효하다면
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 토큰으로부터 유저 정보를 받아
            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            // SecurityContext 에 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 Filter 실행
        filterChain.doFilter(request, response);
    }
}
