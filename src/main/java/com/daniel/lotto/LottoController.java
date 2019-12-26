package com.daniel.lotto;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LottoController {

    @Autowired
    private LottoGeneratorService generatorService;

    @Autowired
    private LottoGamesController gamesController;

    @GetMapping("/generate-lotto")
    public String generateLotto(@RequestParam("number") String amount) {
        try {
            return generatorService.generateNumbers(Integer.parseInt(amount)).toString();
        } catch (NumberFormatException e) {
            return "Wrong 'amount' parameter value:</br>" + amount;
        }
    }

    @GetMapping("/generate/{amount}")
    public ResponseEntity<?> generate(@PathVariable("amount") int amount) {
        return new ResponseEntity<>(generatorService.generateNumbers(amount), HttpStatus.OK);
    }

    @GetMapping("/welcome/{name}")
    public ResponseEntity<?> welcome(@PathVariable("name") String name) {
        HashMap<String, String> data = new HashMap<>();
        data.put("Super person is", name);
        data.put("Have a great one", name);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

	@RequestMapping(value = "/check-wins/", method = RequestMethod.POST)
	public ResponseEntity<?> checkWins(@RequestBody ArrayList<Integer> numbers) {
		return new ResponseEntity<>(gamesController.getMatchGames(numbers), HttpStatus.OK);
    }

    @GetMapping("/games-archive/")
    public ResponseEntity<?> games() {
		return new ResponseEntity<>(gamesController.getAllGames(), HttpStatus.OK);
    }
}
