package com.shatteredpixel.shatteredpixeldungeon.services.daily;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndLeaderboard;

public class SubmitResultData {
    public int code;
    public String message;
    public Data data;

    public static class Data {
        public int rank;
        public int totalPlayers;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public String getError() {
        switch (code) {
            case 1001: return Messages.get(WndLeaderboard.class,"already_played");
            case 1002: return Messages.get(WndLeaderboard.class,"error_seed");
            case 1003: return Messages.get(WndLeaderboard.class,"hack");
            default:   return message != null ? message : Messages.get(WndLeaderboard.class,"unknown");
        }
    }
}