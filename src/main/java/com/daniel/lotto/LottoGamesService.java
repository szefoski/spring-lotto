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

import org.springframework.stereotype.Service;

@Service
public class LottoGamesService {

    private ArrayList<LottoResultModel> allGamesResultsCache;

    private ArrayList<LottoResultModel> downloadAllGamesResults() {
        var allGames = new ArrayList<LottoResultModel>();

        try {
            var r = new BufferedReader(new InputStreamReader(
                    new BufferedInputStream(new URL("http://www.mbnet.com.pl/dl.txt").openStream())));

            var pattern = Pattern.compile(",");

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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return allGames;
    }

    public ArrayList<LottoResultModel> getAllGames() {
        if (allGamesResultsCache == null) {
            allGamesResultsCache = downloadAllGamesResults();
        }

        return allGamesResultsCache;
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