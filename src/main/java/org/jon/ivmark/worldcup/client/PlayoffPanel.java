package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.jon.ivmark.worldcup.shared.LoginInfo;

public class PlayoffPanel {
    private final LoginInfo loginInfo;

    public PlayoffPanel(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public Widget getMainPanel() {
        return new Label("Under konstruktion, så sluta tjata...");
    }
}
