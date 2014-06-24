package org.jon.ivmark.worldcup.shared;

import java.io.Serializable;

public class LoginInfo implements Serializable {

    private boolean isAdmin = false;
    private boolean loggedIn = false;
    private String loginUrl;
    private String logoutUrl;
    private String emailAddress;
    private String nickname;
    private boolean maySubmitPlay = true;

    private boolean maySubmitEightsFinal;
    private boolean maySubmitQuarterFinal;
    private boolean maySubmitSemiFinal;
    private boolean maySubmitBronzeMatch;
    private boolean maySubmitFinal;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMaySubmitPlay(boolean maySubmitPlay) {
        this.maySubmitPlay = maySubmitPlay;
    }

    public boolean maySubmitPlay() {
        return maySubmitPlay;
    }

    public boolean isMaySubmitEightsFinal() {
        return maySubmitEightsFinal;
    }

    public void setMaySubmitEightsFinal(boolean maySubmitEightsFinal) {
        this.maySubmitEightsFinal = maySubmitEightsFinal;
    }

    public boolean isMaySubmitQuarterFinal() {
        return maySubmitQuarterFinal;
    }

    public void setMaySubmitQuarterFinal(boolean maySubmitQuarterFinal) {
        this.maySubmitQuarterFinal = maySubmitQuarterFinal;
    }

    public boolean isMaySubmitSemiFinal() {
        return maySubmitSemiFinal;
    }

    public void setMaySubmitSemiFinal(boolean maySubmitSemiFinal) {
        this.maySubmitSemiFinal = maySubmitSemiFinal;
    }

    public boolean isMaySubmitBronzeMatch() {
        return maySubmitBronzeMatch;
    }

    public void setMaySubmitBronzeMatch(boolean maySubmitBronzeMatch) {
        this.maySubmitBronzeMatch = maySubmitBronzeMatch;
    }

    public boolean isMaySubmitFinal() {
        return maySubmitFinal;
    }

    public void setMaySubmitFinal(boolean maySubmitFinal) {
        this.maySubmitFinal = maySubmitFinal;
    }
}
