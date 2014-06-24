package org.jon.ivmark.worldcup.playoffs.server;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.playoffs.client.PlayoffService;
import org.jon.ivmark.worldcup.shared.GameResult;
import org.jon.ivmark.worldcup.shared.PlayDto;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffGame;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResult;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResults;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffRound;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayoffServiceImpl extends RemoteServiceServlet implements PlayoffService {

    private static final String RESULT_KIND = "playoff_results";

    private static final transient Logger LOGGER = Logger.getLogger(PlayoffServiceImpl.class.getName());

    @Override
    public void saveResults(List<PlayoffResult> results) {
        LOGGER.info("Saving "+ results);

        for (PlayoffResult playoffResult : results) {
            PlayoffGame game = playoffResult.getGame();
            Entity entity = new Entity(RESULT_KIND, game.getGameIndex() + "");
            entity.setUnindexedProperty("home_team", game.getHomeTeam());
            entity.setUnindexedProperty("away_team", game.getAwayTeam());
            entity.setUnindexedProperty("result", playoffResult.getGameResult().toString());

            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            datastoreService.put(entity);
        }
        LOGGER.info("Results saved");

    }

    @Override
    public PlayoffResults getResults() {
        LOGGER.info("Getting results");
        PlayoffResults playoffResults = new PlayoffResults();

        List<PlayoffResult> results = new ArrayList<>(16);
        for (int gameIndex = 0; gameIndex < 16; gameIndex++) {
            PlayoffResult result = getResult(gameIndex);
            results.add(result);
        }
        playoffResults.setResults(results);

        return playoffResults;
    }

    private PlayoffResult getResult(int gameIndex) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.createKey(RESULT_KIND,
                                                                      gameIndex + ""));
            LOGGER.info(gameIndex + "- " + entity.getProperties());
            return resultFromEntity(gameIndex, entity);
        } catch (EntityNotFoundException e) {
            LOGGER.info("No result found for " + gameIndex);
            return defaultPlayoffResult(gameIndex);
        }
    }

    private PlayoffResult resultFromEntity(int gameIndex, Entity entity) {
        PlayoffResult playoffResult = new PlayoffResult();
        PlayoffGame playoffGame = new PlayoffGame();
        playoffGame.setGameIndex(gameIndex);
        playoffGame.setRound(PlayoffRound.fromGameIndex(gameIndex));
        playoffGame.setHomeTeam(entity.getProperty("home_team").toString());
        playoffGame.setAwayTeam(entity.getProperty("away_team").toString());
        playoffResult.setGameResult(parseGameResult(entity.getProperty("result").toString()));
        playoffResult.setGame(playoffGame);

        LOGGER.info("Read result: " + playoffResult);
        return playoffResult;
    }

    private GameResult parseGameResult(String result) {
        if (result == null) {
            return GameResult.UNKNOWN;
        }
        return GameResult.valueOf(result);
    }

    private PlayoffResult defaultPlayoffResult(int gameIndex) {
        PlayoffResult playoffResult = new PlayoffResult();
        PlayoffGame playoffGame = new PlayoffGame();
        playoffGame.setGameIndex(gameIndex);
        playoffGame.setRound(PlayoffRound.fromGameIndex(gameIndex));
        playoffGame.setHomeTeam("Hemmalag");
        playoffGame.setAwayTeam("Bortalag");
        playoffResult.setGame(playoffGame);
        return playoffResult;
    }

    private User getCurrentUser() {
        UserService userService = UserServiceFactory.getUserService();
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException();
        }
        return currentUser;
    }
}
