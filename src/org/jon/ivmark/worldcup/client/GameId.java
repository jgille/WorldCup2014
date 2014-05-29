package org.jon.ivmark.worldcup.client;

public class GameId {

    private final int roundIndex;
    private final int gameIndex;

    public GameId(int roundIndex, int gameIndex) {
        this.roundIndex = roundIndex;
        this.gameIndex = gameIndex;
    }

    public int getRoundIndex() {
        return roundIndex;
    }

    public int getGameIndex() {
        return gameIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameId gameId = (GameId) o;

        return gameIndex == gameId.gameIndex && roundIndex == gameId.roundIndex;

    }

    @Override
    public int hashCode() {
        int result = roundIndex;
        result = 31 * result + gameIndex;
        return result;
    }
}
