package com.daniel.lotto;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LottoGamesService {

    @Autowired
    private LottoGamesArchiveService lottoGamesArchiveService;

    private long archiveLastUpdateCheck = 0;
    private static final Duration CACHE_EXPIRE_TIME = Duration.ofMinutes(30);

    public Collection<LottoGameDataModel> getAllGames() {
        var currentTime = System.currentTimeMillis();

        if (currentTime - archiveLastUpdateCheck > CACHE_EXPIRE_TIME.toMillis()) {
            try {
                if (lottoGamesArchiveService.getModifyTimeOfCachedArchive() != lottoGamesArchiveService.getModifyTimeOfArchiveOnServer()) {
                    lottoGamesArchiveService.evictAllCacheValues();
                }
            } catch (IOException e) {
                e.printStackTrace();
                lottoGamesArchiveService.evictAllCacheValues();
            } finally {
                archiveLastUpdateCheck = currentTime;
            }
        }
        return lottoGamesArchiveService.getAllGames();
    }

    public Collection<LottoGameDataModel> getMatchGames(Collection<Integer> numbers) {
        return getAllGames().parallelStream().filter(game -> game.getNumbers().containsAll(numbers)).collect(Collectors.toUnmodifiableList());
    }
}