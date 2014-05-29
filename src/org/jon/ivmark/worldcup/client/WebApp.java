package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(WebApp.class.getName());

    private LoginInfo loginInfo = null;
    private VerticalPanel loginPanel = new VerticalPanel();
    private Label loginLabel = new Label(
            "Please sign in to your Google Account.");
    private Anchor signInLink = new Anchor("Sign In");

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
                if(loginInfo.isLoggedIn()) {
                    loadWorldCupPage();
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

    private void loadWorldCupPage() {
        LOGGER.info("Loading page...");

        Grid mainGrid = new Grid(2, 3);
        mainGrid.setStyleName("mainGrid");

        HTMLTable.CellFormatter cellFormatter = mainGrid.getCellFormatter();

        for (int col = 0; col < 3; col++) {
            mainGrid.setText(0, col, "Omgång " + (col + 1));
            cellFormatter.setStyleName(0, col, "tableHeading");
        }

        for (int round = 1; round < 4; round++) {
            Grid grid = new Grid(17, 4);

            grid.setText(0, 0, "");
            grid.setText(0, 1, "1");
            grid.setText(0, 2, "X");
            grid.setText(0, 3, "2");


            for (int i = 0; i < 16; i++) {
                Label game = new Label("Game " + (i + 1));
                CheckBox checkBox1 = new CheckBox();
                CheckBox checkBoxX = new CheckBox();
                CheckBox checkBox2 = new CheckBox();
                int row = i + 1;
                grid.setWidget(row, 0, game);
                grid.setWidget(row, 1, checkBox1);
                grid.setWidget(row, 2, checkBoxX);
                grid.setWidget(row, 3, checkBox2);
            }

            HTMLTable.CellFormatter cellFormatter1X2 = grid.getCellFormatter();
            for (int r = 0; r < 17; r++) {
                for (int c = 1; c < 4; c++) {
                    cellFormatter1X2.setHorizontalAlignment(r, c, HasHorizontalAlignment.ALIGN_CENTER);
                }
            }

            mainGrid.setWidget(1, round - 1, grid);
        }

        RootPanel.get("main").add(mainGrid);
    }

}
