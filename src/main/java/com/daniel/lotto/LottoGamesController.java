package com.daniel.lotto;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LottoGamesController {

    @Autowired
    LottoGamesService lottoGamesService;

    long archiveLastUpdateCheck = 0;
    long archiveModifyTimeDownloaded = 0;

    public ArrayList<LottoResultModel> getAllGames() {
        if (System.currentTimeMillis() - archiveLastUpdateCheck > 1000 * 20) {
            archiveLastUpdateCheck = System.currentTimeMillis();
            if (lottoGamesService.getDateGamesArchiveOnServer() != archiveModifyTimeDownloaded) {
                archiveModifyTimeDownloaded = lottoGamesService.getDateGamesArchiveOnServer();
                lottoGamesService.evictAllCacheValues();
            }
        }
        return lottoGamesService.getAllGames();
    }

    public ArrayList<LottoResultModel> getMatchGames(ArrayList<Integer> numbers) {
        return lottoGamesService.getMatchGames(numbers);
    }
}