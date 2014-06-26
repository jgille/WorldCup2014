package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlay;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlays;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToplistPanel {

    private VerticalPanel mainPanel = new VerticalPanel();

    public Widget getWidget() {
        loadToplist();
        return mainPanel;
    }

    private void loadToplist() {
        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);

        playoffService.getAllPlays(new AsyncCallback<List<PlayoffPlays>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(List<PlayoffPlays> allPlays) {
                renderToplist(allPlays);
            }

        });
    }

    private void renderToplist(List<PlayoffPlays> allPlays) {
        Collections.sort(allPlays, new Comparator<PlayoffPlays>() {
            @Override
            public int compare(PlayoffPlays o1, PlayoffPlays o2) {
                return o2.numCorrectPlays() - o1.numCorrectPlays();
            }
        });

        Grid grid = new Grid(allPlays.size() + 1, 3);
        grid.setStyleName("topListGrid");

        grid.setText(0, 1, "Lag");
        grid.setText(0, 2, "Antal rätt");

        int position = 1;
        int prevNumCorrect = -1;

        int row = 0;

        for (PlayoffPlays plays : allPlays) {
            int numCorrect = plays.numCorrectPlays();
            if (numCorrect < prevNumCorrect) {
                position = row + 1;
            }

            String posLabel = numCorrect == prevNumCorrect ? "" : position + ".";
            prevNumCorrect = numCorrect;

            grid.setText(row, 0, posLabel);
            grid.setText(row, 1, plays.getTeamName());
            grid.setText(row, 2, numCorrect + "");
            row++;
        }

        mainPanel.add(grid);
    }
}
