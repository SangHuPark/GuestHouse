package project.GuestHouse.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import project.GuestHouse.domain.entity.User;
import project.GuestHouse.service.UserService;
import project.GuestHouse.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Value("${jwt.secret}")
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("authorizationHeader : {}" + authorizationHeader);

        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            logger.error("token 추출에 실패했습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        if(authorizationHeader.startsWith("Bearer ")){
            try{
                String userName = JwtUtil.getUserName(token, secretKey);
                //UserName Token에서 꺼내기
                logger.info("userName : {}" + userName);
                //UserDetail 가져오기 >> UserRole
                User user = userService.getUserByUserName(userName);
                logger.info("userName:{}, userRole:{}", user.getUserName(), user.getRole());


                //문열어주기 >> 허용
                //Role 바인딩
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }catch (IllegalArgumentException e){
                throw new SNSException(ErrorCode.INVALID_TOKEN);
            }catch (ExpiredJwtException e){
                logger.info("만료된 토큰입니다.");
                throw new SNSException(ErrorCode.INVALID_TOKEN,"토큰 기한 만료");
            }catch (SignatureException e){
                logger.info("서명이 일치하지 않습니다.");
                throw new SNSException(ErrorCode.INVALID_TOKEN,"서명 불일치");
            }
        }
        else{
            logger.error("헤더를 가져오는 과정에서 에러가 났습니다. 헤더가 null이거나 잘못되었습니다.");
        }
        filterChain.doFilter(request, response);
    }
}
