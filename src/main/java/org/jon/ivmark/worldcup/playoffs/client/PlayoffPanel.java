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
        tabs.add(new ScrollPanel(getResultsPanel()), "Resultat");
        mainPanel.add(tabs);
        return mainPanel;
    }

    private Widget getResultsPanel() {
        return new ResultsPanel(loginInfo).getWidget();
    }
}
