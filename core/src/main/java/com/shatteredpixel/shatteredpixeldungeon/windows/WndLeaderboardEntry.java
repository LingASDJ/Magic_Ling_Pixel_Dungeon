package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.services.daily.LeaderboardData;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

/**
 * Player details for a daily leaderboard entry. The UUID is deliberately not
 * shown in the list itself; it appears here as small grey text under the name.
 */
public class WndLeaderboardEntry extends Window {

    private static final int WIDTH = 140;
    private static final int MARGIN = 6;

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
