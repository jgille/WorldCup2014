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
        tabs.add(new ScrollPanel(getToplistPanel()), "Topplista");
        tabs.add(new ScrollPanel(getAllPlaysPanel()), "Alla spel");
        mainPanel.add(tabs);
        return mainPanel;
    }

    private Widget getToplistPanel() {
        return new ToplistPanel().getWidget();
    }

    private Widget getPlaysPanel() {
        return new PlayPanel(loginInfo).getWidget();
    }

    private Widget getResultsPanel() {
        return new ResultsPanel(loginInfo).getWidget();
    }

    public Widget getAllPlaysPanel() {
        return new AllPlaysPanel(loginInfo).getWidget();
    }
}
