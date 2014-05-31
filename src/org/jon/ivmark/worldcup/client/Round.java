package org.jon.ivmark.worldcup.client;

import java.util.ArrayList;
import java.util.List;

public class Round {

    public static final int NUM_ROUNDS = 3;
    public static final int NUM_GAMES = 16;
    private static final int MAX_NUM_ROWS = 75;
    private final List<Play> plays = new ArrayList<Play>(NUM_GAMES);

    private final int round;

    public Round(int round) {
        this.round = round;
        for (int gameNumber = 0; gameNumber < NUM_GAMES; gameNumber++) {
            plays.add(new Play(round, gameNumber));
        }
    }

    public int numPlayedRows() {
        int numRows = 0;
        for (Play play : plays) {
            int numChecked = play.numChecked();
            if (numRows == 0) {
                numRows = numChecked;
            } else if (numChecked > 0) {
                numRows *= numChecked;
            }
        }
        return numRows;
    }

    public boolean isValid() {
        return numPlayedRows() <= MAX_NUM_ROWS && allGamesAreChecked();
    }

    private boolean allGamesAreChecked() {
        for (Play play : plays) {
            if (play.numChecked() == 0) {
                return false;
            }
        }
        return true;
    }

    public String numRowsText() {
        return "Antal rader: " + numPlayedRows();
    }

    public Play getGame(int gameNumber) {
        return plays.get(gameNumber);
    }
}
