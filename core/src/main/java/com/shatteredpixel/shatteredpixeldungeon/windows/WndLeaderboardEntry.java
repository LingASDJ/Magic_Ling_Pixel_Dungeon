package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Rankings;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.CollectRankings;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.DailyUtils;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.LeaderboardData;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

/**
 * Player details for a daily leaderboard entry. The UUID is deliberately not
 * shown in the list itself; it appears here as small grey text under the name.
 */
public class WndLeaderboardEntry extends Window {

    private static final int WIDTH = 140;
    private static final int MARGIN = 6;

    /**
     * Opens the full game-log ranking detail (stats, talents, items, badges,
     * challenges) for a daily entry. Returns false when the entry has no
     * complete snapshot, in which case the compact fallback window is used.
     */
    public static boolean open(LeaderboardData.Entry entry) {
        Rankings.Record rec = buildRecord(entry);
        if (rec == null) return false;
        try {
            // WndRanking's StatsTab queries CollectRankings; initialize it like
            // RankingsScene does before opening a record.
            CollectRankings.INSTANCE.load();
            ShatteredPixelDungeon.scene().addToFront(new WndRanking(rec));
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static Rankings.Record buildRecord(LeaderboardData.Entry entry) {
        if (entry == null || entry.gameData == null || entry.gameData.isEmpty()) return null;

        Bundle data;
        Bundle stats;
        try {
            data = Bundle.fromString(entry.gameData);
            if (data == null || data.isNull()) return null;
            Bundle hero = data.getBundle("hero");
            if (hero == null || hero.isNull() || !hero.contains("class")) return null;

            // The detail window title uses hero.name(); use the same multiplayer
            // name shown in the leaderboard row.
            if (entry.playerName != null && !entry.playerName.trim().isEmpty()) {
                hero.put("name", entry.playerName.trim());
                data.put("hero", hero);
            }

            // WndRanking expects the same game-data shape as local rankings:
            // nested stats/badges/handlers plus daily flags. The server snapshot
            // is flat, so clone it as the stats bundle and add empty extras.
            stats = Bundle.fromString(data.toString());
            data.put("stats", stats);
            data.put("badges", new Bundle());
            data.put("handlers", new Bundle());
            data.put("daily", true);
            data.put("daily_replay", false);
        } catch (Throwable t) {
            return null;
        }

        String date = entry.date == null || entry.date.isEmpty()
                ? DailyUtils.todayUtc() : entry.date;
        if (!data.contains("custom_seed")) data.put("custom_seed", date);

        Rankings.Record rec = new Rankings.Record();
        rec.gameData = data;
        rec.win = entry.won;
        rec.score = entry.score;
        rec.heroClass = entry.heroClass == null ? HeroClass.WARRIOR : entry.heroClass;
        rec.armorTier = entry.armorTier;
        rec.herolevel = entry.heroLevel;
        rec.depth = entry.depth;
        rec.daily = true;
        rec.customSeed = date;
        rec.date = date;
        rec.version = entry.version == null ? "" : entry.version;
        rec.gameID = (entry.playerId == null ? "" : entry.playerId) + "-" + date;
        return rec;
    }

    public WndLeaderboardEntry(LeaderboardData.Entry entry) {
        super();

        float pos = MARGIN;

        // Selected hero avatar at the top-left, player name to its right.
        Image avatar = HeroSprite.avatar(entry.heroClass, entry.armorTier);
        avatar.x = MARGIN;
        avatar.y = pos;
        add(avatar);

        float textX = MARGIN + avatar.width() + 4;
        RenderedTextBlock name = PixelScene.renderTextBlock(
                entry.playerName == null ? "" : entry.playerName, 9);
        name.hardlight(TITLE_COLOR);
        name.maxWidth((int) Math.max(1, WIDTH - textX - MARGIN));
        name.setPos(textX, pos);
        add(name);

        float headerBottom = Math.max(avatar.y + avatar.height(), name.bottom());
        if (entry.playerId != null && !entry.playerId.isEmpty()) {
            RenderedTextBlock uuid = PixelScene.renderTextBlock(
                    Messages.get(this, "uuid") + ": " + entry.playerId, 6);
            uuid.hardlight(0x888888);
            uuid.maxWidth((int) Math.max(1, WIDTH - textX - MARGIN));
            uuid.setPos(textX, name.bottom() + 1);
            add(uuid);
            headerBottom = Math.max(headerBottom, uuid.bottom());
        }
        pos = headerBottom + 4;

        // Challenge count/content is intentionally not shown: every player
        // receives the same daily configuration.
        pos = addRow("rank", Integer.toString(entry.rank), pos);
        pos = addRow("score", Integer.toString(entry.score), pos);
        pos = addRow("depth", Integer.toString(entry.depth), pos);
        pos = addRow("won", Messages.get(this, entry.won ? "yes" : "no"), pos);

        resize(WIDTH, (int) Math.ceil(pos) + MARGIN);
    }

    private float addRow(String key, String value, float y) {
        RenderedTextBlock label = PixelScene.renderTextBlock(Messages.get(this, key), 7);
        label.hardlight(TITLE_COLOR);
        label.setPos(MARGIN, y);
        add(label);

        RenderedTextBlock text = PixelScene.renderTextBlock(value, 7);
        text.hardlight(0xDDDDDD);
        text.maxWidth(WIDTH / 2 - MARGIN);
        text.setPos(WIDTH / 2f, y);
        add(text);

        return Math.max(label.bottom(), text.bottom()) + 2;
    }
}
