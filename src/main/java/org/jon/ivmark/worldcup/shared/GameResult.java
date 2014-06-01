package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;

public enum GameResult implements Serializable {
    ONE(0), X(1), TWO(2), UNKNOWN(-1);

    private final int intValue;

    GameResult(int intValue) {
        this.intValue = intValue;
    }

    public int intValue() {
        return intValue;
    }

    public static GameResult fromInt(int intValue) {
        for (GameResult gameResult : GameResult.values()) {
            if (gameResult.intValue == intValue) {
                return gameResult;
            }
        }
        throw new IllegalArgumentException();
    }
}
