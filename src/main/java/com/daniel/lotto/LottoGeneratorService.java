package com.daniel.lotto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class LottoGeneratorService {

    private Random random = new Random(System.currentTimeMillis());

    public List<Integer> generateNumbers(int amountOfNumbers) {
        return random.ints(amountOfNumbers, 1, 50).boxed().collect(Collectors.toUnmodifiableList());
    }
}