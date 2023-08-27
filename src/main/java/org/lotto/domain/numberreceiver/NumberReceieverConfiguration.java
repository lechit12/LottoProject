package org.lotto.domain.numberreceiver;

import java.time.Clock;

public class NumberReceieverConfiguration {


    NumberReceieverFacade createForTest(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceieverFacade(numberValidator, drawDateGenerator, hashGenerator, ticketRepository);
    }


}