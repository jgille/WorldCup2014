package org.jon.ivmark.worldcup.shared.playoffs;

import org.jon.ivmark.worldcup.shared.GameResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayoffPlay implements Serializable {

    private PlayoffGame game;
    private List<Boolean> checked = new ArrayList<>();
    private GameResult gameResult;

    public PlayoffPlay() {
        for (int i = 0; i < 3; i++) {
            checked.add(false);
        }
    }

    public PlayoffGame getGame() {
        return game;
    }

    public void setGame(PlayoffGame game) {
        this.game = game;
    }

    public List<Boolean> getChecked() {
        return checked;
    }

    public void setChecked(List<Boolean> checked) {
        this.checked = checked;
    }

    public int numChecked() {
        int numChecked = 0;
        for (Boolean check : checked) {
            if (check) {
                numChecked++;
            }
        }
        return numChecked;
    }

    @Override
    public String toString() {
        return "PlayoffPlay{" +
                "game=" + game +
                ", checked=" + checked +
                '}';
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public boolean isCorrect() {
        if (gameResult == null || gameResult == GameResult.UNKNOWN) {
            return false;
        }
        return checked.get(gameResult.intValue());
    }
}
