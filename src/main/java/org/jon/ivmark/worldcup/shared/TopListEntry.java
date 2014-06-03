package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;
import java.util.List;

public class TopListEntry implements Serializable {

    private String teamName;
    private List<PointsEntry> entries;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<PointsEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PointsEntry> entries) {
        this.entries = entries;
    }

    public int totalPoints() {
        int total = 0;
        for (PointsEntry pe : entries) {
            total += pe.getPoints();
        }
        return total;
    }

    @Override
    public String toString() {
        return "TopListEntry{" +
                "teamName='" + teamName + '\'' +
                ", entries=" + entries +
                '}';
    }
}
