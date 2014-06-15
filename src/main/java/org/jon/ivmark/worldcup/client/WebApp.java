package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.client.domain.*;
import org.jon.ivmark.worldcup.shared.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp implements EntryPoint {

    private static final int ONE_INDEX = 0;
    private static final int X_INDEX = 1;
    private static final int TWO_INDEX = 2;

    private LoginInfo loginInfo = null;

    private VerticalPanel userPanel = new VerticalPanel();
    private Label userLabel = new Label();

    private Anchor signOutLink = new Anchor("Logga ut");
    private final List<Label> numRowsLabels = new ArrayList<>(Round.NUM_ROUNDS);

    private final List<Button> saveButtons = new ArrayList<>(Round.NUM_ROUNDS);
    private List<Round> rounds;
    private TextBox teamTextBox = new TextBox();
    private Button saveSettingsButton = new Button("Spara");
    private VerticalPanel resultsPanel = new VerticalPanel();
    private List<Result> results;
    private VerticalPanel topListPanel = new VerticalPanel();

    private Label playInfoLabel = new Label();

    private boolean editable = true;

    private VerticalPanel allPlaysPanel = new VerticalPanel();

    private VerticalPanel similaritiesPanel = new VerticalPanel();


    public WebApp() {
        this.rounds = new ArrayList<>(Round.NUM_ROUNDS);
        for (int i = 0; i < Round.NUM_ROUNDS; i++) {
            rounds.add(new Round(i));
        }

        saveSettingsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveSettings();
            }
        });
        teamTextBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                // TODO: Use Pattern
                saveSettingsButton.setEnabled(teamTextBox.getText().trim().length() > 1);
            }
        });
        teamTextBox.setMaxLength(50);
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        // Check login status using login service.
        LoginServiceAsync loginService = GWT.create(LoginService.class);
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
            public void onFailure(Throwable error) {
            }

            public void onSuccess(LoginInfo result) {
                loginInfo = result;
                editable = loginInfo.maySubmitPlay();
                if (loginInfo.isLoggedIn()) {
                    loadRounds();
                    loadToplist();
                    loadTeam();
                } else {
                    loadLogin();
                }
            }

        });
    }

    private void loadToplist() {
        ResultsServiceAsync resultsServiceAsync = GWT.create(ResultsService.class);
        resultsServiceAsync.getTopList(new AsyncCallback<TopList>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(TopList result) {
                renderToplist(result);
            }
        });
    }

    private void renderToplist(TopList result) {
        topListPanel.setStyleName("topListPanel");
        List<TopListEntry> entries = result.getEntries();

        Grid overallToplist = new Grid(entries.size() + 1, 6);
        overallToplist.setStyleName("topListGrid");
        int firstRow = 0;
        int col = 0;
        overallToplist.setText(firstRow, col++, "");
        overallToplist.setText(firstRow, col++, "Lag");
        overallToplist.setText(firstRow, col++, "Omgång 1");
        overallToplist.setText(firstRow, col++, "Omgång 2");
        overallToplist.setText(firstRow, col++, "Omgång 3");
        overallToplist.setText(firstRow, col, "Totalt");

        HTMLTable.CellFormatter cellFormatter = overallToplist.getCellFormatter();

        for (int column = 0; column < 6; column++) {
            cellFormatter.setStyleName(0, column, "tableHeading");
        }

        int row = 1;
        int prevPos = -1;
        for (TopListEntry topListEntry : entries) {
            List<PointsEntry> pointsEntries = topListEntry.getEntries();

            int column = 0;
            int position = topListEntry.getPosition();
            String posText = prevPos != position ? position + "." : "";
            prevPos = position;
            overallToplist.setText(row, column++, posText);
            overallToplist.setText(row, column++, topListEntry.getTeamName());
            overallToplist.setText(row, column++, getPointsText(pointsEntries.get(0)));
            overallToplist.setText(row, column++, getPointsText(pointsEntries.get(1)));
            overallToplist.setText(row, column++, getPointsText(pointsEntries.get(2)));
            overallToplist.setText(row, column, topListEntry.totalPoints() + " (" + topListEntry.totalNumCorrect() +
                    ")");
            row++;
        }

        topListPanel.add(overallToplist);
    }

    private String getPointsText(PointsEntry pointsEntry) {
        return pointsEntry.getPoints() + " (" + pointsEntry.getNumCorrectGames() + ")";
    }

    private void loadTeam() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        playService.getTeamName(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String teamName) {
                signOutLink.setHref(loginInfo.getLogoutUrl());
                userLabel.setText(getWelcomeText(teamName));
                teamTextBox.setText(teamName);
            }
        });
    }

    private void loadLogin() {
        Window.Location.assign(loginInfo.getLoginUrl());
    }

    private void renderAll() {
        Grid mainGrid = new Grid(4, Round.NUM_ROUNDS);
        VerticalPanel playPanel = new VerticalPanel();
        playPanel.setStyleName("mainGrid");
        mainGrid.setStyleName("mainGrid");

        playPanel.add(mainGrid);
        if (!editable) {
            playInfoLabel.setText("Du kan inte ändra dina spel längre, VM har börjat...");
            playPanel.add(playInfoLabel);
        }
        HTMLTable.CellFormatter cellFormatter = mainGrid.getCellFormatter();

        for (int col = 0; col < Round.NUM_ROUNDS; col++) {
            mainGrid.setText(0, col, "Omgång " + (col + 1));
            cellFormatter.setStyleName(0, col, "tableHeading");
        }

        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            Round round = getRound(roundIndex);

            Grid grid = getPlayGrid(roundIndex, getRound(roundIndex), results.get(roundIndex), null, 1);

            HTMLTable.CellFormatter cellFormatter1X2 = grid.getCellFormatter();
            for (int r = 0; r < 17; r++) {
                for (int c = 1; c < 4; c++) {
                    cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
                }
            }

            Label label = new Label(round.numRowsText());
            mainGrid.setWidget(2, roundIndex, label);

            final Button saveButton = new Button("Spara");
            saveButton.setEnabled(false);
            final int finalRoundNumber = roundIndex;
            saveButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    saveButton.setText("Sparar...");
                    saveRound(saveButton, finalRoundNumber);
                }
            });
            mainGrid.setWidget(3, roundIndex, saveButton);

            numRowsLabels.add(label);
            saveButtons.add(saveButton);

            mainGrid.setWidget(1, roundIndex, grid);
        }

        DockLayoutPanel mainPanel = new DockLayoutPanel(Style.Unit.EM);

        TabLayoutPanel tabs = new TabLayoutPanel(1.5, Style.Unit.EM);
        tabs.add(new ScrollPanel(playPanel), "Dina spel");

        tabs.add(new ScrollPanel(resultsPanel), "Resultat");
        tabs.add(new ScrollPanel(topListPanel), "Topplista");
        tabs.add(new ScrollPanel(allPlaysPanel), "Alla spel");
        tabs.add(new ScrollPanel(similaritiesPanel), "Likhet med andra spelare");

        HorizontalPanel settingsPanel = new HorizontalPanel();
        settingsPanel.add(new Label("Lagnamn:"));
        settingsPanel.add(teamTextBox);
        settingsPanel.add(saveSettingsButton);
        settingsPanel.setStyleName("settingsPanel");
        tabs.add(settingsPanel, "Inställningar");

        tabs.add(new ScrollPanel(new HTML(Rules.rulesHtml())), "Regler");

        DockLayoutPanel header = new DockLayoutPanel(Style.Unit.EM);

        userPanel.add(userLabel);
        userPanel.add(signOutLink);
        header.addEast(userPanel, 20);
        mainPanel.addNorth(header, 4);
        mainPanel.add(tabs);

        RootLayoutPanel.get().add(mainPanel);
    }

    private Grid getPlayGrid(int roundIndex, Round round, Result result, List<Plays> howPeoplePlayed, int numPlayers) {
        Games games = Games.allGames();

        int numRows = howPeoplePlayed != null ? Round.NUM_GAMES * 2 + 1 : Round.NUM_GAMES + 1;

        Grid grid = new Grid(numRows, 4);
        HTMLTable.CellFormatter cellFormatter = grid.getCellFormatter();

        for (int r = 0; r < numRows; r++) {
            for (int c = 1; c < 4; c++) {
                cellFormatter.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
            }
        }

        grid.setStyleName("roundTable");

        grid.setText(0, 0, "");
        grid.setText(0, 1, "1");
        grid.setText(0, 2, "X");
        grid.setText(0, 3, "2");

        List<GameResult> resultList = result.getResults();
        int row = 1;
        for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
            GameResult gameResult = resultList.get(gameIndex);
            GameId gameId = new GameId(roundIndex, gameIndex);
            Game game = games.get(gameId);
            Label gameLabel = new Label(game.label());
            grid.setWidget(row, 0, gameLabel);

            Play play = round.getPlay(gameIndex);

            CheckBox checkBoxOne = createCheckBox(roundIndex, gameIndex, ONE_INDEX, play.isOneChecked(),
                                                  gameResult == GameResult.ONE);
            grid.setWidget(row, 1, checkBoxOne);

            CheckBox checkBoxX = createCheckBox(roundIndex, gameIndex, X_INDEX, play.isXChecked(),
                                                gameResult == GameResult.X);
            grid.setWidget(row, 2, checkBoxX);

            CheckBox checkBoxTwo = createCheckBox(roundIndex, gameIndex, TWO_INDEX, play.isTwoChecked(),
                                                  gameResult == GameResult.TWO);
            grid.setWidget(row, 3, checkBoxTwo);

            row++;

            if (howPeoplePlayed != null) {
                Plays plays = howPeoplePlayed.get(gameIndex);

                Label labelOne = new Label(plays.getOne() + "/" + numPlayers);
                labelOne.setStyleName("howPeoplePlayed");
                grid.setWidget(row, 1, labelOne);

                Label labelX = new Label(plays.getX() + "/" + numPlayers);
                labelX.setStyleName("howPeoplePlayed");
                grid.setWidget(row, 2, labelX);

                Label labelTwo = new Label(plays.getTwo() + "/" + numPlayers);
                labelTwo.setStyleName("howPeoplePlayed");
                grid.setWidget(row, 3, labelTwo);
                row++;
            }
        }
        return grid;
    }

    private void loadResultPage() {
        ResultsServiceAsync resultsService = GWT.create(ResultsService.class);
        resultsService.loadResults(new AsyncCallback<List<Result>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<Result> results) {
                WebApp.this.results = results;
                renderResultsPanel();
                loadAllPlays();
                loadSimilarities();
                renderAll();
            }
        });
    }

    private void loadSimilarities() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        playService.loadSimilarities(new AsyncCallback<SimilarityMatrix>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SimilarityMatrix similarityMatrix) {
                renderSimilarities(similarityMatrix);
            }
        });
    }

    private void renderSimilarities(SimilarityMatrix similarityMatrix) {
        List<String> teams = similarityMatrix.getTeams();
        int[][] similarities = similarityMatrix.getSimilarities();
        Grid grid = new Grid(teams.size() + 1, teams.size() + 1);
        grid.setStyleName("similarityGrid");

        int teamIndex = 1;
        for (String team : teams) {
            Label rowHeader = new Label(team);
            grid.setWidget(0, teamIndex, rowHeader);
            Label columnHeader = new Label(team);
            grid.setWidget(teamIndex, 0, columnHeader);

            int colIndex = 1;
            for (int similarity : similarities[teamIndex - 1]) {
                Label simLabel = new Label(similarity + " %");
                if (teamIndex != colIndex) {
                    Style style = simLabel.getElement().getStyle();
                    style.setBackgroundColor(getSimilarityColor(similarity));
                } else {
                    simLabel.setText("-");
                }
                grid.setWidget(teamIndex, colIndex++, simLabel);
            }

            teamIndex++;
        }

        similaritiesPanel.add(grid);
    }

    private String getSimilarityColor(int similarity) {
        double sim = similarity / 100d;
        int green = (int) (180 * sim);
        int red = 180 - green;
        int blue = red;
        return "rgb(" + red + "," + green + "," + blue + ")";
    }

    private void loadAllPlays() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        playService.loadAllCompletePlays(new AsyncCallback<Map<String, List<PlaysDto>>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Map<String, List<PlaysDto>> allPlays) {
                renderAllPlays(allPlays);
            }
        });
    }

    private void renderAllPlays(Map<String, List<PlaysDto>> allPlays) {
        int numPlayers = allPlays.size();

        List<List<Plays>> howPeoplePlayed = computePlaysPercentages(allPlays);

        for (Map.Entry<String, List<PlaysDto>> e : allPlays.entrySet()) {
            int row = 0;

            Grid mainGrid = new Grid(2, Round.NUM_ROUNDS);
            mainGrid.setStyleName("mainGrid");
            HTMLTable.CellFormatter cellFormatter = mainGrid.getCellFormatter();

            for (int col = 0; col < Round.NUM_ROUNDS; col++) {
                mainGrid.setText(row, col, "Omgång " + (col + 1));
                cellFormatter.setStyleName(row, col, "tableHeading");
            }

            String teamName = e.getKey();
            List<PlaysDto> plays = e.getValue();
            Label label = new Label(teamName);
            label.setStyleName("h1");

            int roundIndex = 0;
            for (PlaysDto playsDto : plays) {
                Result result = results.get(roundIndex);

                Grid grid = getPlayGrid(roundIndex, Round.fromPlaysDto(playsDto), result,
                                        howPeoplePlayed.get(roundIndex), numPlayers);
                mainGrid.setWidget(row + 1, roundIndex, grid);
                roundIndex++;
            }

            allPlaysPanel.add(label);
            allPlaysPanel.add(mainGrid);
        }
    }

    private List<List<Plays>> computePlaysPercentages(Map<String, List<PlaysDto>> allPlays) {
        List<List<Plays>> all = new ArrayList<>();
        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            List<Plays> playsForRound = new ArrayList<>();
            all.add(playsForRound);
            for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
                playsForRound.add(new Plays());
            }
        }

        for (Map.Entry<String, List<PlaysDto>> e : allPlays.entrySet()) {
            for (PlaysDto playsDto : e.getValue()) {
                int gameIndex = 0;
                List<Plays> plays = all.get(playsDto.roundIndex);
                for (PlayDto playDto : playsDto.plays) {
                    plays.get(gameIndex++).append(playDto);
                }
            }
        }

        return all;
    }

    private static class Plays {

        private int one;
        private int x;
        private int two;

        private Plays() {
            this(0, 0, 0);
        }

        private Plays(int one, int x, int two) {
            this.one = one;
            this.x = x;
            this.two = two;
        }

        private int getOne() {
            return one;
        }

        private int getX() {
            return x;
        }

        private int getTwo() {
            return two;
        }

        public void append(PlayDto playDto) {
            boolean[] checked = playDto.checked;
            one += checked[0] ? 1 : 0;
            x += checked[1] ? 1 : 0;
            two += checked[2] ? 1 : 0;
        }

        public int[] percentages(int numPlayers) {
            return new int[]{percentage(one, numPlayers),
                             percentage(x, numPlayers),
                             percentage(two, numPlayers)};
        }

        private int percentage(int numPlayed, int numPlayers) {
            return (int) Math.round((100d * numPlayed) / numPlayers);
        }
    }

    private void renderResultsPanel() {
        Grid main = new Grid(2, 3);

        HTMLTable.CellFormatter cellFormatter = main.getCellFormatter();

        for (int col = 0; col < Round.NUM_ROUNDS; col++) {
            main.setText(0, col, "Omgång " + (col + 1));
            cellFormatter.setStyleName(0, col, "tableHeading");
        }

        Games games = Games.allGames();
        for (final Result result : results) {

            Grid resultGrid = new Grid(Round.NUM_GAMES + 1, 4);

            resultGrid.setText(0, 1, "1");
            resultGrid.setText(0, 2, "X");
            resultGrid.setText(0, 3, "2");

            resultGrid.setStyleName("resultsGrid");
            int gameIndex = 0;
            for (GameResult gameResult : result.getResults()) {
                Game game = games.get(new GameId(result.getRoundIndex(), gameIndex));
                int row = gameIndex + 1;
                resultGrid.setText(row, 0, game.label());
                for (int i = 0; i < 3; i++) {
                    RadioButton radioButton = createRadioButton(result, gameIndex, gameResult, i);
                    radioButton.setEnabled(loginInfo.isAdmin());
                    resultGrid.setWidget(row, i + 1, radioButton);
                }
                gameIndex++;

                HTMLTable.CellFormatter cellFormatter1X2 = resultGrid.getCellFormatter();
                for (int r = 0; r < 17; r++) {
                    for (int c = 1; c < 4; c++) {
                        cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
                    }
                }
            }

            main.setWidget(1, result.getRoundIndex(), resultGrid);
        }
        resultsPanel.setStyleName("resultsPanel");
        resultsPanel.add(main);
        if (loginInfo.isAdmin()) {
            Button saveButton = new Button("Spara");
            saveButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    saveResults();
                }
            });
            resultsPanel.add(saveButton);
        }
    }

    private void saveResults() {
        ResultsServiceAsync resultsService = GWT.create(ResultsService.class);
        for (Result result : results) {
            resultsService.saveResult(result, new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(Void result) {
                }
            });
        }
    }

    private RadioButton createRadioButton(final Result result, final int gameIndex, final GameResult gameResult,
                                          final int index) {
        final RadioButton radioButton = new RadioButton("rb-" + result.getRoundIndex() + "-" + gameIndex);
        if (index == gameResult.intValue()) {
            radioButton.setValue(true);
        }
        radioButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (radioButton.getValue()) {
                    result.setResult(gameIndex, GameResult.fromInt(index));
                } else {
                    result.setResult(gameIndex, GameResult.UNKNOWN);
                }
            }
        });
        return radioButton;
    }

    private void saveSettings() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        final String name = teamTextBox.getText();
        playService.setTeamName(name, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
                userLabel.setText(getWelcomeText(name));
                saveSettingsButton.setEnabled(false);
            }
        });
    }

    private String getWelcomeText(String teamName) {
        return "Välkommen " + teamName;
    }

    private void loadRounds() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        playService.loadPlays(new AsyncCallback<List<PlaysDto>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<PlaysDto> result) {
                rounds = new ArrayList<>(result.size());
                for (PlaysDto playsDto : result) {
                    Round round = Round.fromPlaysDto(playsDto);
                    rounds.add(round);
                }
                loadResultPage();

            }
        });

    }

    private void saveRound(final Button button, int roundIndex) {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        PlaysDto plays = getRound(roundIndex).asDto();
        playService.savePlay(plays, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.Location.reload();
            }

            @Override
            public void onSuccess(Void result) {
                button.setText("Spara");
                button.setEnabled(false);
            }
        });
    }

    private Round getRound(int roundIndex) {
        return rounds.get(roundIndex);
    }

    private CheckBox createCheckBox(final int round, final int game, final int signIndex, boolean check,
                                    boolean correct) {
        CheckBox checkBox = new CheckBox();
        checkBox.setValue(check);
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                checkBoxStateChanged(round, game, signIndex, event.getValue());
            }
        });
        checkBox.setEnabled(editable);
        if (correct) {
            checkBox.setStyleName("correct");
        }
        return checkBox;
    }

    private void checkBoxStateChanged(int roundNumber, int gameNumber, int signIndex, boolean isChecked) {
        Round round = getRound(roundNumber);

        Play play = getGame(gameNumber, round);
        switch (signIndex) {
            case ONE_INDEX:
                play.setOne(isChecked);
                break;
            case X_INDEX:
                play.setX(isChecked);
                break;
            case TWO_INDEX:
                play.setTwo(isChecked);
                break;
        }

        Label label = numRowsLabels.get(roundNumber);
        label.setText(round.numRowsText());
        if (round.tooManyRows()) {
            label.setStyleName("invalid");
        } else {
            label.removeStyleName("invalid");
        }
        Button saveButton = saveButtons.get(roundNumber);
        saveButton.setEnabled(round.isValid() && editable);
    }

    private Play getGame(int gameNumber, Round round) {
        return round.getPlay(gameNumber);
    }

}
