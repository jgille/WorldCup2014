package org.jon.ivmark.worldcup.shared.playoffs;

import org.jon.ivmark.worldcup.shared.GameResult;

import java.io.Serializable;

public class PlayoffResult implements Serializable {

    private PlayoffGame game;
    private GameResult gameResult = GameResult.UNKNOWN;

    public PlayoffGame getGame() {
        return game;
    }

    public void setGame(PlayoffGame game) {
        this.game = game;
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public void setGameResult(GameResult gameResult) {
        this.gameResult = gameResult;
    }
}
