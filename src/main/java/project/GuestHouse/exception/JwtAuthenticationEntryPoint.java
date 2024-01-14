package project.GuestHouse.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.GuestHouse.exception.errorCode.UserErrorCode;
import project.GuestHouse.exception.response.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String)request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, UserErrorCode.TOKEN_NOT_EXIST);
        }
        else if (exception.equals(UserErrorCode.TOKEN_EXPIRED_ERROR.name())) {
            setResponse(response, UserErrorCode.TOKEN_EXPIRED_ERROR);
        }
        else if (exception.equals(UserErrorCode.TOKEN_SIGNATURE_ERROR.name())) {
            setResponse(response, UserErrorCode.TOKEN_SIGNATURE_ERROR);
        }
    }

    private void setResponse(HttpServletResponse response, UserErrorCode jwtUserErrorCode) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset-UTF-8"); // json 에 한글이 들어가기 때문에 content type 도 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrorResponse errorResponse = ErrorResponse.of(jwtUserErrorCode.getHttpStatus(), String.valueOf(jwtUserErrorCode.getCode()), jwtUserErrorCode.getMessage());
        String result = new ObjectMapper().writeValueAsString(errorResponse); // ObjectMapper().writeValueAsString() 으로 errorResponse 를 문자열로 바꾼다.
        response.getWriter().write(result); // 응답 내용 전송
        //
    }
}
