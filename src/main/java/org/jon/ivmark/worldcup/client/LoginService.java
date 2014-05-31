package org.jon.ivmark.worldcup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.jon.ivmark.worldcup.shared.LoginInfo;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

    LoginInfo login(String requestUri);

}
