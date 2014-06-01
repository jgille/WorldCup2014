package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.datastore.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jon.ivmark.worldcup.client.ResultsService;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.shared.GameResult;
import org.jon.ivmark.worldcup.shared.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ResultsServiceImpl extends RemoteServiceServlet implements ResultsService {
    private static final String KIND = "result";

    private static final Logger LOGGER = Logger.getLogger(ResultsServiceImpl.class.getName());

    @Override
    public List<Result> loadResults() {
        List<Result> results = new ArrayList<>(Round.NUM_ROUNDS);
        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            results.add(loadResult(roundIndex));
        }
        LOGGER.info("Loaded results");
        return results;
    }

    private Result loadResult(int roundIndex) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Result result = new Result(roundIndex);
            Entity entity = datastoreService.get(getKey(roundIndex));
            for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
                GameResult gameResult = GameResult.valueOf((String) entity.getProperty("g" + gameIndex));
                result.setResult(gameIndex, gameResult);
            }
            return result;
        } catch (EntityNotFoundException e) {
            return new Result(roundIndex);
        }
    }

    @Override
    public void saveResult(Result result) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, getKey(result.getRoundIndex()));
        entity.setProperty("round_index", result.getRoundIndex());
        int gameIndex = 0;
        for (GameResult gameResult : result.getResults()) {
            entity.setUnindexedProperty("g" + gameIndex++, gameResult.name());
        }
        datastoreService.put(entity);
        LOGGER.info("Saved result: " + result);

    }

    private Key getKey(int roundIndex) {
        return KeyFactory.createKey(KIND, "round_" + roundIndex);
    }
}
