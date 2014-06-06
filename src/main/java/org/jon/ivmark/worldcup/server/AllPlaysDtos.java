package org.jon.ivmark.worldcup.server;

import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllPlaysDtos {

    private final List<PlaysDto> allPlays;

    public AllPlaysDtos(List<PlaysDto> allPlays) {
        this.allPlays = allPlays;
    }

    public Map<String, List<PlaysDto>> byUserWithAllRoundsSubmitted() {
        Map<String, List<PlaysDto>> byUser = new HashMap<>();

        for (PlaysDto playsDto : allPlays) {
            List<PlaysDto> forUser = byUser.get(playsDto.userId);
            if (forUser == null) {
                forUser = new ArrayList<>();
                byUser.put(playsDto.userId, forUser);
            }
            forUser.add(playsDto);
        }
        return filterOutIncomplete(byUser);
    }

    private Map<String, List<PlaysDto>> filterOutIncomplete(Map<String, List<PlaysDto>> byUser) {
        Map<String, List<PlaysDto>> complete = new HashMap<>();

        for (Map.Entry<String, List<PlaysDto>> e : byUser.entrySet()) {
            if (e.getValue().size() == 3) {
                complete.put(e.getKey(), e.getValue());
            }
        }
        return complete;
    }
}
