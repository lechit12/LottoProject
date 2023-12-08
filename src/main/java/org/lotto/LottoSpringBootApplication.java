package org.lotto;

import org.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class})
public class LottoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }

}