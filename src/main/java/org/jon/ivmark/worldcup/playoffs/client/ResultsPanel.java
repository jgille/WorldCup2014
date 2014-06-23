package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffGame;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResult;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResults;

import java.util.List;

public class ResultsPanel {
    private final LoginInfo loginInfo;

    private VerticalPanel mainPanel = new VerticalPanel();

    public ResultsPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Widget getWidget() {
        renderResults(new PlayoffResults());
        return mainPanel;
    }

    private void renderResults(PlayoffResults playoffResults) {
        mainPanel.add(getSubPanel("Åttondelsfinaler", playoffResults.getEightsFinalResults()));
        mainPanel.add(getSubPanel("Kvartsfinaler", playoffResults.getQuarterFinalResults()));
        mainPanel.add(getSubPanel("Semifinaler", playoffResults.getSemiFinalResults()));
        mainPanel.add(getSubPanel("Bronsmatch", playoffResults.getBronzeGameResult()));
        mainPanel.add(getSubPanel("Final", playoffResults.getFinalGameResult()));
    }

    private Widget getSubPanel(String title, List<PlayoffResult> results) {
        VerticalPanel panel = new VerticalPanel();
        panel.add(new Label(title));

        Grid grid = new Grid(results.size() + 1, 7);
        grid.setText(0, 4, "1");
        grid.setText(0, 5, "X");
        grid.setText(0, 6, "2");

        int row = 1;
        for (PlayoffResult result : results) {
            addRow(result, grid, row++);
        }

        panel.add(grid);

        if (loginInfo.isAdmin()) {
            Button button = new Button("Spara");
            panel.add(button);
        }

        return panel;
    }

    private void addRow(PlayoffResult result, Grid grid, int row) {
        PlayoffGame game = result.getGame();
        int col = 0;
        grid.setText(row, col++, row + ". ");
        TextBox homeTeam = new TextBox();
        homeTeam.setText(game.getHomeTeam());
        homeTeam.setEnabled(loginInfo.isAdmin());
        grid.setWidget(row, col++, homeTeam);
        grid.setText(row, col++, " - ");
        TextBox awayTeam = new TextBox();
        awayTeam.setText(game.getAwayTeam());
        awayTeam.setEnabled(loginInfo.isAdmin());
        grid.setWidget(row, col++, awayTeam);
        for (int i = 0; i < 3; i++) {
            RadioButton radioButton = new RadioButton("playoff-rb-" + game.getGameIndex());
            grid.setWidget(row, col++, radioButton);
        }

    }
}
