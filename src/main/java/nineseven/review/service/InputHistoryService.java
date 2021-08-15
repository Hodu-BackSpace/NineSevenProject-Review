package nineseven.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.InputHistory;
import nineseven.review.domain.dto.InputHistoryFormDto;
import nineseven.review.repository.InputHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class InputHistoryService {

    private final InputHistoryRepository inputHistoryRepository;

    @Transactional(readOnly = false)
    public void save(InputHistoryFormDto inputHistoryFormDto) {
        InputHistory newInputHistory = InputHistoryFormDto.of(inputHistoryFormDto);

        inputHistoryRepository.save(newInputHistory);
    }

    public List<InputHistoryFormDto> findAll() {
        List<InputHistory> result = inputHistoryRepository.findAll();
        List<InputHistoryFormDto> transferDtoResult = result.stream().map(InputHistoryFormDto::create)
                .collect(Collectors.toList());

        return transferDtoResult;
    }

}
