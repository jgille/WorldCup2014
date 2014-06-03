package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.users.User;

public interface TeamRepository {

    String getTeamName(User user);

    void setTeamName(User user, String teamName);

    String getTeamName(String teamName);
}
