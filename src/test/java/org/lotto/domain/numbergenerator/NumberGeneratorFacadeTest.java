package org.lotto.domain.numbergenerator;

import org.junit.jupiter.api.Test;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryImpl();
    NumberReceieverFacade numberReceieverFacade = mock(NumberReceieverFacade.class);

    @Test
    public void it_should_return_set_of_required_size() {
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        assertThat(generatedNumbers.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    public void it_should_return_set_of_required_size_within_required_range() {
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        int upperBand = 99;
        int lowerBand = 1;
        Set<Integer> winningNumbers = generatedNumbers.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream().allMatch(number -> number >= lowerBand && number <= upperBand);
        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void it_should_throw_an_exception_when_number_not_in_range() {
        Set<Integer> numbersOutOfRange=Set.of(1,2,3,4,5,100);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl(numbersOutOfRange);
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator= new NumberGeneratorConfiguration().createForTest(generator,winningNumbersRepository,numberReceieverFacade);
        assertThrows(IllegalStateException.class,numbersGenerator::generateWinningNumbers,"Number out of range!");
    }
    @Test
    public void it_should_return_collection_of_unique_values() {
        //given
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        WinningNumbersDto generatedNumbers = numbersGenerator.generateWinningNumbers();
        //then
        int generatedNumbersSize = new HashSet<>(generatedNumbers.getWinningNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(6);
    }

    @Test
    public void it_should_return_winning_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        WinningNumbersDto winningNumbersDto = numbersGenerator.retrieveWinningNumberByDate(drawDate);
        //then
        WinningNumbersDto expectedWinningNumbersDto = WinningNumbersDto.builder()
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        assertThat(expectedWinningNumbersDto).isEqualTo(winningNumbersDto);
    }

    @Test
    public void it_should_throw_an_exception_when_fail_to_retrieve_numbers_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        //then
        assertThrows(WinningNumbersNotFoundException.class, () -> winningNumbersGeneratorFacade.retrieveWinningNumberByDate(drawDate), "Not Found");
    }

    @Test
    public void it_should_return_true_if_numbers_are_generated_by_given_date() {
        //given
        LocalDateTime drawDate = LocalDateTime.of(2022, 12, 17, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .date(drawDate)
                .winningNumbers(generatedWinningNumbers)
                .build();
        winningNumbersRepository.save(winningNumbers);
        RandomNumberGenerable generator = new WinningNumberGeneratorTestImpl();
        when(numberReceieverFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGeneratorFacade numbersGenerator = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, numberReceieverFacade);
        //when
        boolean areWinningNumbersGeneratedByDate = numbersGenerator.areWinningNumbersGeneratedByDate();
        //then
        assertTrue(areWinningNumbersGeneratedByDate);

    }
}