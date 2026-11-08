package com.shatteredpixel.shatteredpixeldungeon.multiplayer;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyIdentity;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 本机联机个人资料：UUID、名字、佩戴徽章（最多 3 枚）与历史最高分。
 *
 * <p>UUID 由 {@link DailyIdentity} 统一管理；名字由 {@link Mp} 管理；
 * 最高分从 {@link Rankings} 的本地记录实时计算（每日挑战记录一并参与比较）。
 * 只有佩戴徽章需要单独持久化到 mp_profile.dat。</p>
 */
public class MpProfile {

    /** 最多佩戴的徽章数。 */
    public static final int MAX_BADGES = 3;

    private static final String FILE_NAME = "mp_profile.dat";
    private static final String BADGES = "badges";

    private final List<Badges.Badge> badges = new ArrayList<>();

    /** 从磁盘读取本机资料（文件不存在/损坏时返回空徽章）。 */
    public static MpProfile load() {
        MpProfile profile = new MpProfile();
        try {
            Bundle bundle = FileUtils.bundleFromFile(FILE_NAME);
            if (bundle != null) {
                String[] names = bundle.getStringArray(BADGES);
                if (names != null) {
                    Set<Badges.Badge> allowed = new HashSet<>(selectableBadges());
                    for (String name : names) {
                        if (profile.badges.size() >= MAX_BADGES) break;
                        if (name == null || name.isEmpty()) continue;
                        try {
                            Badges.Badge badge = Badges.Badge.valueOf(name);
                            if (allowed.contains(badge) && !profile.badges.contains(badge)) {
                                profile.badges.add(badge);
                            }
                        } catch (IllegalArgumentException ignored) {}
                    }
                }
            }
        } catch (Throwable ignored) {}
        return profile;
    }

    /** 保存佩戴徽章到 mp_profile.dat。 */
    public void save() {
        Bundle bundle = new Bundle();
        String[] names = new String[badges.size()];
        for (int i = 0; i < badges.size(); i++) {
            names[i] = badges.get(i).name();
        }
        bundle.put(BADGES, names);
        try {
            FileUtils.bundleToFile(FILE_NAME, bundle);
        } catch (IOException ignored) {}
    }

    /** 展示用 UUID（与每日挑战一致）。 */
    public String getUuid() {
        return DailyIdentity.getPlayerId();
    }

    /** 当前玩家名（由 Mp 持久化）。 */
    public String getName() {
        return Mp.get().getMyName();
    }

    /** 当前佩戴的徽章（只读，最多 3 枚）。 */
    public List<Badges.Badge> getBadges() {
        return Collections.unmodifiableList(badges);
    }

    /**
     * 设置佩戴徽章：只保留已解锁且未被替换的徽章，最多 3 枚。
     *
     * @param selected 新选择（可为 null）。
     */
    public void setBadges(List<Badges.Badge> selected) {
        Set<Badges.Badge> allowed = new HashSet<>(selectableBadges());
        badges.clear();
        if (selected != null) {
            for (Badges.Badge badge : selected) {
                if (badge == null || !allowed.contains(badge) || badges.contains(badge)) continue;
                badges.add(badge);
                if (badges.size() >= MAX_BADGES) break;
            }
        }
        save();
        Mp.get().onLocalProfileChanged();
    }

    /** 本机已拥有的全部徽章（个人主页徽章页展示/同步用）。 */
    public List<Badges.Badge> getAllBadges() {
        return selectableBadges();
    }

    /** 可选择佩戴的徽章：已解锁、非隐藏、未被更高等级替换。 */
    public static List<Badges.Badge> selectableBadges() {
        List<Badges.Badge> list = new ArrayList<>(Badges.allUnlocked());
        list = Badges.filterReplacedBadges(list);
        Iterator<Badges.Badge> it = list.iterator();
        while (it.hasNext()) {
            Badges.Badge badge = it.next();
            if (badge == null || badge.type == null
                    || badge.type == Badges.BadgeType.HIDDEN || badge.image < 0) {
                it.remove();
            }
        }
        return list;
    }

    /**
     * 历史最高分对局：遍历本地排行榜记录（含每日挑战），取 score 最大者。
     *
     * @return 最高分记录；没有记录时返回 null。
     */
    public static Rankings.Record bestRecord() {
        Rankings.INSTANCE.load();
        Rankings.Record best = null;
        for (Rankings.Record record : Rankings.INSTANCE.records) {
            if (record == null) continue;
            if (best == null || record.score > best.score) best = record;
        }
        return best;
    }

    /** 历史最高分（没有记录时为 0）。 */
    public int getHighestScore() {
        Rankings.Record record = bestRecord();
        return record == null ? 0 : record.score;
    }

    /**
     * 把一条排行榜记录序列化为可走网络的 bundle 字节。
     *
     * @param record 记录。
     * @return bundle 字节。
     * @throws IOException 写入失败。
     */
    public static byte[] encodeRecord(Rankings.Record record) throws IOException {
        Bundle bundle = new Bundle();
        record.storeInBundle(bundle);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bundle.write(bundle, bos);
        bos.flush();
        return bos.toByteArray();
    }

    /**
     * 从网络字节还原排行榜记录。
     *
     * @param data bundle 字节。
     * @return 记录。
     * @throws IOException 读取失败。
     */
    public static Rankings.Record decodeRecord(byte[] data) throws IOException {
        Bundle bundle = Bundle.read(new ByteArrayInputStream(data));
        Rankings.Record record = new Rankings.Record();
        record.restoreFromBundle(bundle);
        return record;
    }
}
