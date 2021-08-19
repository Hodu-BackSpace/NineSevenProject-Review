package nineseven.review.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class YoutubeApiAiDto {

    private String videoId;
    private String videoTitle;
    private int viewCount;
    private String subscriberCount;

    public YoutubeApiAiDto() {
    }

    public YoutubeApiAiDto(String videoId, String videoTitle, int viewCount, String subscriberCount) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.viewCount = viewCount;
        this.subscriberCount = subscriberCount;
    }


}
