package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.Arrays;

public class PlayDto implements Serializable {

    public boolean[] checked;

    @Override
    public String toString() {
        return "PlayDto{" +
                "checked=" + Arrays.toString(checked) +
                '}';
    }

    public static PlayDto fromString(String string) {
        PlayDto playDto = new PlayDto();
        playDto.checked = new boolean[3];
        playDto.checked[0] = string.contains("1");
        playDto.checked[1] = string.contains("X");
        playDto.checked[2] = string.contains("2");
        return playDto;
    }

    public String asString() {
        String string = "";
        string += checked[0] ? "1" : "";
        string += checked[1] ? "X" : "";
        string += checked[2] ? "2" : "";
        return string;
    }

    public boolean isCorrect(GameResult result) {
        return result != GameResult.UNKNOWN && checked[result.intValue()];
    }

    public int difference(PlayDto otherPlay) {
        int difference = 0;
        for (int i = 0; i < 3; i++) {
            if (checked[i] && !otherPlay.checked[i]) {
                difference++;
            }
        }
        return difference;
    }

    private int numChecked() {
        return asString().length();
    }

    public double similarityWith(PlayDto otherPlay) {
        int maxNumChecked = Math.max(numChecked(), otherPlay.numChecked());

        int same = 0;
        for (int i = 0; i < 3; i++) {
            if (checked[i] && otherPlay.checked[i]) {
                same++;
            }
        }
        return (1d * same) / maxNumChecked;
    }
}
