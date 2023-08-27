package org.lotto.domain.numbergenerator;

import org.lotto.domain.numbergenerator.dto.WinningNumbersDto;

public class WinningNumbersNotFoundException extends RuntimeException{
    WinningNumbersNotFoundException(String message)
    {
        super(message);
    }
}
