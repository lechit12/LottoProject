package org.lotto.domain.numberreceiver.dto;

import lombok.Builder;

@Builder
public record NumberReceieverResponseDto(
        TicketDto ticketDto,
        String message) {
}