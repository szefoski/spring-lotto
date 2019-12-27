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

    private enum LineSections {
        GAME_NUMBER(0),
        GAME_DATE(1),
        GAME_RESULTS(2);

        private final int value;

        LineSections(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Cacheable(value = "games-archive", unless="#result.size() == 0")
    public Collection<LottoResultModel> getAllGames() {
        try {
            return getLatestGamesArchive();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.unmodifiableCollection(new ArrayList<>());
        }
    }

    private Collection<LottoResultModel> getLatestGamesArchive() throws IOException {
        System.out.println("Download games archive");

        var bufferedFile = new BufferedReader(
                new InputStreamReader(new BufferedInputStream(new URL(GAMES_ARCHIVE_LINK).openStream())));
        var allGames = new ArrayList<LottoResultModel>();
        var pattern = Pattern.compile(",");
        modifyTimeCachedArchive = getModifyTimeOfArchiveOnServer();

        String line;
        while ((line = bufferedFile.readLine()) != null) {
            allGames.add(parseLine(line, pattern));
        }

        bufferedFile.close();
        return Collections.unmodifiableCollection(allGames);
    }

    private LottoResultModel parseLine(String line, Pattern pattern) {
        var parts = line.split(" ");
        var stringDrawNo = parts[LineSections.GAME_NUMBER.getValue()].substring(0, parts[LineSections.GAME_NUMBER.getValue()].length() - 1);
        var drawNo = Integer.valueOf(stringDrawNo);
        var date = parts[LineSections.GAME_DATE.getValue()];
        List<Integer> numbers = pattern.splitAsStream(parts[LineSections.GAME_RESULTS.getValue()]).map(Integer::valueOf)
                .collect(Collectors.toList());

        return new LottoResultModel(numbers, date, drawNo);
    }

    long getModifyTimeOfArchiveOnServer() throws IOException {
        return new URL(GAMES_ARCHIVE_LINK).openConnection().getLastModified();
    }

    long getModifyTimeOfCachedArchive() {
        return modifyTimeCachedArchive;
    }

    @CacheEvict(value = "games-archive", allEntries = true)
    public void evictAllCacheValues() {
        System.out.println("clear archive");
    }
}