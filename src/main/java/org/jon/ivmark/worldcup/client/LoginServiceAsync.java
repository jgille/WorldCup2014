package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.jon.ivmark.worldcup.shared.LoginInfo;

public interface LoginServiceAsync {

    void login(String requestUri, AsyncCallback<LoginInfo> callback);

}
