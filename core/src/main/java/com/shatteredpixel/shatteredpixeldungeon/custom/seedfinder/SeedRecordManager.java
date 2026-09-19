package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/** 查种记录的保存/读取/对比，最多 5 份，新的顶在前面，超出丢弃最旧 */
public class SeedRecordManager {

    private static final String PREFS_NAME = "SeedFinderRecords";
    private static final int MAX_RECORDS = 5;
    private static final String KEY_PREFIX = "record_";

    private static Preferences prefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static List<SeedRecord> loadAll() {
        Preferences p = prefs();
        List<SeedRecord> list = new ArrayList<>();
        Json json = new Json();
        for (int i = 0; i < MAX_RECORDS; i++) {
            String s = p.getString(KEY_PREFIX + i, null);
            if (s != null && !s.isEmpty()) {
                try {
                    list.add(json.fromJson(SeedRecord.class, s));
                } catch (Exception ignored) {
                    // 损坏的记录直接跳过
                }
            }
        }
        return list;
    }

    public static void saveRecord(SeedRecord rec) {
        Preferences p = prefs();
        Json json = new Json();
        List<SeedRecord> list = loadAll();
        list.add(0, rec);
        while (list.size() > MAX_RECORDS) list.remove(list.size() - 1);
        for (int i = 0; i < MAX_RECORDS; i++) {
            String key = KEY_PREFIX + i;
            if (i < list.size()) p.putString(key, json.toJson(list.get(i)));
            else p.remove(key);
        }
        p.flush();
    }

    public static void deleteRecord(int index) {
        Preferences p = prefs();
        List<SeedRecord> list = loadAll();
        if (index < 0 || index >= list.size()) return;
        list.remove(index);
        for (int i = 0; i < MAX_RECORDS; i++) {
            String key = KEY_PREFIX + i;
            if (i < list.size()) p.putString(key, new Json().toJson(list.get(i)));
            else p.remove(key);
        }
        p.flush();
    }

    public static String compare(SeedRecord a, SeedRecord b) {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.get(SeedRecordManager.class, "compare_title")).append("\n");
        sb.append(Messages.get(SeedRecordManager.class, "compare_a"))
                .append(a.seedCode)
                .append("  ").append(Messages.get(SeedRecordManager.class, "range", 0, a.floors))
                .append("\n");
        sb.append(Messages.get(SeedRecordManager.class, "compare_b"))
                .append(b.seedCode)
                .append("  ").append(Messages.get(SeedRecordManager.class, "range", 0, b.floors))
                .append("\n\n");

        // 合并整个区域(0~N层)的物品，并记录每个物品出现在哪几层
        Map<String, List<Integer>> floorsA = toFloorMap(a);
        Map<String, List<Integer>> floorsB = toFloorMap(b);

        Set<String> allA = new TreeSet<>(floorsA.keySet());
        Set<String> allB = new TreeSet<>(floorsB.keySet());

        Set<String> common = new TreeSet<>(allA); common.retainAll(allB);
        Set<String> onlyA  = new TreeSet<>(allA); onlyA.removeAll(allB);
        Set<String> onlyB  = new TreeSet<>(allB); onlyB.removeAll(allA);

        if (common.isEmpty() && onlyA.isEmpty() && onlyB.isEmpty()) {
            sb.append(Messages.get(SeedRecordManager.class, "no_diff")).append("\n");
            return sb.toString();
        }

        sb.append(Messages.get(SeedRecordManager.class, "tag_common")).append("\n");
        appendItemWithFloors(sb, common, floorsA, floorsB);
        sb.append("\n");

        sb.append(Messages.get(SeedRecordManager.class, "tag_only_a")).append("\n");
        appendItemWithFloors(sb, onlyA, floorsA, null);
        sb.append("\n");

        sb.append(Messages.get(SeedRecordManager.class, "tag_only_b")).append("\n");
        appendItemWithFloors(sb, onlyB, floorsB, null);

        return sb.toString();
    }

    /** floorItems: {层 -> [物品]} 转成 物品 -> [出现在哪几层] */
    private static Map<String, List<Integer>> toFloorMap(SeedRecord rec) {
        Map<String, List<Integer>> map = new TreeMap<>();
        if (rec.floorItems == null) return map;
        for (Map.Entry<Integer, List<String>> e : rec.floorItems.entrySet()) {
            int floor = e.getKey();
            for (String item : e.getValue()) {
                map.computeIfAbsent(item, k -> new ArrayList<>()).add(floor);
            }
        }
        return map;
    }

    /** 输出物品名，并在括号里标注它出现在哪几层；两侧都有时取A侧楼层 */
    private static void appendItemWithFloors(StringBuilder sb, Set<String> items,
                                             Map<String, List<Integer>> floorsA,
                                             Map<String, List<Integer>> floorsB) {
        boolean first = true;
        for (String item : items) {
            if (!first) sb.append("\n");
            sb.append(" - ").append(item);
            List<Integer> fl = floorsA != null && floorsA.containsKey(item)
                    ? floorsA.get(item)
                    : (floorsB != null ? floorsB.get(item) : null);
            if (fl != null && !fl.isEmpty()) {
                sb.append(Messages.get(SeedRecordManager.class, "at_floors", fl.toString()));
            }
            first = false;
        }
    }

}
