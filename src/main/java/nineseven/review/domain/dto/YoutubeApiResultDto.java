package nineseven.review.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class YoutubeApiResultDto {

    private String videoId;
    private String videoTitle;
    private String channelTitle;
    private int viewCount;
    private String subscriberCount;

    @Builder
    public YoutubeApiResultDto(String videoId, String videoTitle, String channelTitle, int viewCount, String subscriberCount) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
        this.subscriberCount = subscriberCount;
    }
}
