package org.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;

public interface NumberReceieverRepository {

    Ticket save(Ticket ticket);

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime date);
}
