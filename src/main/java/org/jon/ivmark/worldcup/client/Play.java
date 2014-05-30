package org.jon.ivmark.worldcup.client;

class Play {
   private final GameId gameId;
    private boolean oneChecked;
    private boolean xChecked;
    private boolean twoChecked;

    Play(int round, int gameNumber) {
        this(round, gameNumber, false, false, false);
    }

    Play(int round, int gameNumber, boolean oneChecked, boolean xChecked, boolean twoChecked) {
        this.gameId = new GameId(round, gameNumber);
        this.oneChecked = oneChecked;
        this.xChecked = xChecked;
        this.twoChecked = twoChecked;
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
}
