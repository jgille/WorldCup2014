package org.jon.ivmark.worldcup.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jon.ivmark.worldcup.playoffs.client.PlayoffPanel;
import org.jon.ivmark.worldcup.shared.LoginInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebApp implements EntryPoint {

    private LoginInfo loginInfo = null;

    private Label userLabel = new Label();
    private Anchor signOutLink = new Anchor("Logga ut");

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
                    DockLayoutPanel header = new DockLayoutPanel(Style.Unit.EM);

                    UserPanel userPanel = new UserPanel(loginInfo);
                    header.addEast(userPanel.getMainPanel(), 20);

                    TabLayoutPanel tabs = new TabLayoutPanel(1.5, Style.Unit.EM);

                    PlayoffPanel playoffPanel = new PlayoffPanel(loginInfo);
                    tabs.add(playoffPanel.getMainPanel(), "Slutspel");

                    GroupPlayPanel groupPlayPanel = new GroupPlayPanel(loginInfo);
                    tabs.add(groupPlayPanel.getMainPanel(), "Gruppspel");

                    tabs.add(new SettingsPanel(userPanel).getMainPanel(), "Inställningar");

                    DockLayoutPanel mainPanel = new DockLayoutPanel(Style.Unit.EM);
                    mainPanel.addNorth(header, 4);
                    mainPanel.add(tabs);

                    RootLayoutPanel.get().add(mainPanel);
                } else {
                    loadLogin();
                }
            }

        });
    }

    private void loadLogin() {
        Window.Location.assign(loginInfo.getLoginUrl());
    }

}
