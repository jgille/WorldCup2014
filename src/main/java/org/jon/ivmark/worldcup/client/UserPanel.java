package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jon.ivmark.worldcup.shared.LoginInfo;

public class UserPanel {

    private final LoginInfo loginInfo;

    private Label userLabel = new Label();
    private Anchor signOutLink = new Anchor("Logga ut");

    public UserPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
        signOutLink.setHref(loginInfo.getLogoutUrl());
    }

    public void setTeamName(String teamName) {
        userLabel.setText(getWelcomeText(teamName));
    }

    private String getWelcomeText(String teamName) {
        return "Välkommen " + teamName;
    }

    Widget getMainPanel() {
        VerticalPanel userPanel = new VerticalPanel();
        userPanel.add(userLabel);
        userPanel.add(signOutLink);
        return userPanel;
    }
}
