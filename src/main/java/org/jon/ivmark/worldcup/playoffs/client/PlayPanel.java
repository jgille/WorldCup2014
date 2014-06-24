package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffGame;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlay;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlays;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffRound;

import java.util.ArrayList;
import java.util.List;

public class PlayPanel {

    private static final int MAX_NUM_ROWS = 72;

    public PlayPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    private static class PlayRow {
        private int gameIndex;
        private Label label;
        private List<CheckBox> checkBoxes;

        public int numChecked() {
            int numChecked = 0;
            for (CheckBox checkBox : checkBoxes) {
                if (checkBox.getValue()) {
                    numChecked++;
                }
            }
            return numChecked;
        }

        public PlayoffPlay asPlay() {
            PlayoffPlay playoffPlay = new PlayoffPlay();
            PlayoffGame game = new PlayoffGame();
            game.setGameIndex(gameIndex);
            playoffPlay.setGame(game);
            List<Boolean> checked = new ArrayList<>(3);
            for (CheckBox checkBox : checkBoxes) {
                checked.add(checkBox.getValue());
            }
            playoffPlay.setChecked(checked);
            return playoffPlay;
        }

    }

    private List<PlayRow> rows = new ArrayList<>();

    private List<Button> saveButtons = new ArrayList<>();

    private Label numRowsLabel = new Label("");

    private VerticalPanel mainPanel = new VerticalPanel();

    private final LoginInfo loginInfo;

    public Widget getWidget() {
        loadPlays();
        return mainPanel;
    }

    private int numCheckedRows() {
        int numRows = 0;
        for (PlayRow row : rows) {
            int numChecked = row.numChecked();
            if (numRows == 0) {
                numRows = numChecked;
            } else {
                numRows *= Math.max(numChecked, 1);
            }

        }
        return numRows;
    }

