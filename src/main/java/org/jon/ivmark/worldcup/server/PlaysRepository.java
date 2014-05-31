package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.users.User;
import org.jon.ivmark.worldcup.shared.PlaysDto;

public interface PlaysRepository {

    void save(User user, PlaysDto playsDto);

    PlaysDto get(User user, int roundIndex);
}
