package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.shared.PlayDto;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.ArrayList;
import java.util.List;

public class DataStorePlaysRepository implements PlaysRepository {
    private static final String KIND = "play";

    @Override
    public void save(User user, PlaysDto playsDto) {
        String id = getId(user, playsDto.roundIndex);
        Entity entity = new Entity(KIND, id);
        entity.setProperty("email", user.getEmail());
        entity.setProperty("user_id", user.getUserId());
        entity.setProperty("round_index", playsDto.roundIndex);

        int i = 0;
        for (PlayDto playDto : playsDto.plays) {
            String key = getGamePropertyName(i++);
            entity.setUnindexedProperty(key, playDto.asString());
        }

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(entity);
    }

    private String getId(User user, int roundIndex) {
        return user.getUserId() + "_" + roundIndex;
    }

    @Override
    public PlaysDto get(User user, int roundIndex) {

        String id = getId(user, roundIndex);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity;
        try {
            entity = datastoreService.get(KeyFactory.createKey(KIND, id));
        } catch (EntityNotFoundException e) {
            return new Round(roundIndex).asDto();
        }
        return playsFromEntity(entity);
    }

    @Override
    public AllPlaysDtos getAll() {
        Query query = new Query(KIND);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Iterable<Entity> entities = datastoreService.prepare(query).asIterable(FetchOptions.Builder.withLimit(500));
        List<PlaysDto> plays = new ArrayList<>();
        for (Entity entity : entities) {
            plays.add(playsFromEntity(entity));
        }

        return new AllPlaysDtos(plays);
    }

    private PlaysDto playsFromEntity(Entity entity) {
        PlaysDto playsDto = new PlaysDto();
        playsDto.userId = (String) entity.getProperty("user_id");
        playsDto.roundIndex = ((Long) entity.getProperty("round_index")).intValue();
        playsDto.plays = new PlayDto[Round.NUM_GAMES];

        for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
            String property = (String) entity.getProperty(getGamePropertyName(gameIndex));
            PlayDto playDto = PlayDto.fromString(property);
            playsDto.plays[gameIndex] = playDto;
        }
        return playsDto;
    }

    private String getGamePropertyName(int gameIndex) {
        return "play_" + gameIndex;
    }
}
