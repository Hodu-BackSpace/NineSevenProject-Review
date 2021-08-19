package nineseven.review.common.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {

    private String fieldName;
    private String defaultMessage;
    private String rejectedValue;
}
