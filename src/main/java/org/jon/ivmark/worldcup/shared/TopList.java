package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.*;

public class TopList implements Serializable {

    private List<TopListEntry> entries;

    public List<TopListEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TopListEntry> entries) {
        this.entries = entries;
    }

    public static TopList computeTopList(List<Result> results, Map<String, List<PlaysDto>> playsByTeam) {
        sortResults(results);

        List<TopListEntry> topListEntries = new ArrayList<>();

        for (Map.Entry<String, List<PlaysDto>> e : playsByTeam.entrySet()) {
            List<PlaysDto> plays = e.getValue();
            sortPlays(plays);

            String teamName = e.getKey();
            TopListEntry topListEntry = new TopListEntry();
            topListEntry.setTeamName(teamName);
            List<PointsEntry> pointsEntries = new ArrayList<>();
            topListEntry.setEntries(pointsEntries);
            topListEntries.add(topListEntry);

            int i = 0;
            for (PlaysDto playsDto : plays) {
                Result result = results.get(i);
                if (result.getRoundIndex() != playsDto.roundIndex) {
                    throw new IllegalArgumentException();
                }
                int numCorrect = playsDto.computeNumCorrect(result);
                PointsEntry pointsEntry = new PointsEntry();
                pointsEntry.setNumCorrectGames(numCorrect);
                pointsEntries.add(pointsEntry);
            }
        }

        return computeTopList(topListEntries);
    }

    private static void sortResults(List<Result> results) {
        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result r1, Result r2) {
                return r1.getRoundIndex() - r2.getRoundIndex();
            }
        });
    }

    private static void sortPlays(List<PlaysDto> plays) {
        Collections.sort(plays, new Comparator<PlaysDto>() {
            @Override
            public int compare(PlaysDto p1, PlaysDto p2) {
                return p1.roundIndex - p2.roundIndex;
            }
        });
    }

    static TopList computeTopList(List<TopListEntry> topListEntries) {
        // TODO: Implement
        // - Group by round
        // - Compute points for each round
        // - Sum points per team
        // - Sort by total points descending
        return new TopList();
    }
}
