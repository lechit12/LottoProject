package org.lotto.domain.numbergenerator;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomGenerator implements RandomNumberGenerable {
    private final int LOWER_BAND = 1;
    private final int UPPER_BAND = 99;
    private final int RANDOM_NUMBER_BOUND = (UPPER_BAND - LOWER_BAND) + 1;


    @Override
    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            int randomNumber = generateRandom();
            winningNumbers.add(randomNumber);
        }

        return winningNumbers;

    }

    private int generateRandom() {

        Random random = new Random();
        return random.nextInt(RANDOM_NUMBER_BOUND) + 1;
    }


}
