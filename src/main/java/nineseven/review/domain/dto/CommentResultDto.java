package nineseven.review.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResultDto {

    private String commentId;
    private String comment;
    private String accuracy;
}
