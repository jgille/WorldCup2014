package org.jon.ivmark.worldcup.client.domain;

import org.jon.ivmark.worldcup.shared.PlayDto;

public class Play {
    private final GameId gameId;
    private boolean oneChecked;
    private boolean xChecked;
    private boolean twoChecked;

    public Play(int round, int gameNumber) {
        this(round, gameNumber, false, false, false);
    }

    public Play(int round, int gameNumber, boolean oneChecked, boolean xChecked, boolean twoChecked) {
        this.gameId = new GameId(round, gameNumber);
        this.oneChecked = oneChecked;
        this.xChecked = xChecked;
        this.twoChecked = twoChecked;
    }

    public GameId getGameId() {
        return gameId;
    }

    public boolean isOneChecked() {
        return oneChecked;
    }

    public boolean isXChecked() {
        return xChecked;
    }

    public boolean isTwoChecked() {
        return twoChecked;
    }

    public void setOne(boolean checked) {
        oneChecked = checked;
    }

    public void setX(boolean checked) {
        xChecked = checked;
    }

    public void setTwo(boolean checked) {
        twoChecked = checked;
    }

    public int numChecked() {
        return checkedToInt(oneChecked) + checkedToInt(xChecked) + checkedToInt(twoChecked);
    }

    private int checkedToInt(boolean checked) {
        return checked ? 1 : 0;
    }

    public PlayDto asDto() {
        PlayDto playDto = new PlayDto();
        playDto.checked = new boolean[3];
        playDto.checked[0] = oneChecked;
        playDto.checked[1] = xChecked;
        playDto.checked[2] = twoChecked;
        return playDto;
    }
}
