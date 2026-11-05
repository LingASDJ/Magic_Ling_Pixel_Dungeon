package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;

/**
 * 挑战图标的统一映射表。
 * 原先私有在 {@link WndChallenges} 中，现在供挑战设置与每日挑战弹窗共用。
 */
public final class ChallengeIcons {

    private ChallengeIcons() {
    }

    public static Image icon(int index) {
        if (index < 0 || index >= Challenges.NAME_IDS.length) {
            return Icons.get(Icons.PREFS);
        }

        switch (Challenges.NAME_IDS[index]) {
            case "no_food":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_1, new ItemSprite.Glowing(0xFF0000));
            case "no_armor":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_2, new ItemSprite.Glowing(0x8f8f8f));
            case "no_healing":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_3, new ItemSprite.Glowing(0xff0000));
            case "no_herbalism":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_4);
            case "swarm_intelligence":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_5, new ItemSprite.Glowing(0xffff00));
            case "darkness":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_6, new ItemSprite.Glowing(0x8f8f8f));
            case "no_scrolls":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_7, new ItemSprite.Glowing(0x00dd00));
            case "aquaphobia":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_8, new ItemSprite.Glowing(0xac89cc));
            case "champion_enemies":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_9, new ItemSprite.Glowing(0x333333));
            case "rlpt":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_10, new ItemSprite.Glowing(0xc6cc6c));
            case "sbsg":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_11, new ItemSprite.Glowing(0x3b1051));
            case "exsg":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_12, new ItemSprite.Glowing(0xd1ce9f));
            case "stronger_bosses":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_13, new ItemSprite.Glowing(0xff0000));
            case "dhxd":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_15, new ItemSprite.Glowing(0x384976));
            case "morelevel":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_16, new ItemSprite.Glowing(0x98bc76));
            case "cs":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_17, new ItemSprite.Glowing(0x08bed5));
            case "blood_died":
                return new ItemSprite(ItemSpriteSheet.CHALLANEESICON_14, new ItemSprite.Glowing(Window.GDX_COLOR));
            default:
                return Icons.get(Icons.PREFS);
        }
    }
}
