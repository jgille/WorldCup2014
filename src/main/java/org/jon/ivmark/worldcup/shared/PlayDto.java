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
}
