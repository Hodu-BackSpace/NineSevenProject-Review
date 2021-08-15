package nineseven.review.common.exception;

import lombok.Data;

@Data
public class ErrorDto {

    private int statusCode;
    private String message;
}
