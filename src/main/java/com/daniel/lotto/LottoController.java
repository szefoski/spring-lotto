package com.daniel.lotto;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/generate-lotto")
    @ResponseBody
    public String generateLotto(@RequestParam("id") String amount) {
        try {
            return generateNumbers(Integer.parseInt(amount)).toString();
        } catch (NumberFormatException e) {
            return "Wrong 'amount' parameter value:</br>" + amount;
        }
    }
}
