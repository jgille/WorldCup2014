package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.shared.GameResult;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffGame;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResult;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResults;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffRound;

import java.util.ArrayList;
import java.util.List;

public class ResultsPanel {

    private static class ResultRow {
        private int gameIndex;
        private TextBox homeTeam;
        private TextBox awayTeam;
        private List<RadioButton> radioButtons;

        ResultRow(PlayoffResult playoffResult, boolean editable) {
            PlayoffGame game = playoffResult.getGame();
            this.gameIndex = game.getGameIndex();
            this.homeTeam = new TextBox();
            this.homeTeam.setEnabled(editable);
            this.homeTeam.setText(game.getHomeTeam());

            this.awayTeam = new TextBox();
            this.awayTeam.setEnabled(editable);
            this.awayTeam.setText(game.getAwayTeam());

            this.radioButtons = new ArrayList<>(3);
            for (int i = 0; i < 3; i++) {
                RadioButton radioButton = new RadioButton("playoff-rb-" + game.getGameIndex());
                radioButton.setEnabled(editable);
                if (playoffResult.getGameResult().intValue() == i) {
                    radioButton.setValue(true);
                }
                radioButtons.add(radioButton);
            }
        }

        PlayoffResult asResult() {
            PlayoffGame playoffGame = new PlayoffGame();
            playoffGame.setGameIndex(gameIndex);
            playoffGame.setHomeTeam(homeTeam.getText());
            playoffGame.setAwayTeam(awayTeam.getText());
            playoffGame.setRound(PlayoffRound.fromGameIndex(gameIndex));
            PlayoffResult result = new PlayoffResult();
            result.setGame(playoffGame);
            result.setGameResult(getGameResult());
            return result;
        }

        private GameResult getGameResult() {
            if (radioButtons.get(0).getValue()) {
                return GameResult.ONE;
            } else if (radioButtons.get(1).getValue()) {
                return GameResult.X;
            } else if (radioButtons.get(2).getValue()) {
                return GameResult.TWO;
            } else {
                return GameResult.UNKNOWN;
            }
        }
    }

    private final LoginInfo loginInfo;

    private VerticalPanel mainPanel = new VerticalPanel();

    public ResultsPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Widget getWidget() {
        loadResults();
        return mainPanel;
    }

    private void loadResults() {
        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);
        playoffService.getResults(new AsyncCallback<PlayoffResults>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(PlayoffResults results) {
                renderResults(results);
            }
        });
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
        Label titleLabel = new Label(title);
        titleLabel.setStyleName("playoffTableHeading");
        panel.add(titleLabel);

        Grid grid = new Grid(results.size() + 1, 7);
        grid.setText(0, 4, "1");
        grid.setText(0, 5, "X");
        grid.setText(0, 6, "2");

        final List<ResultRow> rows = new ArrayList<>(results.size());

        int row = 1;
        for (PlayoffResult result : results) {
            rows.add(addRow(result, grid, row++));
        }

        panel.add(grid);

        if (loginInfo.isAdmin()) {
            Button button = new Button("Spara");
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    save(rows);
                }
            });
            panel.add(button);
        }

        return panel;
    }

    private void save(List<ResultRow> rows) {
        List<PlayoffResult> playoffResults = new ArrayList<>(rows.size());

        for (ResultRow resultRow : rows) {
            playoffResults.add(resultRow.asResult());
        }

        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);
        playoffService.saveResults(playoffResults, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
            }
        });

    }

    private ResultRow addRow(PlayoffResult result, Grid grid, int row) {
        ResultRow resultRow = new ResultRow(result, loginInfo.isAdmin());

        int col = 0;
        grid.setText(row, col++, row + ". ");

        TextBox homeTeam = resultRow.homeTeam;
        homeTeam.setEnabled(loginInfo.isAdmin());
        grid.setWidget(row, col++, homeTeam);

        grid.setText(row, col++, " - ");

        TextBox awayTeam = resultRow.awayTeam;
        awayTeam.setEnabled(loginInfo.isAdmin());
        grid.setWidget(row, col++, awayTeam);

        for (RadioButton radioButton : resultRow.radioButtons) {
            grid.setWidget(row, col++, radioButton);
        }

        return resultRow;
    }
}
