package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/**
 * 死灵猎手（necro_predator，32x16 帧）。
 *
 * <p>待机 0~5（速度 6）、行走 6~13（速度 16）、攻击 14~17（速度 12）、死亡 18~21（速度 12）。</p>
 */
public class NecroHunterSprite extends MobSprite {

    public NecroHunterSprite() {
        super();

        texture(Assets.Sprites.NECRO_PREDATOR);

        TextureFilm frames = new TextureFilm(texture, 32, 16);

        idle = new Animation(6, true);
        idle.frames(frames, 0, 1, 2, 3, 4, 5);

        run = new Animation(16, true);
        run.frames(frames, 6, 7, 8, 9, 10, 11, 12, 13);

        attack = new Animation(12, false);
        attack.frames(frames, 14, 15, 16, 17);

        die = new Animation(12, false);
        die.frames(frames, 18, 19, 20, 21);

        play(idle);
    }
}
