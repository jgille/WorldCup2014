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

    public PlaySimilarity calculateSimilarityWith(PlaysDto other) {
        PlaysDto play1;
        PlaysDto play2;
        int maxNumChecked;

        int numChecked = numChecked();
        int otherNumChecked = other.numChecked();
        if (numChecked > otherNumChecked) {
            maxNumChecked = numChecked;
            play1 = this;
            play2 = other;
        } else if (numChecked < otherNumChecked || userId.compareTo(other.userId) < 0) {
            maxNumChecked = otherNumChecked;
            play1 = other;
            play2 = this;
        } else {
            maxNumChecked = numChecked;
            play1 = this;
            play2 = other;
        }

        int numDifferent = 0;

        for (int gameIndex = 0; gameIndex < plays.length; gameIndex++) {
            PlayDto play = play1.plays[gameIndex];
            PlayDto otherPlay = play2.plays[gameIndex];
            numDifferent += play.difference(otherPlay);
        }

        return new PlaySimilarity(numDifferent, maxNumChecked);
    }

    int numChecked() {
        int result = 0;
        for (PlayDto playDto : plays) {
            result += numChecked(playDto.checked);
        }
        return result;
    }

    private int numChecked(boolean[] checked) {
        int result = 0;
        for (boolean b : checked) {
            if (b) {
                result++;
            }
        }
        return result;
    }
}
