package org.lotto.domain.resultchecker.Dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PlayersDto(List<ResultDto> results, String message) {
}
