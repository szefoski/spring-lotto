package com.daniel.lotto;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LottoGamesService {

    @Autowired
    LottoGamesArchiveService lottoGamesArchiveService;

    private long archiveLastUpdateCheck = 0;
    private static final Duration CACHE_EXPIRE_TIME = Duration.ofMinutes(30);

    public Collection<LottoResultModel> getAllGames() {
        var currentTime = System.currentTimeMillis();

        if (currentTime - archiveLastUpdateCheck > CACHE_EXPIRE_TIME.toMillis()) {
            archiveLastUpdateCheck = currentTime;
            if (lottoGamesArchiveService.getModifyTimeOfArchiveOnServer() != lottoGamesArchiveService.getModifyTimeOfCachedArchive()) {
                lottoGamesArchiveService.evictAllCacheValues();
            }
        }
        return lottoGamesArchiveService.getAllGames();
    }

    public Collection<LottoResultModel> getMatchGames(Collection<Integer> numbers) {
        var wonGames = new ArrayList<LottoResultModel>();

        for (var game : getAllGames()) {
            if (game.getNumbers().containsAll(numbers)) {
                wonGames.add(game);
            }
        }

        return wonGames;
    }
}