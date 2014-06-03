package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;

public class PointsEntry implements Serializable {

    private int roundIndex;
    private int numCorrectGames;
    private int points;

    public int getRoundIndex() {
        return roundIndex;
    }

    public void setRoundIndex(int roundIndex) {
        this.roundIndex = roundIndex;
    }

    public int getNumCorrectGames() {
        return numCorrectGames;
    }

    public void setNumCorrectGames(int numCorrectGames) {
        this.numCorrectGames = numCorrectGames;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PointsEntry{" +
                "roundIndex=" + roundIndex +
                ", numCorrectGames=" + numCorrectGames +
                ", points=" + points +
                '}';
    }
}
