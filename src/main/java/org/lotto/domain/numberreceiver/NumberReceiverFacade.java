package org.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import org.lotto.domain.numberreceiver.dto.InputNumberResultDto;

import java.util.Set;

import static org.lotto.domain.numberreceiver.dto.InputNumberResultDto.*;

@AllArgsConstructor
public class NumberReceiverFacade {
    private final NumberValidator validator;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (areAllNumbersInRange) {
            return  builder()
                    .message("success")
                    .build();

        }
        return builder()
                .message("failed")
                .build();
    }

}

