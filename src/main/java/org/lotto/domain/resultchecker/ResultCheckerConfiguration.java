package org.lotto.domain.resultchecker;

import org.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import org.lotto.domain.numberreceiver.NumberReceieverFacade;
public class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(WinningNumbersGeneratorFacade generatorFacade, NumberReceieverFacade receiverFacade, PlayerRepository playerRepository) {
        WinnersRetriever winnerGenerator = new WinnersRetriever();
        return new ResultCheckerFacade(generatorFacade, receiverFacade, playerRepository, winnerGenerator);
    }
}