package org.lotto.domain.numbergenerator;

import org.lotto.domain.numberreceiver.NumberReceieverFacade;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NumberGeneratorConfiguration {


    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(WinningNumbersRepository winningNumbersRepository, NumberReceieverFacade numberReceieverFacade, RandomNumberGenerable randomNumberGenerator, WinningNumbersGeneratorFacadeConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(randomNumberGenerator, winningNumberValidator, winningNumbersRepository, numberReceieverFacade, properties);
    }

    WinningNumbersGeneratorFacade createForTest(RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceieverFacade numberReceieverFacade) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();
        return winningNumbersGeneratorFacade(winningNumbersRepository, numberReceieverFacade, generator, properties);
    }
}
