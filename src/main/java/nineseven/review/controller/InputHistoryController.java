package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.common.exception.ChildException;
import nineseven.review.domain.dto.InputHistoryFormDto;
import nineseven.review.domain.dto.YoutubeApiResultDto;
import nineseven.review.service.InputHistoryService;
import nineseven.review.service.youtube.YoutubeApiServiceV1;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class InputHistoryController {


    @PostMapping("/save")
    public List<YoutubeApiResultDto> saveController(@Validated @RequestBody InputHistoryFormDto inputHistoryFormDto) {

        if (inputHistoryFormDto.getTitle().equals("abcd")) {
            throw new ChildException("error!");
        }
        return Collections.emptyList();
    }


}
