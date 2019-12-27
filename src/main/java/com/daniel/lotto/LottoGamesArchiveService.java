package com.daniel.lotto;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LottoGamesArchiveService {

    private static final String GAMES_ARCHIVE_LINK = "http://www.mbnet.com.pl/dl.txt";
    private long modifyTimeCachedArchive = 0;

    @Cacheable(value = "games-archive", unless="#result.size() == 0")
    public Collection<LottoResultModel> getAllGames() {
        try {
            return getLatestGamesArchive();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    Collection<LottoResultModel> getLatestGamesArchive() throws IOException {
        System.out.println("Download games archive");
        var r = new BufferedReader(
                new InputStreamReader(new BufferedInputStream(new URL(GAMES_ARCHIVE_LINK).openStream())));
        var allGames = new ArrayList<LottoResultModel>();
        final var pattern = Pattern.compile(",");

        modifyTimeCachedArchive = new URL(GAMES_ARCHIVE_LINK).openConnection().getLastModified();
        String line;
        while ((line = r.readLine()) != null) {
            var parts = line.split(" ");

            var stringDrawNo = parts[0].substring(0, parts[0].length() - 1);
            var drawNo = Integer.valueOf(stringDrawNo);
            var date = parts[1];
            List<Integer> numbers = pattern.splitAsStream(parts[2]).map(Integer::valueOf)
                    .collect(Collectors.toList());

            var game = new LottoResultModel(numbers, date, drawNo);
            allGames.add(game);
        }
        r.close();

        return Collections.unmodifiableCollection(allGames);
    }

    long getModifyTimeOfArchiveOnServer() {
        long time = 0;

        try {
            var url = new URL(GAMES_ARCHIVE_LINK);
            time = url.openConnection().getLastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

    long getModifyTimeOfCachedArchive() {
        return modifyTimeCachedArchive;
    }

    @CacheEvict(value = "games-archive", allEntries = true)
    public void evictAllCacheValues() {
        System.out.println("clear archive");
    }
}