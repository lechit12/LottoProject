package org.lotto.domain.numberreceiver;

import java.util.Set;

record Ticket(String ticketId, java.time.LocalDateTime drawDate, Set<Integer> numbersFromUser) {

}