    private void loadPlays() {
        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);
        playoffService.getPlays(new AsyncCallback<PlayoffPlays>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(PlayoffPlays plays) {
                renderPlays(plays);
            }
        });
    }

    private void renderPlays(PlayoffPlays plays) {
        rows = new ArrayList<>();
        mainPanel.add(numRowsLabel);
        mainPanel.add(getSubPanel("Åttondelsfinaler", plays.getEightsFinalResults(), PlayoffRound.EIGHTS_FINAL));
        mainPanel.add(getSubPanel("Kvartsfinaler", plays.getQuarterFinalResults(), PlayoffRound.QUARTER_FINAL));
        mainPanel.add(getSubPanel("Semifinaler", plays.getSemiFinalResults(), PlayoffRound.SEMI_FINAL));
        mainPanel.add(getSubPanel("Bronsmatch", plays.getBronzeGameResult(), PlayoffRound.BRONZE_GAME));
        mainPanel.add(getSubPanel("Final", plays.getFinalGameResult(), PlayoffRound.FINAL));
        numRowsLabel.setText(getNumRowsText());
    }

    private String getNumRowsText() {
        return "Antal rader: " + numCheckedRows() + " (max 72 totalt för alla 16 matcherna)";
    }

    private Widget getSubPanel(String title, List<PlayoffPlay> plays, final PlayoffRound round) {
        int numCheckedRows = numCheckedRows();
        final VerticalPanel panel = new VerticalPanel();
        Label titleLabel = new Label(title);
        titleLabel.setStyleName("playoffTableHeading");
        panel.add(titleLabel);

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

        boolean enabled = numCheckedRows <= MAX_NUM_ROWS && maySubmitRound(round);

        int rowIndex = 1;
        final List<PlayRow> rowsForRound = new ArrayList<>();
        for (PlayoffPlay play : plays) {
            PlayRow row = addRow(play, grid, rowIndex++, enabled);
            rows.add(row);
            rowsForRound.add(row);
        }

        panel.add(grid);

        final Button button = new Button("Spara");
        saveButtons.add(button);
        button.setEnabled(maySubmitRound(round, rowsForRound));
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (maySubmitRound(round)) {
                    button.setText("Sparar...");
                    savePlays(button, rowsForRound);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        panel.add(button);

        return panel;
    }

    private boolean maySubmitRound(PlayoffRound round, List<PlayRow> rowsForRound) {
        if (!maySubmitRound(round)) {
            return false;
        }
        return allGamesPlayed(rowsForRound);
    }

    private boolean allGamesPlayed(List<PlayRow> rowsForRound) {
        for (PlayRow row : rowsForRound) {
            if (row.numChecked() == 0) {
                return false;
            }
        }
        return true;
    }

    private void savePlays(final Button button, List<PlayRow> playRows) {
        PlayoffServiceAsync playoffService = GWT.create(PlayoffService.class);

        List<PlayoffPlay> plays = new ArrayList<>(playRows.size());
        for (PlayRow row : playRows) {
            plays.add(row.asPlay());
        }
        playoffService.savePlays(plays, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Void result) {
                button.setText("Spara");
                button.setEnabled(false);

            }
        });
    }

    private boolean maySubmitRound(PlayoffRound round) {
        switch (round) {
            case EIGHTS_FINAL:
                return loginInfo.isMaySubmitEightsFinal();
            case QUARTER_FINAL:
                return loginInfo.isMaySubmitQuarterFinal();
            case SEMI_FINAL:
                return loginInfo.isMaySubmitSemiFinal();
            case BRONZE_GAME:
                return loginInfo.isMaySubmitBronzeMatch();
            case FINAL:
                return loginInfo.isMaySubmitFinal();

        }
        return false;
    }

    private PlayRow addRow(PlayoffPlay playoffPlay, Grid grid, int row, boolean enabled) {
        PlayRow playRow = new PlayRow();
        PlayoffGame game = playoffPlay.getGame();
        playRow.gameIndex = game.getGameIndex();
        playRow.label = new Label(row + ". " + game.getHomeTeam() + " - " + game.getAwayTeam());
        playRow.checkBoxes = new ArrayList<>(3);
        int result = playoffPlay.getGameResult().intValue();
        int index = 0;
        for (Boolean checked : playoffPlay.getChecked()) {
            CheckBox checkBox = new CheckBox();
            if (index++ == result) {
                checkBox.setStyleName("correct");
            }
            checkBox.setValue(checked);
            checkBox.setEnabled(enabled);
            checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                @Override
                public void onValueChange(ValueChangeEvent<Boolean> event) {
                    int roundIndex = 0;
                    int numCheckedRows = numCheckedRows();
                    numRowsLabel.setText(getNumRowsText());
                    for (Button saveButton : saveButtons) {
                        if (numCheckedRows > MAX_NUM_ROWS) {
                            saveButton.setEnabled(false);
                        } else {
                            saveButton.setEnabled(maySubmitRound(getRound(roundIndex),
                                                                 getPlays(roundIndex)));
                        }
                        roundIndex++;
                    }
                }
            });
            playRow.checkBoxes.add(checkBox);
        }

        int col = 0;
        grid.setWidget(row, col++, playRow.label);

        for (CheckBox checkBox : playRow.checkBoxes) {
            grid.setWidget(row, col++, checkBox);
        }

        return playRow;
    }

    private List<PlayRow> getPlays(int roundIndex) {
        switch (roundIndex) {
            case 0:
                return rows.subList(0, 8);
            case 1:
                return rows.subList(8, 12);
            case 2:
                return rows.subList(12, 14);
            case 3:
                return rows.subList(14, 15);
            case 4:
                return rows.subList(15, 16);
            default:
                throw new IllegalArgumentException();
        }
    }

    private PlayoffRound getRound(int roundIndex) {
        switch (roundIndex) {
            case 0:
                return PlayoffRound.EIGHTS_FINAL;
            case 1:
                return PlayoffRound.QUARTER_FINAL;
            case 2:
                return PlayoffRound.SEMI_FINAL;
            case 3:
                return PlayoffRound.BRONZE_GAME;
            case 4:
                return PlayoffRound.FINAL;
            default:
                throw new IllegalArgumentException();
        }
    }
}
