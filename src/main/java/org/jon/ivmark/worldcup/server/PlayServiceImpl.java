package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jon.ivmark.worldcup.client.PlayService;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlayServiceImpl extends RemoteServiceServlet implements PlayService {

    private final PlaysRepository playsRepository = new DataStorePlaysRepository();
    private final TeamRepository teamRepository = new DataStoreTeamRepository();

    private static final Logger LOGGER = Logger.getLogger(DataStorePlaysRepository.class.getName());

    @Override
    public void savePlay(PlaysDto plays) {
        if (!CutOff.isBeforeCutOff()) {
            throw new IllegalStateException("Too late!");
        }
        User currentUser = getCurrentUser();
        LOGGER.info("Saving play for user: " + currentUser.getNickname());
        playsRepository.save(currentUser, plays);
    }

    @Override
    public List<PlaysDto> loadPlays() {
        User currentUser = getCurrentUser();
        LOGGER.info("Loading plays for user: " + currentUser.getNickname());

        List<PlaysDto> playsDtos = new ArrayList<>();
        for (int roundIndex = 0; roundIndex < Round.NUM_ROUNDS; roundIndex++) {
            PlaysDto playsDto = playsRepository.get(currentUser, roundIndex);
            playsDtos.add(playsDto);
        }
        return playsDtos;
    }

    @Override
    public String getTeamName() {
        User currentUser = getCurrentUser();
        return teamRepository.getTeamName(currentUser);
    }

    @Override
    public void setTeamName(String teamName) {
        User currentUser = getCurrentUser();
        teamRepository.setTeamName(currentUser, teamName);
    }

    @Override
    public Map<String, List<PlaysDto>> loadAllCompletePlays() {
        Map<String, List<PlaysDto>> result = new HashMap<>();

        if (CutOff.isBeforeCutOff()) {
            return result;
        }

        Map<String, List<PlaysDto>> playsByUser = playsRepository.getAll().byUserWithAllRoundsSubmitted();

        for (Map.Entry<String, List<PlaysDto>> e : playsByUser.entrySet()) {
            String userId = e.getKey();
            String teamName = teamRepository.getTeamName(userId);
            result.put(teamName, e.getValue());
        }
        LOGGER.info("Loaded all plays");
        return result;
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
