package org.jon.ivmark.worldcup.shared.playoffs;

import java.io.Serializable;

public class PlayoffGame implements Serializable {

    private PlayoffRound round;
    private int gameIndex;
    private String homeTeam;
    private String awayTeam;

    public PlayoffRound getRound() {
        return round;
    }

    public void setRound(PlayoffRound round) {
        this.round = round;
    }

    public int getGameIndex() {
        return gameIndex;
    }

    public void setGameIndex(int gameIndex) {
        this.gameIndex = gameIndex;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    @Override
    public String toString() {
        return "PlayoffGame{" +
                "round=" + round +
                ", gameIndex=" + gameIndex +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                '}';
    }
}
