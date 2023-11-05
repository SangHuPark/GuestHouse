package project.GuestHouse.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.GuestHouse.exception.JwtAccessDeniedHandler;
import project.GuestHouse.exception.JwtAuthenticationEntryPoint;
import project.GuestHouse.jwt.JwtFilter;
import project.GuestHouse.jwt.JwtTokenProvider;
import project.GuestHouse.service.UserService;

// @EnableWebSecurity 어노테이션을 달아주면 Security가 활성화된다.
// Security가 활성화되면 회원 가입, 로그인 틀을 만든 Controller가 작동하기 전에 Security가 가로채서 확인
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()

                .cors()
                .and()

                .authorizeHttpRequests()
                .antMatchers("/api/users/join", "/api/users/login").permitAll() // join, login 허용
                .antMatchers(HttpMethod.POST,"/**").authenticated()
                .antMatchers(HttpMethod.DELETE,"/**").authenticated()
                .antMatchers(HttpMethod.PUT,"/**").authenticated()
                .antMatchers(HttpMethod.GET,"/**").authenticated()
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 을 사용하지 않고 jwt 사용하는 경우
                .and()

                // exceptionHandling 시 만든 클래스를 포함
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()

                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) // UserNamePasswordAuthenticationFilter 적용하기 전에 JWTTokenFilter 적용
                .build();
    }

}
