package com.daniel.lotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LottoController {

    @Autowired
    private LottoGeneratorService service;

    @GetMapping("/generate-lotto")
    @ResponseBody
    public String generateLotto(@RequestParam("id") String amount) {
        try {
            return service.generateNumbers(Integer.parseInt(amount)).toString();
        } catch (NumberFormatException e) {
            return "Wrong 'amount' parameter value:</br>" + amount;
        }
    }
}
