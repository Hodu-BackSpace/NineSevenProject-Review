package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.common.annotation.TokenInfo;
import nineseven.review.domain.dto.CommentDto;
import nineseven.review.domain.dto.VideoListDto;
import nineseven.review.service.youtube.YoutubeApiServiceV2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pryt")
@Slf4j
public class YoutubeApiController {

    private final YoutubeApiServiceV2 youtubeApiService;
    private final List<VideoListDto> userTestData = new ArrayList<>();

    @PostConstruct
    public void init() {
        userTestData.add(new VideoListDto("asdsad", "title1", "description1"));
        userTestData.add(new VideoListDto("sdsddd", "title2", "description2"));
        userTestData.add(new VideoListDto("gdsgsd", "title3", "description3"));
    }


    @GetMapping("/list")
    public List<VideoListDto> videoList(@TokenInfo String authToken) {
//        List<VideoListDto> userVideoList = youtubeApiService.getUserChannelId(authToken);

        return userTestData;
    }

    @GetMapping("/comments/{videoId}")
    public List<CommentDto> getComments(@PathVariable("videoId") String videoID) {
        return youtubeApiService.getCommentList(videoID);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@TokenInfo String authToken,@RequestBody List<String> comments) {
        youtubeApiService.deleteComment(authToken, comments);
        return "OK!";
    }

}