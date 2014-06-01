package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.Result;

import java.util.List;

@RemoteServiceRelativePath("result")
public interface ResultsService extends RemoteService {

    List<Result> loadResults();

    void saveResult(Result result);
}
