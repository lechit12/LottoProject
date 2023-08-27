package org.lotto.domain.resultannoucer.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
@Builder
public record ResponseDto(String hash,Set<Integer>numbers, Set<Integer> hitNumbers, boolean isWinner, LocalDateTime drawDate) {
}
