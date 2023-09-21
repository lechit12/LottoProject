package org.lotto.domain.numbergenerator;


import lombok.AllArgsConstructor;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable randomGenerable;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceieverFacade numberReceieverFacade;

    public WinningNumbersDto generateWinningNumbers()
    {
        LocalDateTime nextDrawDate = numberReceieverFacade.retrieveNextDrawDate();
        SixRandomNumbersDto dto = randomGenerable.generateSixRandomNumbers();
        Set<Integer>winningNumbers=dto.numbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                        .winningNumbers(winningNumbers)
                        .date(nextDrawDate)
                        .build());
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumberByDate(LocalDateTime date)
    {
        WinningNumbers numbersByDate = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(()-> new WinningNumbersNotFoundException("Not Found"));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .date(numbersByDate.date())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate()
    {
        LocalDateTime nextDrawDate = numberReceieverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }




}
