package org.lotto.domain.numbergenerator;


import lombok.AllArgsConstructor;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable winningNumberGenerator;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceieverFacade numberReceieverFacade;

    public WinningNumbersDto generateWinningNumbers()
    {
        LocalDateTime nextDrawDate = numberReceieverFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = winningNumberGenerator.generateSixRandomNumbers();
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
                .orElseThrow(()-> new RuntimeException("Not Found"));
        return WinningNumbersDto.builder().build();
    }

    public boolean areWinningNumbersGeneratedByDate()
    {
        LocalDateTime nextDrawDate = numberReceieverFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }




}
