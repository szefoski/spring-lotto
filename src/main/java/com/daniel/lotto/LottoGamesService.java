package com.daniel.lotto;

import java.time.Duration;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LottoGamesService {

    @Autowired
    LottoGamesArchiveService lottoGamesArchiveService;

    private long archiveLastUpdateCheck = 0;
    private long modifyTimeDownloadedArchive = 0;
    private static final Duration CACHE_EXPIRE_TIME = Duration.ofMinutes(30);

    public ArrayList<LottoResultModel> getAllGames() {
        if (System.currentTimeMillis() - archiveLastUpdateCheck > CACHE_EXPIRE_TIME.toMillis()) {
            archiveLastUpdateCheck = System.currentTimeMillis();
            if (lottoGamesArchiveService.getModifyTimeArchiveOnServer() != modifyTimeDownloadedArchive) {
                modifyTimeDownloadedArchive = lottoGamesArchiveService.getModifyTimeArchiveOnServer();
                lottoGamesArchiveService.evictAllCacheValues();
            }
        }
        return lottoGamesArchiveService.getAllGames();
    }

    public ArrayList<LottoResultModel> getMatchGames(ArrayList<Integer> numbers) {
        return lottoGamesArchiveService.getMatchGames(numbers);
    }
}