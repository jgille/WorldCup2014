package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.client.domain.Game;
import org.jon.ivmark.worldcup.client.domain.GameId;
import org.jon.ivmark.worldcup.client.domain.Games;
import org.jon.ivmark.worldcup.shared.LoginInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(WebApp.class.getName());
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

    private final List<Label> numRowsLabels = new ArrayList<Label>(Round.NUM_ROUNDS);
    private final List<Button> saveButtons = new ArrayList<Button>(Round.NUM_ROUNDS);

    private final List<Round> rounds;

    public WebApp() {
        this.rounds = new ArrayList<Round>(Round.NUM_ROUNDS);
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
                    loadWorldCupPage(loginInfo);
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
        LOGGER.info("Loading page...");

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

            for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
                GameId gameId = new GameId(roundIndex, gameIndex);
                Game game = games.get(gameId);
                Label gameLabel = new Label(game.label());
                int row = gameIndex + 1;
                grid.setWidget(row, 0, gameLabel);

                for (int sign = ONE_INDEX; sign <= TWO_INDEX; sign++) {
                    CheckBox checkBox = createCheckBox(roundIndex, gameIndex, sign, false);
                    grid.setWidget(row, sign + 1, checkBox);
                }
            }

            HTMLTable.CellFormatter cellFormatter1X2 = grid.getCellFormatter();
            for (int r = 0; r < 17; r++) {
                for (int c = 1; c < 4; c++) {
                    cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
                }
            }

            Round round = rounds.get(roundIndex);
            Label label = new Label(round.numRowsText());
            mainGrid.setWidget(2, roundIndex, label);

            Button saveButton = new Button("Spara");
            saveButton.setEnabled(round.isValid());
            final int finalRoundNumber = roundIndex;
            saveButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    System.out.println("Saving round " + finalRoundNumber);
                }
            });
            mainGrid.setWidget(3, roundIndex, saveButton);

            numRowsLabels.add(label);
            saveButtons.add(saveButton);

            mainGrid.setWidget(1, roundIndex, grid);
        }

        RootPanel.get("main").add(mainGrid);
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
        Round round = rounds.get(roundNumber);

        Play play = round.getGame(gameNumber);
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
        Button saveButton = saveButtons.get(roundNumber);
        saveButton.setEnabled(round.isValid());
    }

}
