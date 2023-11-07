package project.GuestHouse.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import project.GuestHouse.exception.ErrorCode;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/users/login", "/api/users/signup"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
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
//        if (!jwtTokenProvider.isExpired(token)) {
//            logger.error("만료된 Token 입니다.");
//            filterChain.doFilter(request, response);
//            return;
//        }

        try {
            if (jwtTokenProvider.isExpired(token)) {
                // token 에서 email 꺼내기
                String email = jwtTokenProvider.getEmail(token);
                logger.info("email: " + email);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority("USER")));

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));


                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                /* Authentication authentication = jwtTokenProvider.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication); */
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED_ERROR.name());
        } catch(SignatureException e) {
            request.setAttribute("exception", ErrorCode.TOKEN_SIGNATURE_ERROR.name());
        }catch (Exception e){
            logger.error("[Exception] cause: {} , message: {}" + NestedExceptionUtils.getMostSpecificCause(e) + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
