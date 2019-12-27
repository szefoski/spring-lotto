package com.daniel.lotto;

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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
public class LottoGamesArchiveService {

    private static final String GAMES_ARCHIVE_LINK = "http://www.mbnet.com.pl/dl.txt";
    private long modifyTimeCachedArchive = 0;

    @Cacheable("games-archive")
    public Collection<LottoResultModel> getAllGames() {
        System.out.println("Download games archive");
        return parseGamesArchive(downloadAllGamesResults());
    }

    Collection<LottoResultModel> parseGamesArchive(BufferedReader r) {
        var allGames = new ArrayList<LottoResultModel>();
        final var pattern = Pattern.compile(",");

        try {
            modifyTimeCachedArchive = new URL(GAMES_ARCHIVE_LINK).openConnection().getLastModified();
            String line;
            while ((line = r.readLine()) != null) {
                var parts = line.split(" ");

                var stringDrawNo = parts[0].substring(0, parts[0].length() - 1);
                var drawNo = Integer.valueOf(stringDrawNo);
                var date = parts[1];
                List<Integer> numbers = pattern.splitAsStream(parts[2]).map(Integer::valueOf)
                        .collect(Collectors.toList());

                LottoResultModel game = new LottoResultModel(numbers, date, drawNo);
                allGames.add(game);
            }
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.unmodifiableCollection(allGames);
    }

    BufferedReader downloadAllGamesResults() {
        try {
            return new BufferedReader(
                    new InputStreamReader(new BufferedInputStream(new URL(GAMES_ARCHIVE_LINK).openStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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