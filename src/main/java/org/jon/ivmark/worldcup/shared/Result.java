package org.jon.ivmark.worldcup.shared;

import org.jon.ivmark.worldcup.client.domain.Round;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {

    private int roundIndex;
    private List<GameResult> results = new ArrayList<>(Round.NUM_GAMES);

    public Result() {
        for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
            results.add(GameResult.UNKNOWN);
        }
    }

    public Result(int roundIndex) {
        this();
        this.roundIndex = roundIndex;
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public void setRoundIndex(int roundIndex) {
        this.roundIndex = roundIndex;
    }

    public void setResult(int gameIndex, GameResult result) {
        results.set(gameIndex, result);
    }

    public List<GameResult> getResults() {
        return results;
    }

    public boolean isComplete() {
        for (GameResult gameResult : results) {
            if (gameResult == GameResult.UNKNOWN) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Result{" +
                "roundIndex=" + roundIndex +
                ", results=" + results +
                '}';
    }
}
