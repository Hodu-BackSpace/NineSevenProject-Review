package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.common.annotation.TokenInfo;
import nineseven.review.domain.dto.CommentDto;
import nineseven.review.domain.dto.VideoListDto;
import nineseven.review.service.youtube.YoutubeApiServiceV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pryt")
@Slf4j
public class YoutubeApiController {

    private final YoutubeApiServiceV2 youtubeApiService;

    @GetMapping("/list")
    public List<VideoListDto> videoList(@TokenInfo String authToken) {
        List<VideoListDto> userVideoList = youtubeApiService.getUserChannelId(authToken);

        return userVideoList;
    }

    @GetMapping("/comments/{videoId}")
    public List<CommentDto> getComments(@PathVariable("videoId") String videoID){
        return youtubeApiService.getCommentList(videoID);
    }
}
