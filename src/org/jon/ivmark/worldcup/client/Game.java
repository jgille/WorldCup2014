package org.jon.ivmark.worldcup.client;

import org.jon.ivmark.worldcup.client.domain.GameId;

public class Game {

    private final GameId gameId;
    private final String homeTeam;
    private final String awayTeam;

    public Game(GameId gameId, String homeTeam, String awayTeam) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public GameId getGameId() {
        return gameId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String label() {
        return (gameId.getGameIndex() + 1) + ". " + homeTeam + " - " + awayTeam;
    }
}
