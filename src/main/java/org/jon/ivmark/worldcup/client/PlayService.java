package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.LoginInfo;
import org.jon.ivmark.worldcup.shared.PlaysDto;

import java.util.List;

@RemoteServiceRelativePath("play")
public interface PlayService extends RemoteService {

    void savePlay(PlaysDto plays);

    List<PlaysDto> loadPlays();

}
