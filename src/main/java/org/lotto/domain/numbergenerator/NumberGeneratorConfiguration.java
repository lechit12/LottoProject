package org.lotto.domain.numbergenerator;

import org.lotto.domain.numberreceiver.NumberReceieverFacade;

public class NumberGeneratorConfiguration {

    WinningNumbersGeneratorFacade createForTest(RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, NumberReceieverFacade numberReceieverFacade) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new WinningNumbersGeneratorFacade(generator, winningNumberValidator, winningNumbersRepository, numberReceieverFacade);
    }
}
