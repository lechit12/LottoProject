package org.lotto.domain.resultannoucer;

import org.lotto.domain.resultannoucer.dto.ResponseDto;

public class ResultMapper {
    static ResponseDto mapToDto(ResultResponse resultResponse)
    {
        return ResponseDto.builder()
                .drawDate(resultResponse.drawDate())
                .hash(resultResponse.hash())
                .hitNumbers(resultResponse.hitNumbers())
                .numbers(resultResponse.numbers())
                .isWinner(resultResponse.isWinner())
                .build();
    }
}
