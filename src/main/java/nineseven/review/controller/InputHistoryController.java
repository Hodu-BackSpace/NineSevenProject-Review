package nineseven.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.InputHistoryFormDto;
import nineseven.review.service.InputHistoryService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class InputHistoryController {

    private final InputHistoryService inputHistoryService;

    @PostMapping("/save")
    public Object saveController(@Validated @RequestBody InputHistoryFormDto inputHistoryFormDto,
                                 BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return bindingResult.getAllErrors();
        }
        inputHistoryService.save(inputHistoryFormDto);
        return "OK!";
    }

}
