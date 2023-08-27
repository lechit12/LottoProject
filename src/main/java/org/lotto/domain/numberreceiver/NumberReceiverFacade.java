package org.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import org.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.lotto.domain.numberreceiver.dto.InputNumberResultDto.*;

@AllArgsConstructor
public class NumberReceiverFacade {
    private final NumberValidator validator;
    private final NumberReceiverRepository repository;
    private final Clock clock;
    private final DrawDateGenerator drawDateGenerator;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            String ticketId = UUID.randomUUID().toString();
            LocalDateTime drawDate = LocalDateTime.now(clock);
           Ticket savedTicket = repository.save(new Ticket(ticketId,drawDate,numbersFromUser));
            return  builder()
                    .drawDate(savedTicket.drawDate())
                    .ticketId(savedTicket.ticketId())
                    .numbersFromUser(savedTicket.numbersFromUser())
                    .message("success")
                    .build();

        }
        return builder()
                .message("failed")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date){
        List<Ticket> allTicketsByDrawDate=repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate
                .stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    public LocalDateTime retriveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }
}

