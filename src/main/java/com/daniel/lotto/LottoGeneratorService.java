package com.daniel.lotto;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class LottoGeneratorService {

    private Random random = new Random(System.currentTimeMillis());

    public ArrayList<Integer> generateNumbers(int amountOfNumbers) {
        ArrayList<Integer> bucket = new ArrayList<>();

        for (int i = 0; i < amountOfNumbers; ++i) {
            bucket.add(random.nextInt(49) + 1);
        }

        return bucket;
    }
}