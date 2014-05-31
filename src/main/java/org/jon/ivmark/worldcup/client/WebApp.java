package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.client.domain.*;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp implements EntryPoint {

    private static final int ONE_INDEX = 0;
    private static final int X_INDEX = 1;
    private static final int TWO_INDEX = 2;

    private LoginInfo loginInfo = null;
    private VerticalPanel loginPanel = new VerticalPanel();
    private Label loginLabel = new Label(
            "Logga in med ditt Google-konto.");
    private Anchor signInLink = new Anchor("Logga in");

    private VerticalPanel userPanel = new VerticalPanel();
    private Anchor signOutLink = new Anchor("Logga ut");

    private final List<Label> numRowsLabels = new ArrayList<>(Round.NUM_ROUNDS);
    private final List<Button> saveButtons = new ArrayList<>(Round.NUM_ROUNDS);

    private List<Round> rounds;

    public WebApp() {
        this.rounds = new ArrayList<>(Round.NUM_ROUNDS);
        for (int i = 0; i < Round.NUM_ROUNDS; i++) {
            rounds.add(new Round(i));
        }
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
                if (loginInfo.isLoggedIn()) {
                    loadRounds(loginInfo);
                } else {
                    loadLogin();
                }
            }
        });
    }

    private void loadLogin() {
        // Assemble login panel.
        signInLink.setHref(loginInfo.getLoginUrl());
        loginPanel.add(loginLabel);
        loginPanel.add(signInLink);
        RootPanel.get("main").add(loginPanel);
    }

    private void loadWorldCupPage(LoginInfo loginInfo) {

        signOutLink.setHref(loginInfo.getLogoutUrl());
        userPanel.add(new Label("Välkommen " + loginInfo.getNickname()));
        userPanel.add(signOutLink);
        RootPanel.get("user").add(userPanel);

        Grid mainGrid = new Grid(4, Round.NUM_ROUNDS);
        mainGrid.setStyleName("mainGrid");

        HTMLTable.CellFormatter cellFormatter = mainGrid.getCellFormatter();

        for (int col = 0; col < Round.NUM_ROUNDS; col++) {
            mainGrid.setText(0, col, "Omgång " + (col + 1));
            cellFormatter.setStyleName(0, col, "tableHeading");
        }

        Games games = Games.allGames();

        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            Grid grid = new Grid(Round.NUM_GAMES + 1, 4);

            grid.setText(0, 0, "");
            grid.setText(0, 1, "1");
            grid.setText(0, 2, "X");
            grid.setText(0, 3, "2");

            Round round = getRound(roundIndex);

            for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
                GameId gameId = new GameId(roundIndex, gameIndex);
                Game game = games.get(gameId);
                Label gameLabel = new Label(game.label());
                int row = gameIndex + 1;
                grid.setWidget(row, 0, gameLabel);

                Play play = round.getPlay(gameIndex);

                CheckBox checkBoxOne = createCheckBox(roundIndex, gameIndex, ONE_INDEX, play.isOneChecked());
                grid.setWidget(row, 1, checkBoxOne);

                CheckBox checkBoxX = createCheckBox(roundIndex, gameIndex, X_INDEX, play.isXChecked());
                grid.setWidget(row, 2, checkBoxX);

                CheckBox checkBoxTwo = createCheckBox(roundIndex, gameIndex, TWO_INDEX, play.isTwoChecked());
                grid.setWidget(row, 3, checkBoxTwo);
            }

            HTMLTable.CellFormatter cellFormatter1X2 = grid.getCellFormatter();
            for (int r = 0; r < 17; r++) {
                for (int c = 1; c < 4; c++) {
                    cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
                }
            }

            Label label = new Label(round.numRowsText());
            mainGrid.setWidget(2, roundIndex, label);

            final Button saveButton = new Button("Spara");
            saveButton.setEnabled(round.isValid());
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

        RootPanel.get("main").add(mainGrid);
    }

    private void loadRounds(final LoginInfo loginInfo) {

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
                loadWorldCupPage(loginInfo);
            }
        });

    }

    private void saveRound(final Button button, int roundIndex) {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        PlaysDto plays = getRound(roundIndex).asDto();
        playService.savePlay(plays, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                RootPanel.get("main").add(new Label("Ooops, något gick åt skogen"));
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

    private CheckBox createCheckBox(final int round, final int game, final int signIndex, boolean check) {
        CheckBox checkBox = new CheckBox();
        checkBox.setValue(check);
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                checkBoxStateChanged(round, game, signIndex, event.getValue());
            }
        });
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
        saveButton.setEnabled(round.isValid());
    }

    private Play getGame(int gameNumber, Round round) {
        return round.getPlay(gameNumber);
    }

}
