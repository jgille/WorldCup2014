package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.datastore.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jon.ivmark.worldcup.client.ResultsService;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.shared.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ResultsServiceImpl extends RemoteServiceServlet implements ResultsService {
    private static final String KIND = "result";

    private static final Logger LOGGER = Logger.getLogger(ResultsServiceImpl.class.getName());

    private final PlaysRepository playsRepository = new DataStorePlaysRepository();
    private final TeamRepository teamRepository = new DataStoreTeamRepository();

    @Override
    public List<Result> loadResults() {
        List<Result> results = new ArrayList<>(Round.NUM_ROUNDS);
        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            results.add(loadResult(roundIndex));
        }
        LOGGER.info("Loaded results.");
        return results;
    }

    private Result loadResult(int roundIndex) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Result result = new Result(roundIndex);
            Entity entity = datastoreService.get(getKey(roundIndex));
            for (int gameIndex = 0; gameIndex < Round.NUM_GAMES; gameIndex++) {
                String property = (String) entity.getProperty(getGamePropertyName(gameIndex));
                GameResult gameResult = GameResult.valueOf(property);
                result.setResult(gameIndex, gameResult);
            }
            return result;
        } catch (EntityNotFoundException e) {
            return new Result(roundIndex);
        }
    }

    private String getGamePropertyName(int gameIndex) {
        return "g" + gameIndex;
    }

    @Override
    public void saveResult(Result result) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(KIND, getRoundKey(result.getRoundIndex()));
        entity.setProperty("round_index", result.getRoundIndex());
        int gameIndex = 0;
        for (GameResult gameResult : result.getResults()) {
            entity.setUnindexedProperty(getGamePropertyName(gameIndex++), gameResult.name());
        }
        datastoreService.put(entity);
        LOGGER.info("Saved result: " + result);
    }

    @Override
    public TopList getTopList() {
        Map<String, List<PlaysDto>> playsByUser = playsRepository.getAll().byUserWithAllRoundsSubmitted();
        List<Result> results = loadResults();

        Map<String, List<PlaysDto>> playsByTeam = new HashMap<>();
        for (Map.Entry<String, List<PlaysDto>> e : playsByUser.entrySet()) {
            String teamName = getTeamName(e.getKey());
            playsByTeam.put(teamName, e.getValue());
        }

        TopList topList = TopList.computeTopList(results, playsByTeam);

        LOGGER.info("Loaded top list");
        return topList;
    }

    private String getTeamName(String userId) {
        return teamRepository.getTeamName(userId);
    }

    private Key getKey(int roundIndex) {
        return KeyFactory.createKey(KIND, getRoundKey(roundIndex));
    }

    private String getRoundKey(int roundIndex) {
        return "round_" + roundIndex;
    }
}
