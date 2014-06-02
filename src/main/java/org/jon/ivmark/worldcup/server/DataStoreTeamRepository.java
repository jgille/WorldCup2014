package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;

import java.util.logging.Logger;

public class DataStoreTeamRepository implements TeamRepository {
    private static final Logger LOGGER = Logger.getLogger(DataStorePlaysRepository.class.getName());

    private static final String KIND = "team";

    @Override
    public String getTeamName(User user) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.createKey(KIND, user.getUserId()));
            String teamName = (String) entity.getProperty("name");
            LOGGER.info("Team name: " + teamName + " for email " + user.getEmail());
            return teamName;
        } catch (EntityNotFoundException e) {
            setTeamName(user, user.getNickname());
            LOGGER.info("Defaulting to team name: " + user.getNickname() + " for email " + user.getEmail());
            return user.getNickname();
        }

    }

    @Override
    public void setTeamName(User user, String teamName) {
        LOGGER.info("Saving team name: " + teamName + " for email " + user.getEmail());

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, user.getUserId());
        entity.setProperty("email", user.getEmail());
        entity.setUnindexedProperty("name", teamName);
        datastoreService.put(entity);
    }
}
