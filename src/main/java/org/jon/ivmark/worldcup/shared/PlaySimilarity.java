package org.jon.ivmark.worldcup.shared;

public class PlaySimilarity {

    private final int numDifferent;
    private final int numChecked;

    public PlaySimilarity(int numDifferent, int numChecked) {
        this.numDifferent = numDifferent;
        this.numChecked = numChecked;
    }

    public int getNumDifferent() {
        return numDifferent;
    }

    public int getNumChecked() {
        return numChecked;
    }

    public PlaySimilarity add(PlaySimilarity other) {
        return new PlaySimilarity(numDifferent + other.numDifferent, numChecked + other.numChecked);
    }

    public int similarityPercentage() {
        return 100 - (int) Math.round((100d * numDifferent) / numChecked);
    }
}
