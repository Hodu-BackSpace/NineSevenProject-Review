package nineseven.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {

    private String Comment;
    private String CommentID;

    public CommentDto() {
    }

    public CommentDto(String comment, String commentID) {
        Comment = comment;
        CommentID = commentID;
    }
}
