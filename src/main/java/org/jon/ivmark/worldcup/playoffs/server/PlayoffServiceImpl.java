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
import org.jon.ivmark.worldcup.shared.playoffs.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlayoffServiceImpl extends RemoteServiceServlet implements PlayoffService {

    private static final String RESULT_KIND = "playoff_results";
    private static final String PLAY_KIND = "playoff_plays";

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

    @Override
    public void savePlays(List<PlayoffPlay> plays) {
        User currentUser = getCurrentUser();
        LOGGER.info("Saving plays for " + currentUser.getNickname());

        for (PlayoffPlay playoffPlay : plays) {
            PlayoffGame game = playoffPlay.getGame();
            Entity entity = new Entity(PLAY_KIND, getPlayKey(currentUser, game.getGameIndex()));
            entity.setUnindexedProperty("play", checkedToString(playoffPlay.getChecked()));

            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            datastoreService.put(entity);
        }
        LOGGER.info("Plays saved");
    }

    private String checkedToString(List<Boolean> checked) {
        String s = "";
        if (checked.get(0)) {
            s += "1";
        }
        if (checked.get(1)) {
            s += "X";
        }
        if (checked.get(2)) {
            s += "2";
        }
        return s;
    }

    private List<Boolean> checkedFromString(String checkedString) {
        if (checkedString == null) {
            checkedString = "";
        }
        List<Boolean> checked = new ArrayList<>(3);
        checked.add(checkedString.contains("1"));
        checked.add(checkedString.contains("X"));
        checked.add(checkedString.contains("2"));
        return checked;
    }

    private String getPlayKey(User currentUser, int gameIndex) {
        return currentUser.getUserId() + "." + gameIndex;
    }

    @Override
    public PlayoffPlays getPlays() {
        List<PlayoffResult> results = getResults().getResults();

        User currentUser = getCurrentUser();
        LOGGER.info("Getting plays");
        PlayoffPlays playoffPlays = new PlayoffPlays();

        List<PlayoffPlay> plays = new ArrayList<>(16);
        for (int gameIndex = 0; gameIndex < 16; gameIndex++) {
            PlayoffResult playoffResult = results.get(gameIndex);
            PlayoffPlay play = getPlay(currentUser, gameIndex);
            play.getGame().setHomeTeam(playoffResult.getGame().getHomeTeam());
            play.getGame().setAwayTeam(playoffResult.getGame().getAwayTeam());
            GameResult gameResult = playoffResult.getGameResult();
            play.setGameResult(gameResult);
            plays.add(play);
        }

        playoffPlays.setPlays(plays);
        return playoffPlays;
    }

    private PlayoffPlay getPlay(User currentUser, int gameIndex) {
        String playKey = getPlayKey(currentUser, gameIndex);
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.createKey(PLAY_KIND, playKey));
            return playFromEntity(gameIndex, entity);
        } catch (EntityNotFoundException e) {
            LOGGER.info("No play found for " + gameIndex);
            return defaultPlay(gameIndex);
        }
    }

    private PlayoffPlay defaultPlay(int gameIndex) {
        PlayoffPlay playoffPlay = new PlayoffPlay();
        PlayoffGame game = new PlayoffGame();
        game.setGameIndex(gameIndex);
        game.setRound(PlayoffRound.fromGameIndex(gameIndex));
        playoffPlay.setGame(game);
        return playoffPlay;
    }

    private PlayoffPlay playFromEntity(int gameIndex, Entity entity) {
        PlayoffPlay playoffPlay = new PlayoffPlay();
        PlayoffGame game = new PlayoffGame();
        game.setGameIndex(gameIndex);
        game.setRound(PlayoffRound.fromGameIndex(gameIndex));
        List<Boolean> checked = checkedFromString((String) entity.getProperty("play"));
        playoffPlay.setGame(game);
        playoffPlay.setChecked(checked);
        return playoffPlay;
    }

    private PlayoffResult getResult(int gameIndex) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        try {
            Entity entity = datastoreService.get(KeyFactory.createKey(RESULT_KIND,
                                                                      gameIndex + ""));
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
