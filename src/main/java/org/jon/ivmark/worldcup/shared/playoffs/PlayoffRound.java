package org.jon.ivmark.worldcup.shared.playoffs;

public enum PlayoffRound {
    EIGHTS_FINAL, QUARTER_FINAL, SEMI_FINAL, BRONZE_GAME, FINAL;

    public static PlayoffRound fromGameIndex(int gameIndex) {
        if (gameIndex < 8) {
            return EIGHTS_FINAL;
        } else if (gameIndex < 12) {
            return QUARTER_FINAL;
        } else if (gameIndex < 14) {
            return SEMI_FINAL;
        } else if (gameIndex < 15) {
            return BRONZE_GAME;
        } else {
            return FINAL;
        }

    }
}
