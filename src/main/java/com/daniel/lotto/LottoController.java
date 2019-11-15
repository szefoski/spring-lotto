package com.daniel.lotto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Random;

@Controller
public class LottoController {
    private Random random = new Random(System.currentTimeMillis());

    private ArrayList<Integer> generateNumbers(int amountOfNumbers) {
        ArrayList<Integer> bucket = new ArrayList<>();

        for (int i = 0; i < amountOfNumbers; ++i) {
            bucket.add(random.nextInt(49) + 1);
        }

        return bucket;
    }

    @GetMapping("/")
    public String home() {
        System.out.println("-------Generated Numbers--------");
        var bucket = generateNumbers(10);
        for(var number : bucket) {
            System.out.println(number);
        }
        System.out.println("--------------------------------");

        return "home";
    }
}
