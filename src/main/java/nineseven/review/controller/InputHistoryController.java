package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.InputHistoryFormDto;
import nineseven.review.domain.dto.YoutubeApiResultDto;
import nineseven.review.service.InputHistoryService;
import nineseven.review.service.YoutubeApiService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class InputHistoryController {

    private final InputHistoryService inputHistoryService;
    private final YoutubeApiService youtubeApiService;

    @PostMapping("/save")
    public List<YoutubeApiResultDto> saveController(@Validated @RequestBody InputHistoryFormDto inputHistoryFormDto) {

        List<YoutubeApiResultDto> resultList = youtubeApiService.getResultList(inputHistoryFormDto.getTitle());

        //Log Save
        inputHistoryService.save(inputHistoryFormDto);
        return resultList;
    }


}
