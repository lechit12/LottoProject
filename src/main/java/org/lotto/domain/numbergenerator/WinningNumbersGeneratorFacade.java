package org.lotto.domain.numbergenerator;


import lombok.AllArgsConstructor;
import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import org.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {

    private final RandomNumberGenerable winningNumberGenerator;
    private final WinningNumberValidator winningNumberValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final NumberReceiverFacade numberReceiverFacade;

    public WinningNumbersDto generateWinningNumbers()
    {
        LocalDateTime nextDrawDate = numberReceiverFacade.retriveNextDrawDate();
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
        LocalDateTime nextDrawDate = numberReceiverFacade.retriveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }




}
