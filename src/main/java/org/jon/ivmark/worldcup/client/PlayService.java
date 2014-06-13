package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.PlaysDto;
import org.jon.ivmark.worldcup.shared.SimilarityMatrix;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("play")
public interface PlayService extends RemoteService {

    void savePlay(PlaysDto plays);

    List<PlaysDto> loadPlays();

    String getTeamName();

    void setTeamName(String teamName);

    Map<String, List<PlaysDto>> loadAllCompletePlays();

    SimilarityMatrix loadSimilarities();

}
