package org.lotto.domain.numberreceiver;

import org.lotto.domain.numberreceiver.dto.TicketDto;

public class TicketMapper {
    public static TicketDto mapFromTicket(Ticket ticket)
    {
        return TicketDto.builder()
                .numbersFromUser(ticket.numbers())
                .ticketId(ticket.hash())
                .drawDate(ticket.drawDate())
                .build();
    }
}
