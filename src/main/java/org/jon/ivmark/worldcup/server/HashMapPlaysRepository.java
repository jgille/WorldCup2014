package org.jon.ivmark.worldcup.server;

import com.google.appengine.api.users.User;
import org.jon.ivmark.worldcup.client.domain.Round;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.HashMap;
import java.util.Map;

public class HashMapPlaysRepository implements PlaysRepository {

    private final Map<Key, PlaysDto> repo = new HashMap<>();

    @Override
    public void save(User user, PlaysDto playsDto) {
        System.out.println("Saving plays for " + user);
        repo.put(new Key(user.getUserId(), playsDto.roundIndex), playsDto);
    }

    @Override
    public PlaysDto get(User user, int roundIndex) {
        PlaysDto playsDto = repo.get(new Key(user.getUserId(), roundIndex));
        if (playsDto == null) {
            playsDto = new Round(roundIndex).asDto();
        }
        System.out.println("Loading plays for " + user);
        System.out.println(playsDto);
        return playsDto;
    }

    private static class Key {
        private final String user;
        private final int roundIndex;

        private Key(String user, int roundIndex) {
            this.user = user;
            this.roundIndex = roundIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Key key = (Key) o;

            return roundIndex == key.roundIndex && user.equals(key.user);

        }

        @Override
        public int hashCode() {
            int result = user.hashCode();
            result = 31 * result + roundIndex;
            return result;
        }
    }
}
