package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class SettingsPanel {

    private final UserPanel userPanel;

    private TextBox teamTextBox = new TextBox();
    private Button saveSettingsButton = new Button("Spara");

    private final HorizontalPanel mainPanel = new HorizontalPanel();

    public SettingsPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
        mainPanel.add(new Label("Lagnamn:"));
        mainPanel.add(teamTextBox);
        mainPanel.add(saveSettingsButton);
        mainPanel.setStyleName("settingsPanel");

        teamTextBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                saveSettingsButton.setEnabled(teamTextBox.getText().trim().length() > 1);
            }
        });

        teamTextBox.setMaxLength(50);

        saveSettingsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveSettings();
            }
        });

        loadTeam();
    }

    Widget getMainPanel() {
        return mainPanel;
    }

    private void loadTeam() {
        PlayServiceAsync playService = GWT.create(PlayService.class);
        playService.getTeamName(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String teamName) {
                userPanel.setTeamName(teamName);
                teamTextBox.setText(teamName);
            }
        });
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
                userPanel.setTeamName(name);
                saveSettingsButton.setEnabled(false);
            }
        });
    }

    private String getWelcomeText(String teamName) {
        return "Välkommen " + teamName;
    }
}
