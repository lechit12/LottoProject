package org.lotto.domain.numberreceiver;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

import org.lotto.domain.numberreceiver.dto.NumberReceieverResponseDto;
import org.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.AdjustableClock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();

    @Test
    public void it_should_return_correct_response_when_user_input_six_numbers_in_range() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        // when
        NumberReceieverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceieverResponseDto expectedResponse = new NumberReceieverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_six_numbers_but_one_number_is_out_of_range() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 100);

        // when
        NumberReceieverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceieverResponseDto expectedResponse = new NumberReceieverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_six_numbers_but_one_number_is_out_of_range_and_is_negative() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, -4, 5, 6);

        // when
        NumberReceieverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceieverResponseDto expectedResponse = new NumberReceieverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_less_than_six_numbers() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        // when
        NumberReceieverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceieverResponseDto expectedResponse = new NumberReceieverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_more_than_six_numbers() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        NumberReceieverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceieverResponseDto expectedResponse = new NumberReceieverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_correct_hash() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();

        // then
        assertThat(response).hasSize(36);
        assertThat(response).isNotNull();
    }

    @Test
    public void it_should_return_correct_draw_date() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 26, 12, 0, 0);

        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 26, 12, 0, 0);

        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_tickets_with_correct_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGenerator();

        Instant fixedInstant = LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceieverResponseDto numberReceieverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceieverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceieverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceieverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceieverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        LocalDateTime drawDate = numberReceieverResponseDto.ticketDto().drawDate();
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1);
    }

    @Test
    public void it_should_return_empty_collections_if_there_are_no_tickets() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = LocalDateTime.now(clock);

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        HashGenerable hashGenerator = new HashGenerator();

        Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        NumberReceieverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void it_should_return_next_draw_date() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceieverFacade numberReceiverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.retrieveNextDrawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }
    @Test
    public void it_should_return_Ticket_by_Hash(){
        //given
        TicketRepository ticketRepository = mock(TicketRepositoryTestImpl.class);
        HashGenerable hashGenerator = new HashGenerator();

        NumberReceieverFacade numberReceieverFacade = new NumberReceieverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        String mockHash = "mockHash";
        LocalDateTime mockDrawDate = LocalDateTime.of(2023, 1, 1, 0, 0); // przykładowa data
        Set<Integer> mockNumbers = Set.of(1, 2, 3,4,5,6);
        Ticket mockTicket = new Ticket("mockHash",mockDrawDate,mockNumbers);


        // Konfiguracja zachowania mocka
        when(ticketRepository.findByHash(mockHash)).thenReturn(mockTicket);

        // Wywołanie testowanej metody
        TicketDto result = numberReceieverFacade.findByHash(mockHash);

        // Sprawdzenie rezultatów
        assertEquals(mockHash, result.hash());
        assertEquals(mockNumbers, result.numbers());
        //assertEquals("mockDrawDate", result.);

    }
    @Test
    public void it_should_return_ticket_by_Hash(){
        //given
        TicketRepository ticketRepository = mock(TicketRepositoryTestImpl.class);


        String mockHash = "mockHash";
        LocalDateTime mockDrawDate = LocalDateTime.of(2023, 1, 1, 0, 0); // przykładowa data
        Set<Integer> mockNumbers = Set.of(1, 2, 3,4,5,6);
        Ticket mockTicket = new Ticket("mockHash",mockDrawDate,mockNumbers);


        // Konfiguracja zachowania mocka
        when(ticketRepository.findByHash(mockHash)).thenReturn(mockTicket);

        // Wywołanie testowanej metody
        TicketDto result = TicketMapper.mapFromTicket(mockTicket);

        // Sprawdzenie rezultatów
        assertEquals(mockHash, result.hash());
        assertEquals(mockNumbers, result.numbers());
        //assertEquals("mockDrawDate", result.);

    }
}