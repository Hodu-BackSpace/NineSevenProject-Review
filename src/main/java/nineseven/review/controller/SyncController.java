package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.common.annotation.TokenInfo;
import nineseven.review.domain.dto.CommentDto;
import nineseven.review.domain.dto.VideoListDto;
import nineseven.review.service.SyncService;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pryt")
@Slf4j
public class SyncController {

    private final SyncService youtubeApiService;
    private final List<List<VideoListDto>> userTestData = new ArrayList<>();
    private final List<CommentDto> commentTestData = new ArrayList<>();

    @PostConstruct
    public void init() {
        List<VideoListDto> testDataList = new ArrayList<>();
        testDataList.addAll(List.of(new VideoListDto("asdsad", "title1", "description1"),
                new VideoListDto("sdsddd", "title2", "description2"),
                new VideoListDto("gdsgsd", "title3", "description3")));

        List<VideoListDto> testDataList2 = new ArrayList<>();
        testDataList2.addAll(List.of(new VideoListDto("gsdga", "title4", "description4"),
                new VideoListDto("sdgsadg", "title5", "description5"),
                new VideoListDto("dsfsdfdsf", "title6", "description6")));

        List<VideoListDto> testDataList3 = new ArrayList<>();
        testDataList3.addAll(List.of(new VideoListDto("hadshah", "title7", "description7"),
                new VideoListDto("sdagsdag", "title8", "description8"),
                new VideoListDto("dsgasgasd", "title9", "description9")));

        userTestData.add(testDataList);
        userTestData.add(testDataList2);
        userTestData.add(testDataList3);


        commentTestData.addAll(Arrays.asList(new CommentDto("test Comment", "abdcdcdf"),
                new CommentDto("test Comment2", "sadasfsaf"),
                new CommentDto("test Comment3", "gasdgsdgsad"),
                new CommentDto("test Comment4", "sdgasdg"),
                new CommentDto("test Comment5", "hfddfh"),
                new CommentDto("test Comment6", "ghjfghjghj"),
                new CommentDto("test Comment7", "kjhkhjkhj"),
                new CommentDto("test Comment8", "kjljklhjl")));
    }


    @GetMapping("/list")
    public List<List<VideoListDto>> videoList(@TokenInfo String authToken) {
//        List<VideoListDto> userVideoList = youtubeApiService.getUserChannelId(authToken);

        return userTestData;
    }

    @GetMapping("/comments/{videoId}")
    public List<CommentDto> getComments(@PathVariable("videoId") String videoID) {
//        return youtubeApiService.getCommentList(videoID);
        return commentTestData;
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@TokenInfo String authToken,@RequestBody List<String> comments) {
        youtubeApiService.deleteComment(authToken, comments);
        return "OK!";
    }

}