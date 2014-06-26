package org.jon.ivmark.worldcup.playoffs.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlay;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffPlays;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResult;
import org.jon.ivmark.worldcup.shared.playoffs.PlayoffResults;

import java.util.List;

public interface PlayoffServiceAsync {
    void saveResults(List<PlayoffResult> results, AsyncCallback<Void> async);

    void getResults(AsyncCallback<PlayoffResults> async);

    void savePlays(List<PlayoffPlay> plays, AsyncCallback<Void> async);

    void getPlays(AsyncCallback<PlayoffPlays> async);

    void getAllPlays(AsyncCallback<List<PlayoffPlays>> async);
}
