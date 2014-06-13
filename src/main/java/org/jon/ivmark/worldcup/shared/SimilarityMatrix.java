package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SimilarityMatrix implements Serializable {

    private List<String> teams;
    private int[][] similarities;

    public SimilarityMatrix(List<String> teams, int[][] similarities) {
        this.teams = teams;
        this.similarities = similarities;
    }

    public SimilarityMatrix() {
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    public void setSimilarities(int[][] similarities) {
        this.similarities = similarities;
    }

    public List<String> getTeams() {
        return teams;
    }

    public int[][] getSimilarities() {
        return similarities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(teams.toString()).append("\n");
        for (int[] intArr : similarities) {
            sb.append(Arrays.toString(intArr)).append("\n");
        }
        return sb.toString();
    }
}
