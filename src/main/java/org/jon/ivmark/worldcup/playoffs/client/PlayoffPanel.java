package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.shared.LoginInfo;

public class PlayoffPanel {
    private final LoginInfo loginInfo;

    private DockLayoutPanel mainPanel = new DockLayoutPanel(Style.Unit.EM);

    public PlayoffPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Widget getMainPanel() {
        TabLayoutPanel tabs = new TabLayoutPanel(1.5, Style.Unit.EM);
        tabs.add(new ScrollPanel(getPlaysPanel()), "Dina spel");
        tabs.add(new ScrollPanel(getResultsPanel()), "Resultat");
        tabs.add(new Label("Inte tillgängligt ännu"), "Topplista");
        tabs.add(new Label("Inte tillgängligt ännu"), "Alla spel");
        mainPanel.add(tabs);
        return mainPanel;
    }

    private Widget getPlaysPanel() {
        return new PlayPanel(loginInfo).getWidget();
    }

    private Widget getResultsPanel() {
        return new ResultsPanel(loginInfo).getWidget();
    }
}
