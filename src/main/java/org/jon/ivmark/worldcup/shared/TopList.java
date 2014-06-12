package org.jon.ivmark.worldcup.shared;

import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.server.TeamRepository;

import java.io.Serializable;
import java.util.*;

public class TopList implements Serializable {

    private static final int[] POINTS =
            new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10_000, 15_000, 20_000, 30_000, 50_000, 100_000};

    private List<TopListEntry> entries;

    public List<TopListEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TopListEntry> entries) {
        this.entries = entries;
    }

    public static TopList computeTopList(List<Result> results,
                                         Map<String, List<PlaysDto>> playsByTeam) {
        sortResults(results);

        List<TopListEntry> topListEntries = new ArrayList<>();

        for (Map.Entry<String, List<PlaysDto>> e : playsByTeam.entrySet()) {
            List<PlaysDto> plays = e.getValue();
            sortPlays(plays);

            String team = e.getKey();
            TopListEntry topListEntry = new TopListEntry();
            topListEntry.setTeamName(team);
            List<PointsEntry> pointsEntries = new ArrayList<>();
            topListEntry.setEntries(pointsEntries);
            topListEntries.add(topListEntry);

            int i = 0;
            for (PlaysDto playsDto : plays) {
                Result result = results.get(i++);
                if (result.getRoundIndex() != playsDto.roundIndex) {
                    throw new IllegalArgumentException();
                }
                int numCorrect = playsDto.computeNumCorrect(result);
                PointsEntry pointsEntry = new PointsEntry();
                pointsEntry.setNumCorrectGames(numCorrect);
                pointsEntry.setRoundIndex(playsDto.roundIndex);
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
        List<List<PointsEntry>> byRound = groupByRound(topListEntries);

        for (List<PointsEntry> roundTopListEntries : byRound) {
            int numTeams = 0;
            Map<Integer, List<PointsEntry>> byNumCorrect = groupByNumCorrect(roundTopListEntries);

            for (int numCorrectGames = Round.NUM_GAMES; numCorrectGames >= 0; numCorrectGames--) {
                List<PointsEntry> entries = byNumCorrect.get(numCorrectGames);
                if (entries == null) {
                    continue;
                }
                numTeams += entries.size();
                int points = computePoints(numTeams, numCorrectGames);
                for (PointsEntry entry : entries) {
                    entry.setPoints(points);
                }
            }
        }
        TopList topList = new TopList();
        sort(topListEntries);
        int position = 1;
        int prevPoints = -1;
        int count = 0;
        for (TopListEntry entry : topListEntries) {
            if (entry.totalPoints() < prevPoints) {
                position += count;
            }
            entry.setPosition(position);
            prevPoints = entry.totalPoints();
            count++;
        }

        topList.entries = topListEntries;
        return topList;
    }

    static void sort(List<TopListEntry> topListEntries) {
        Collections.sort(topListEntries, new Comparator<TopListEntry>() {
            @Override
            public int compare(TopListEntry entry1, TopListEntry entry2) {
                int pointsDiff = entry2.totalPoints() - entry1.totalPoints();
                if (pointsDiff != 0) {
                    return pointsDiff;
                }

                int correctDiff = entry2.totalNumCorrect() - entry1.totalNumCorrect();
                if (correctDiff != 0) {
                    return correctDiff;
                }
                return entry1.getTeamName().compareTo(entry2.getTeamName());
            }
        });
    }

    static int computePoints(int numTeams, int numCorrectGames) {
        return POINTS[numCorrectGames] / numTeams;
    }

    private static Map<Integer, List<PointsEntry>> groupByNumCorrect(List<PointsEntry> pointsEntries) {
        Map<Integer, List<PointsEntry>> result = new HashMap<>();
        for (PointsEntry pointsEntry : pointsEntries) {
            int numCorrectGames = pointsEntry.getNumCorrectGames();
            List<PointsEntry> entries = result.get(numCorrectGames);
            if (entries == null) {
                entries = new ArrayList<>();
                result.put(numCorrectGames, entries);
            }
            entries.add(pointsEntry);
        }
        return result;
    }

    private static List<List<PointsEntry>> groupByRound(List<TopListEntry> topListEntries) {
        List<List<PointsEntry>> result = new ArrayList<>();
        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            result.add(new ArrayList<PointsEntry>());
        }

        for (TopListEntry entry : topListEntries) {
            for (PointsEntry pointsEntry : entry.getEntries()) {
                result.get(pointsEntry.getRoundIndex()).add(pointsEntry);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "TopList{" +
                "entries=" + entries +
                '}';
    }
}
