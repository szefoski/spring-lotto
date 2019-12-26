package com.daniel.lotto;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

@Service
public class LottoGamesService {

    private static final String GAMES_ARCHIVE_LINK = "http://www.mbnet.com.pl/dl.txt";

    @Autowired
    CacheManager cacheManager;

    @Cacheable("games-archive")
    public ArrayList<LottoResultModel> getAllGames() {
        System.out.println("Download games archive");
        return parseGamesArchive(downloadAllGamesResults());
    }

    ArrayList<LottoResultModel> parseGamesArchive(BufferedReader r) {
        var allGames = new ArrayList<LottoResultModel>();
        final var pattern = Pattern.compile(",");

        try {
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

        return allGames;
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

    public long getModifyTimeArchiveOnServer() {
        long time = 0;

        try {
            var url = new URL(GAMES_ARCHIVE_LINK);
            time = url.openConnection().getLastModified();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

    @CacheEvict(value = "games-archive", allEntries = true)
    public void evictAllCacheValues() {
        System.out.println("clear archive");
    }

    public ArrayList<LottoResultModel> getMatchGames(ArrayList<Integer> numbers) {
        var wonGames = new ArrayList<LottoResultModel>();

        for (var game : getAllGames()) {
            if (game.getNumbers().containsAll(numbers)) {
                wonGames.add(game);
            }
        }

        return wonGames;
    }
}