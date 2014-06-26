package org.jon.ivmark.worldcup.shared.playoffs;

import org.jon.ivmark.worldcup.shared.GameResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayoffPlays implements Serializable {

    private String teamName;

    private List<PlayoffPlay> plays;

    public PlayoffPlays() {
        this.plays = new ArrayList<>();

        for (int gameIndex = 0; gameIndex < 16; gameIndex++) {
            PlayoffRound round = PlayoffRound.fromGameIndex(gameIndex);
            PlayoffGame playoffGame = new PlayoffGame();
            playoffGame.setGameIndex(gameIndex);
            playoffGame.setRound(round);
            playoffGame.setHomeTeam("Hemmalag");
            playoffGame.setAwayTeam("Bortalag");

            PlayoffPlay playoffPlay = new PlayoffPlay();
            playoffPlay.setGame(playoffGame);

            plays.add(playoffPlay);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int numRows() {
        int rows = 0;
        for (PlayoffPlay play : plays) {
            int numChecked = play.numChecked();
            if (rows == 0) {
                rows = numChecked;
            } else {
                rows *= Math.max(numChecked, 1);
            }
        }
        return rows;
    }

    public List<PlayoffPlay> getPlays() {
        List<PlayoffPlay> playoffPlays = new ArrayList<>(plays);
        Collections.sort(playoffPlays, new Comparator<PlayoffPlay>() {
            @Override
            public int compare(PlayoffPlay o1, PlayoffPlay o2) {
                return o1.getGame().getGameIndex() - o2.getGame().getGameIndex();
            }
        });
        return playoffPlays;
    }

    public List<PlayoffPlay> getEightsFinalPlays() {
        return getPlays().subList(0, 8);
    }

    public List<PlayoffPlay> getQuarterFinalPlays() {
        return getPlays().subList(8, 12);
    }

    public List<PlayoffPlay> getSemiFinalPlays() {
        return getPlays().subList(12, 14);
    }

    public List<PlayoffPlay> getBronzeGamePlay() {
        return getPlays().subList(14, 15);
    }

    public List<PlayoffPlay> getFinalGamePlay() {
        return getPlays().subList(15, 16);
    }

    public void setPlays(List<PlayoffPlay> plays) {
        this.plays = new ArrayList<>(plays);

        for (int i = plays.size(); i < 16; i++) {
            PlayoffPlay playoffPlay = new PlayoffPlay();
            PlayoffGame game = new PlayoffGame();
            game.setGameIndex(i);
            game.setRound(PlayoffRound.fromGameIndex(i));
            game.setHomeTeam("Hemmalag");
            game.setAwayTeam("Bortalag");
            playoffPlay.setGame(game);
            playoffPlay.setGameResult(GameResult.UNKNOWN);
            this.plays.add(playoffPlay);
        }
    }


    public int numCorrectPlays() {
        int numCorrect = 0;

        for (PlayoffPlay playoffPlay : plays) {
            if (playoffPlay.isCorrect()) {
                numCorrect++;
            }

        }
        return numCorrect;
    }

    @Override
    public String toString() {
        return "PlayoffPlays{" +
                "plays=" + plays +
                '}';
    }
}
