package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.Arrays;

public class PlaysDto implements Serializable {

    public int roundIndex;

    public PlayDto[] plays;

    @Override
    public String toString() {
        return "PlaysDto{" +
                "roundIndex=" + roundIndex +
                ", plays=" + Arrays.toString(plays) +
                '}';
    }
}
