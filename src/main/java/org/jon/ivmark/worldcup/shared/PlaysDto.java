package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class PlaysDto implements Serializable {

    public String userId;

    public int roundIndex;

    public PlayDto[] plays;

    @Override
    public String toString() {
        return "PlaysDto{" +
                "userId='" + userId + '\'' +
                ", roundIndex=" + roundIndex +
                ", plays=" + Arrays.toString(plays) +
                '}';
    }

    public int computeNumCorrect(Result result) {
        int numCorrect = 0;
        int i = 0;
        List<GameResult> results = result.getResults();
        for (PlayDto playDto : plays) {
            numCorrect += playDto.isCorrect(results.get(i++)) ? 1 : 0;
        }
        return numCorrect;
    }

    public double calculateSimilarityWith(PlaysDto other) {
        double similarity = 0;

        for (int gameIndex = 0; gameIndex < plays.length; gameIndex++) {
            PlayDto play = plays[gameIndex];
            PlayDto otherPlay = other.plays[gameIndex];
            similarity += play.similarityWith(otherPlay);
        }

        return similarity;
    }
}
