package com.daniel.lotto;

import java.time.Duration;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LottoGamesController {

    @Autowired
    LottoGamesService lottoGamesService;

    private long archiveLastUpdateCheck = 0;
    private long modifyTimeDownloadedArchive = 0;
    private static final Duration CACHE_EXPIRE_TIME = Duration.ofMinutes(30);

    public ArrayList<LottoResultModel> getAllGames() {
        if (System.currentTimeMillis() - archiveLastUpdateCheck > CACHE_EXPIRE_TIME.toMillis()) {
            archiveLastUpdateCheck = System.currentTimeMillis();
            if (lottoGamesService.getModifyTimeArchiveOnServer() != modifyTimeDownloadedArchive) {
                modifyTimeDownloadedArchive = lottoGamesService.getModifyTimeArchiveOnServer();
                lottoGamesService.evictAllCacheValues();
            }
        }
        return lottoGamesService.getAllGames();
    }

    public ArrayList<LottoResultModel> getMatchGames(ArrayList<Integer> numbers) {
        return lottoGamesService.getMatchGames(numbers);
    }
}