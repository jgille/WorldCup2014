package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffGame;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlay;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlays;

import java.util.List;

public class AllPlaysPanel {
    private final LoginInfo loginInfo;

    private VerticalPanel mainPanel = new VerticalPanel();

    public AllPlaysPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Widget getWidget() {
        loadAllPlays();
        return mainPanel;
    }

    private void loadAllPlays() {
        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);

        playoffService.getAllPlays(new AsyncCallback<List<PlayoffPlays>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(List<PlayoffPlays> allPlays) {
                renderPlays(allPlays);
            }

        });
    }

    private void renderPlays(List<PlayoffPlays> allPlays) {
        for (PlayoffPlays playoffPlays : allPlays) {
            int numCorrect = playoffPlays.numCorrectPlays();

            Label teamLabel = new Label(playoffPlays.getTeamName() + " (" + numCorrect + " rätt)");
            teamLabel.setStyleName("h1");

            mainPanel.add(teamLabel);
            renderPlays("Åttondelsfinaler", playoffPlays.getEightsFinalPlays(), loginInfo.isMaySubmitEightsFinal());
            renderPlays("Kvartsfinaler", playoffPlays.getQuarterFinalPlays(), loginInfo.isMaySubmitQuarterFinal());
            renderPlays("Semifinaler", playoffPlays.getSemiFinalPlays(), loginInfo.isMaySubmitSemiFinal());
            renderPlays("Bronsmatch", playoffPlays.getBronzeGamePlay(), loginInfo.isMaySubmitBronzeMatch());
            renderPlays("Final", playoffPlays.getFinalGamePlay(), loginInfo.isMaySubmitFinal());
        }
    }

    private void renderPlays(String title, List<PlayoffPlay> plays, boolean maySubmit) {
        Label titleLabel = new Label(title);
        titleLabel.setStyleName("playoffTableHeading");
        mainPanel.add(titleLabel);

        Grid grid = new Grid(plays.size() + 1, 4);
        HTMLTable.CellFormatter cellFormatter1X2 = grid.getCellFormatter();
        for (int r = 0; r < grid.getRowCount(); r++) {
            for (int c = 1; c < grid.getColumnCount(); c++) {
                cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
            }
        }

        grid.setText(0, 1, "1");
        grid.setText(0, 2, "X");
        grid.setText(0, 3, "2");

        int rowIndex = 1;
        for (PlayoffPlay play : plays) {
            addRow(play, grid, rowIndex++, !maySubmit);
        }

        mainPanel.add(grid);
    }

    private void addRow(PlayoffPlay playoffPlay, Grid grid, int row, boolean showPlays) {
        int result = playoffPlay.getGameResult().intValue();
        int index = 0;

        PlayoffGame game = playoffPlay.getGame();
        Label label = new Label(row + ". " + game.getHomeTeam() + " - " + game.getAwayTeam());
        grid.setWidget(row, 0, label);

        int col = 1;
        for (Boolean checked : playoffPlay.getChecked()) {
            CheckBox checkBox = new CheckBox();
            if (index++ == result) {
                checkBox.setStyleName("correct");
            }
            if (showPlays) {
                checkBox.setValue(checked);
            }
            checkBox.setEnabled(false);
            grid.setWidget(row, col++, checkBox);
        }
    }

}
