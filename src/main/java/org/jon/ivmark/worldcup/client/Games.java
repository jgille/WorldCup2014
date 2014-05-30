package org.jon.ivmark.worldcup.client;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Games {

    private final Map<GameId, Game> allGames = new LinkedHashMap<GameId, Game>();

    private static final Games GAMES = new Games();

    public static Games allGames() {
        return GAMES;
    }

    private Games() {
        int roundIndex = 0;
        int gameIndex = 0;

        addGame(roundIndex, gameIndex++, "Brasilien", "Kroatien");
        addGame(roundIndex, gameIndex++, "Mexiko", "Kamerun");
        addGame(roundIndex, gameIndex++, "Spanien", "Nederländerna");
        addGame(roundIndex, gameIndex++, "Chile", "Australien");
        addGame(roundIndex, gameIndex++, "Colombia", "Grekland");
        addGame(roundIndex, gameIndex++, "Elfenbenskusten", "Japan");
        addGame(roundIndex, gameIndex++, "Uruguay", "Costa Rica");
        addGame(roundIndex, gameIndex++, "England", "Italien");
        addGame(roundIndex, gameIndex++, "Schweiz", "Ecuador");
        addGame(roundIndex, gameIndex++, "Frankrike", "Honduras");
        addGame(roundIndex, gameIndex++, "Argentina", "Bosnien");
        addGame(roundIndex, gameIndex++, "Iran", "Nigeria");
        addGame(roundIndex, gameIndex++, "Tyskland", "Portugal");
        addGame(roundIndex, gameIndex++, "Ghana", "USA");
        addGame(roundIndex, gameIndex++, "Belgien", "Algeriet");
        addGame(roundIndex, gameIndex, "Ryssland", "Sydkorea");

        roundIndex++;
        gameIndex = 0;

        addGame(roundIndex, gameIndex++, "Brasilien", "Mexiko");
        addGame(roundIndex, gameIndex++, "Kamerun", "Kroatien");
        addGame(roundIndex, gameIndex++, "Spanien", "Chile");
        addGame(roundIndex, gameIndex++, "Australien", "Nederländerna");
        addGame(roundIndex, gameIndex++, "Colombia", "Elfenbenskusten");
        addGame(roundIndex, gameIndex++, "Japan", "Grekland");
        addGame(roundIndex, gameIndex++, "Uruguay", "England");
        addGame(roundIndex, gameIndex++, "Italien", "Costa Rica");
        addGame(roundIndex, gameIndex++, "Schweiz", "Frankrike");
        addGame(roundIndex, gameIndex++, "Honduras", "Ecuador");
        addGame(roundIndex, gameIndex++, "Argentina", "Iran");
        addGame(roundIndex, gameIndex++, "Nigeria", "Bosnien");
        addGame(roundIndex, gameIndex++, "Tyskland", "Ghana");
        addGame(roundIndex, gameIndex++,  "USA", "Portugal");
        addGame(roundIndex, gameIndex++, "Belgien", "Ryssland");
        addGame(roundIndex, gameIndex, "Sykorea", "Algeriet");

        roundIndex++;
        gameIndex = 0;

        addGame(roundIndex, gameIndex++, "Kamerun", "Brasilien");
        addGame(roundIndex, gameIndex++, "Kroatien", "Mexiko");
        addGame(roundIndex, gameIndex++, "Australien", "Spanien");
        addGame(roundIndex, gameIndex++, "Nederländerna", "Chile");
        addGame(roundIndex, gameIndex++, "Japan", "Colombia");
        addGame(roundIndex, gameIndex++, "Grekland", "Elfenbenskusten");
        addGame(roundIndex, gameIndex++, "Italien", "Uruguay");
        addGame(roundIndex, gameIndex++, "Costa Rica", "England");
        addGame(roundIndex, gameIndex++, "Honduras", "Schweiz");
        addGame(roundIndex, gameIndex++, "Ecuador", "Frankrike");
        addGame(roundIndex, gameIndex++, "Nigeria", "Argentina");
        addGame(roundIndex, gameIndex++, "Bosnien", "Iran");
        addGame(roundIndex, gameIndex++,  "USA", "Tyskland");
        addGame(roundIndex, gameIndex++, "Portugal", "Ghana");
        addGame(roundIndex, gameIndex++, "Sykorea", "Belgien");
        addGame(roundIndex, gameIndex, "Algeriet", "Ryssland");
    }

    private void addGame(int roundIndex, int gameIndex, String homeTeam, String awayTeam) {
        GameId gameId = new GameId(roundIndex, gameIndex);
        Game game = new Game(gameId, homeTeam, awayTeam);
        allGames.put(gameId, game);
    }

    public Game get(GameId gameId) {
        return allGames.get(gameId);
    }
}
