package org.lotto.domain.numberreceiver;

import lombok.Builder;

import java.util.Set;
@Builder
record Ticket(String hash, java.time.LocalDateTime drawDate, Set<Integer> numbers) {

}
