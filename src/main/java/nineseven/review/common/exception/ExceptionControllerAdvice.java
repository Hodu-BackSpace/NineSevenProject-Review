package nineseven.review.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> basicError(MethodArgumentNotValidException e) {
        Map<String, String> errorDto = new HashMap();
        e.getFieldErrors().stream().forEach(fieldError -> {
            errorDto.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return errorDto;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorDto> notReadableError(HttpMessageNotReadableException e) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setStatusCode(400);
        errorDto.setMessage("Json Parse Error!!");

        log.info("{}",e.getHttpInputMessage());

        List<ErrorDto> list = new ArrayList<>();
        list.add(errorDto);
        return list;
    }
}
