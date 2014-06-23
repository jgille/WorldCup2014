package org.jon.ivmark.worldcup.shared.playoffs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayoffResults implements Serializable {

    private List<PlayoffResult> results;

    public PlayoffResults() {
        this.results = new ArrayList<>();

        for (int gameIndex = 0; gameIndex < 16; gameIndex++) {
            PlayoffRound round = PlayoffRound.fromGameIndex(gameIndex);
            PlayoffGame playoffGame = new PlayoffGame();
            playoffGame.setGameIndex(gameIndex);
            playoffGame.setRound(round);
            playoffGame.setHomeTeam("Hemmalag");
            playoffGame.setAwayTeam("Bortalag");
            PlayoffResult playoffResult = new PlayoffResult();
            playoffResult.setGame(playoffGame);

            results.add(playoffResult);
        }
    }

    public List<PlayoffResult> getResults() {
        ArrayList<PlayoffResult> playoffResults = new ArrayList<>(results);
        Collections.sort(playoffResults, new Comparator<PlayoffResult>() {
            @Override
            public int compare(PlayoffResult o1, PlayoffResult o2) {
                return o1.getGame().getGameIndex() - o2.getGame().getGameIndex();
            }
        });
        return playoffResults;
    }

    public List<PlayoffResult> getEightsFinalResults() {
        return getResults().subList(0, 8);
    }

    public List<PlayoffResult> getQuarterFinalResults() {
        return getResults().subList(8, 12);
    }

    public List<PlayoffResult> getSemiFinalResults() {
        return getResults().subList(12, 14);
    }

    public List<PlayoffResult> getBronzeGameResult() {
        return getResults().subList(14, 15);
    }

    public List<PlayoffResult> getFinalGameResult() {
        return getResults().subList(15, 16);
    }

    public void setResults(List<PlayoffResult> results) {
        this.results = results;
    }
}
