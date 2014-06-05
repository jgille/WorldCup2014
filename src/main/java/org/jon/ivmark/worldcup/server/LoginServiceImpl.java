package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.common.io.Resources;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jon.ivmark.worldcup.client.LoginService;
import org.jon.ivmark.worldcup.shared.LoginInfo;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private final List<String> adminUsers = new ArrayList<>();

    public LoginServiceImpl() {
        URL resource = Resources.getResource("admin-users.txt");
        try {
            List<String> admins = Resources.readLines(resource, Charset.forName("UTF-8"));
            for (String admin : admins) {
                if (!admin.trim().isEmpty()) {
                    adminUsers.add(admin.trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginInfo login(String requestUri) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        LoginInfo loginInfo = new LoginInfo();

        loginInfo.setMaySubmitPlay(CutOff.isBeforeCutOff(DateTime.now()));

        if (user != null) {
            loginInfo.setLoggedIn(true);
            loginInfo.setEmailAddress(user.getEmail());
            loginInfo.setNickname(user.getNickname());
            loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
            loginInfo.setAdmin(adminUsers.contains(user.getEmail()));
        } else {
            loginInfo.setLoggedIn(false);
            loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
        }
        return loginInfo;
    }

}
