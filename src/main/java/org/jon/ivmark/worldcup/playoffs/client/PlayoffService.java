package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResult;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResults;

import java.util.List;

@RemoteServiceRelativePath("playoff")
public interface PlayoffService extends RemoteService {

    void saveResults(List<PlayoffResult> results);

    PlayoffResults getResults();
}
