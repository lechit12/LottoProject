package org.lotto.domain.numbergenerator;

import org.junit.jupiter.api.Test;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumberGeneratorFacadeTest {

    private final WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryImpl();
    NumberReceieverFacade numberReceieverFacade = mock(NumberReceieverFacade.class);


    @Test
    public void it_should_return_set_of_required_size() {
        RandomNumberGenerable generator = new RandomGenerator();
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
        RandomNumberGenerable generator = new RandomGenerator();
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

}