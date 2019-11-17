package com.daniel.lotto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @GetMapping("/generate/{amount}")
    public ResponseEntity<List<Integer>> generate(@PathVariable("amount") int amount) {
        return new ResponseEntity<List<Integer>>(service.generateNumbers(amount), HttpStatus.OK);
    }

    @GetMapping("/welcome/{name}")
    public ResponseEntity<HashMap<String, String>> generate2(@PathVariable("name") String name) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Super person is", name);
        data.put("Have a great one", name);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

	@RequestMapping(value = "/check-wins/", method = RequestMethod.POST)
	public ResponseEntity<?> updateUser(@RequestBody ArrayList<Integer> numbers) {
        //TODO check if given numbers won in the past
		return new ResponseEntity<String>("Given numbers: " + numbers.toString(), HttpStatus.OK);
	}
}
