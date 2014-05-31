package org.jon.ivmark.worldcup.client.domain;

import org.jon.ivmark.worldcup.shared.PlayDto;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.ArrayList;
import java.util.List;

public class Round {

    public static final int NUM_ROUNDS = 3;
    public static final int NUM_GAMES = 16;
    private static final int MAX_NUM_ROWS = 72;
    private final List<Play> plays = new ArrayList<Play>(NUM_GAMES);

    private final int round;

    public Round(int round) {
        this.round = round;
        for (int gameNumber = 0; gameNumber < NUM_GAMES; gameNumber++) {
            plays.add(new Play(round, gameNumber));
        }
    }

    public PlaysDto asDto() {
        PlaysDto playsDto = new PlaysDto();
        playsDto.roundIndex = round;
        playsDto.plays = new PlayDto[plays.size()];
        int i = 0;
        for (Play play : plays) {
            playsDto.plays[i++] = play.asDto();
        }

        return playsDto;
    }

    public int numRows() {
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
        return numRows() <= MAX_NUM_ROWS && allGamesAreChecked();
    }

    private boolean allGamesAreChecked() {
        for (Play play : plays) {
            if (play.numChecked() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean tooManyRows() {
        return numRows() > MAX_NUM_ROWS;
    }

    public String numRowsText() {
        return "Antal rader: " + numRows() + " (max " + MAX_NUM_ROWS + ")";
    }

    public Play getPlay(int gameNumber) {
        return plays.get(gameNumber);
    }

    public static Round fromPlaysDto(PlaysDto playsDto) {
        Round round = new Round(playsDto.roundIndex);
        int i = 0;
        for (PlayDto playDto : playsDto.plays) {
            Play play = round.getPlay(i++);
            play.setOne(playDto.checked[0]);
            play.setX(playDto.checked[1]);
            play.setTwo(playDto.checked[2]);

        }
        return round;
    }
}
