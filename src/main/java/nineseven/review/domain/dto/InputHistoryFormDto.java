package nineseven.review.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nineseven.review.domain.InputHistory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InputHistoryFormDto {

    @NotBlank(message = "Must Title")
    private String title;

    @NotNull(message = "Must Subscribe")
    private Integer subscribe;

    @Builder
    public InputHistoryFormDto(String title, Integer subscribe) {
        this.title = title;
        this.subscribe = subscribe;
    }

    public static InputHistory of(InputHistoryFormDto inputHistoryFormDto) {
        return InputHistory.builder()
                .inputTitle(inputHistoryFormDto.getTitle())
                .inputSubscribe(inputHistoryFormDto.getSubscribe())
                .build();
    }

    public static InputHistoryFormDto create(InputHistory inputHistory) {
        return InputHistoryFormDto.builder()
                .title(inputHistory.getInputTitle())
                .subscribe(inputHistory.getInputSubscribe())
                .build();
    }
}
