package org.lotto.domain.numberreceiver;


import org.junit.jupiter.api.Test;
import org.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import org.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.AdjustableClock;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceieverFacadeTest {
    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2023, 2, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    NumberReceieverFacade numberReceieverFacade = new NumberReceieverFacade(
            new NumberValidator(),
            new InMemoryNumberReceieverRepositoryTestImpl(),
            clock
    );

    @Test
    public void should_return_success_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        //when
        InputNumberResultDto result = numberReceieverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);
        //when
        InputNumberResultDto result = numberReceieverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);
        //when
        InputNumberResultDto result = numberReceieverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_at_least_one_number_out_of_1_to_99() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2000, 3, 4, 5, 6);
        //when
        InputNumberResultDto result = numberReceieverFacade.inputNumbers(numbersFromUser);
        //then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_save_to_database_when_user_gave_six_numbers() {
        //given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceieverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = LocalDateTime.of(2023, 2, 15, 13, 0, 0);
        //when

        List<TicketDto> ticketDtos = numberReceieverFacade.userNumbers(drawDate);
        //then

        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(drawDate)
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );


    }
}