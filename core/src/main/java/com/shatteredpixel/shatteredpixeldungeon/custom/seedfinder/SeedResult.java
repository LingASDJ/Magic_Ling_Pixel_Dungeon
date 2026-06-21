package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import java.util.List;

public class SeedResult {

    public String fullLog;
    public List<String> matchedInfo;
    public String seedStr;
    public boolean success;

    public SeedResult(String fullLog, String seedStr, List<String> matchedInfo, boolean success) {
        this.fullLog = fullLog;
        this.seedStr = seedStr;
        this.matchedInfo = matchedInfo;
        this.success = success;
    }
}