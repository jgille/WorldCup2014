package org.jon.ivmark.worldcup.shared;

import org.hamcrest.core.Is;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TopListTest {

    private List<Result> results;

    @Before
    public void init() {
        results = new ArrayList<>();
        for (int roundIndex = 0; roundIndex < 3; roundIndex++) {
            results.add(createResult(roundIndex));
        }
    }

    private Result createResult(int roundIndex) {
        Result result = new Result(roundIndex);
        for (int i = 0; i < 16; i++) {
            result.setResult(i, GameResult.fromInt(i % 3));
        }
        return result;
    }

    @Test
    public void computeTopListFromSingleTeamWithNoPoints() {
        List<PlaysDto> plays = new ArrayList<>();
        plays.add(allPlaysWith(0, GameResult.ONE));
        plays.add(allPlaysWith(1, GameResult.X));
        plays.add(allPlaysWith(2, GameResult.TWO));

        String userId = "test";
        TopList topList = TopList.computeTopList(results, Collections.singletonMap(userId, plays));

        List<TopListEntry> entries = topList.getEntries();
        assertEquals(1, entries.size());

        TopListEntry topListEntry = entries.get(0);
        assertEquals(userId, topListEntry.getTeamName());

        assertEquals(0, topListEntry.totalPoints());

        List<PointsEntry> pointsEntries = topListEntry.getEntries();
        assertEquals(3, pointsEntries.size());

        assertEquals(0, pointsEntries.get(0).getRoundIndex());
        assertEquals(1, pointsEntries.get(1).getRoundIndex());
        assertEquals(2, pointsEntries.get(2).getRoundIndex());

        assertEquals(0, pointsEntries.get(0).getPoints());
        assertEquals(0, pointsEntries.get(1).getPoints());
        assertEquals(0, pointsEntries.get(2).getPoints());

        assertEquals(6, pointsEntries.get(0).getNumCorrectGames());
        assertEquals(5, pointsEntries.get(1).getNumCorrectGames());
        assertEquals(5, pointsEntries.get(2).getNumCorrectGames());
    }

    @Test
    public void computeTopListFromSingleTeamWithFullPoints() {
        List<PlaysDto> plays = getPlaysDtosAllCorrect();

        String userId = "test";
        TopList topList = TopList.computeTopList(results, Collections.singletonMap(userId, plays));

        List<TopListEntry> entries = topList.getEntries();
        assertEquals(1, entries.size());

        TopListEntry topListEntry = entries.get(0);
        assertEquals(userId, topListEntry.getTeamName());

        assertEquals(300_000, topListEntry.totalPoints());

        List<PointsEntry> pointsEntries = topListEntry.getEntries();
        assertEquals(3, pointsEntries.size());

        assertEquals(0, pointsEntries.get(0).getRoundIndex());
        assertEquals(1, pointsEntries.get(1).getRoundIndex());
        assertEquals(2, pointsEntries.get(2).getRoundIndex());

        assertEquals(100_000, pointsEntries.get(0).getPoints());
        assertEquals(100_000, pointsEntries.get(1).getPoints());
        assertEquals(100_000, pointsEntries.get(2).getPoints());

        assertEquals(16, pointsEntries.get(0).getNumCorrectGames());
        assertEquals(16, pointsEntries.get(1).getNumCorrectGames());
        assertEquals(16, pointsEntries.get(2).getNumCorrectGames());
    }

    @Test
    public void computeTopListFromOneTeamWithAll16AndOneTeamWithAll15() {
        List<PlaysDto> correct16 = getPlaysDtosAllCorrect();
        List<PlaysDto> correct15 = getPlaysDtosAllCorrect();
        correct15.get(0).plays[0].checked = new boolean[] {false, false, false};
        correct15.get(1).plays[0].checked = new boolean[] {false, false, false};
        correct15.get(2).plays[0].checked = new boolean[] {false, false, false};

        Map<String, List<PlaysDto>> playsByTeam = new HashMap<>();
        playsByTeam.put("team16", correct16);
        playsByTeam.put("team15", correct15);
        TopList topList = TopList.computeTopList(results, playsByTeam);

        List<TopListEntry> entries = topList.getEntries();
        assertEquals(2, entries.size());

        TopListEntry topListEntry = entries.get(0);
        assertEquals("team16", topListEntry.getTeamName());

        assertEquals(300_000, topListEntry.totalPoints());

        List<PointsEntry> pointsEntries = topListEntry.getEntries();
        assertEquals(3, pointsEntries.size());

        assertEquals(0, pointsEntries.get(0).getRoundIndex());
        assertEquals(1, pointsEntries.get(1).getRoundIndex());
        assertEquals(2, pointsEntries.get(2).getRoundIndex());

        assertEquals(100_000, pointsEntries.get(0).getPoints());
        assertEquals(100_000, pointsEntries.get(1).getPoints());
        assertEquals(100_000, pointsEntries.get(2).getPoints());

        assertEquals(16, pointsEntries.get(0).getNumCorrectGames());
        assertEquals(16, pointsEntries.get(1).getNumCorrectGames());
        assertEquals(16, pointsEntries.get(2).getNumCorrectGames());

        TopListEntry topListEntry2 = entries.get(1);
        assertEquals("team15", topListEntry2.getTeamName());

        assertEquals(75_000, topListEntry2.totalPoints());

        List<PointsEntry> pointsEntries2 = topListEntry2.getEntries();
        assertEquals(3, pointsEntries2.size());

        assertEquals(0, pointsEntries2.get(0).getRoundIndex());
        assertEquals(1, pointsEntries2.get(1).getRoundIndex());
        assertEquals(2, pointsEntries2.get(2).getRoundIndex());

        assertEquals(25_000, pointsEntries2.get(0).getPoints());
        assertEquals(25_000, pointsEntries2.get(1).getPoints());
        assertEquals(25_000, pointsEntries2.get(2).getPoints());

        assertEquals(15, pointsEntries2.get(0).getNumCorrectGames());
        assertEquals(15, pointsEntries2.get(1).getNumCorrectGames());
        assertEquals(15, pointsEntries2.get(2).getNumCorrectGames());
    }

    @Test
    public void sort() {
        TopListEntry topListEntry1 = new TopListEntry();
        PointsEntry pointsEntry1 = new PointsEntry();
        pointsEntry1.setPoints(100);
        List<PointsEntry> pointsEntries1 = Collections.singletonList(pointsEntry1);
        topListEntry1.setEntries(pointsEntries1);

        TopListEntry topListEntry2 = new TopListEntry();
        PointsEntry pointsEntry2 = new PointsEntry();
        pointsEntry2.setPoints(200);
        List<PointsEntry> pointsEntries2 = Collections.singletonList(pointsEntry2);
        topListEntry2.setEntries(pointsEntries2);

        List<TopListEntry> topListEntries = new ArrayList<>(Arrays.asList(topListEntry1, topListEntry2));
        TopList.sort(topListEntries);

        assertThat(topListEntries.get(0), Is.is(topListEntry2));
        assertThat(topListEntries.get(1), Is.is(topListEntry1));
    }

    @Test
    public void sortByTeamName() {
        TopListEntry topListEntry1 = new TopListEntry();
        topListEntry1.setTeamName("Team 1");
        PointsEntry pointsEntry1 = new PointsEntry();
        pointsEntry1.setPoints(100);
        List<PointsEntry> pointsEntries1 = Collections.singletonList(pointsEntry1);
        topListEntry1.setEntries(pointsEntries1);

        TopListEntry topListEntry2 = new TopListEntry();
        topListEntry2.setTeamName("A team");
        PointsEntry pointsEntry2 = new PointsEntry();
        pointsEntry2.setPoints(100);
        List<PointsEntry> pointsEntries2 = Collections.singletonList(pointsEntry2);
        topListEntry2.setEntries(pointsEntries2);

        List<TopListEntry> topListEntries = new ArrayList<>(Arrays.asList(topListEntry1, topListEntry2));
        TopList.sort(topListEntries);

        assertThat(topListEntries.get(0), Is.is(topListEntry2));
        assertThat(topListEntries.get(1), Is.is(topListEntry1));
    }

    @Test
    public void computePoints() {
        assertThat(TopList.computePoints(1, 16), Is.is(100_000));
        assertThat(TopList.computePoints(1, 15), Is.is(50_000));
        assertThat(TopList.computePoints(1, 14), Is.is(30_000));
        assertThat(TopList.computePoints(1, 13), Is.is(20_000));
        assertThat(TopList.computePoints(1, 12), Is.is(15_000));
        assertThat(TopList.computePoints(1, 11), Is.is(10_000));
        for (int points = 0; points < 11; points++) {
            assertThat(TopList.computePoints(1, points), Is.is(0));
        }
    }

    @Test
    public void computePointsWithTies() {
        assertThat(TopList.computePoints(2, 16), Is.is(50_000));
        assertThat(TopList.computePoints(2, 15), Is.is(25_000));
        assertThat(TopList.computePoints(2, 14), Is.is(15_000));
        assertThat(TopList.computePoints(2, 13), Is.is(10_000));
        assertThat(TopList.computePoints(2, 12), Is.is(7_500));
        assertThat(TopList.computePoints(2, 11), Is.is(5_000));
        for (int points = 0; points < 11; points++) {
            assertThat(TopList.computePoints(2, points), Is.is(0));
        }
    }

    private List<PlaysDto> getPlaysDtosAllCorrect() {
        List<PlaysDto> plays = new ArrayList<>();
        int roundIndex = 0;
        for (Result result : results) {
            PlaysDto playsDto = new PlaysDto();
            plays.add(playsDto);
            PlayDto[] playDtos = new PlayDto[Round.NUM_GAMES];
            playsDto.plays = playDtos;
            playsDto.roundIndex = roundIndex++;
            int gameIndex = 0;
            for (GameResult gameResult : result.getResults()) {
                PlayDto playDto = getPlayDto(gameResult);
                playDtos[gameIndex++] = playDto;
            }
        }
        return plays;
    }

    private PlaysDto allPlaysWith(int roundIndex, GameResult gameResult) {
        PlaysDto playsDto = new PlaysDto();
        PlayDto[] playDtos = new PlayDto[16];
        for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
            PlayDto playDto = getPlayDto(gameResult);
            playDtos[gameIndex] = playDto;
        }
        playsDto.roundIndex = roundIndex;
        playsDto.plays = playDtos;
        return playsDto;
    }

    private PlayDto getPlayDto(GameResult gameResult) {
        boolean[] checked = new boolean[]{false, false, false};
        checked[gameResult.intValue()] = true;
        PlayDto playDto = new PlayDto();
        playDto.checked = checked;
        return playDto;
    }
}
