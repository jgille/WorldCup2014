package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;

public class DataStoreTeamRepository implements TeamRepository {
    private static final String KIND = "team";

    @Override
    public String getTeamName(User user) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.createKey(KIND, user.getUserId()));
            return (String) entity.getProperty("name");
        } catch (EntityNotFoundException e) {
            return user.getNickname();
        }

    }

    @Override
    public void setTeamName(User user, String teamName) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, user.getUserId());
        entity.setUnindexedProperty("name", teamName);
        datastoreService.put(entity);
    }
}
