package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 一条已保存的查种记录（可被 Gdx Json 序列化） */
public class SeedRecord {

    public String seed = "";
    public String seedCode = "";
    public int challenges;
    public int floors;
    public String condition = "";
    public long time;
    public List<String> matchedInfo = new ArrayList<>();

    /** 楼层 -> 该层物品名列表（用于对比） */
    public Map<Integer, List<String>> floorItems = new HashMap<>();

    public SeedRecord() {} // Json 反序列化需要无参构造

    public String getDisplayTitle() {
        String t = new java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault()).format(time);
        return seedCode + "  [" + t + "]";
    }
}
