package nineseven.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoListDto {
    private String videoId;
    private String videoTitle;
    private String description;

    public VideoListDto() {
    }

    public VideoListDto(String videoId, String videoTitle, String description) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.description = description;
    }
}
