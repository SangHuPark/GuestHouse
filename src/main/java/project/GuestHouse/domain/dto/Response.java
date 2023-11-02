package project.GuestHouse.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Builder
@Getter
public class Response<T> {
    private  boolean isSuccess;
    private String message;
    private T body;

    /*private T result;

    public static <T> Response<T> success(String message, T result){
        return new Response<>(true, message, result);
    }

    public static <T> Response<T> error(String message, T result){
        return new Response<>(false, message, result);
    }*/
}
