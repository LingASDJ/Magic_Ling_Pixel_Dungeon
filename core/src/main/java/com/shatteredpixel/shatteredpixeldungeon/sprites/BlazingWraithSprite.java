package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/**
 * 炽烈亡魂（blazing_wraith，16x23 帧）。
 *
 * <p>待机 0~5（速度 6）、攻击 6~9（速度 12）、死亡 10~15（速度 16）、行走 16~21（速度 6）。</p>
 */
public class BlazingWraithSprite extends MobSprite {

    public BlazingWraithSprite() {
        super();

        texture(Assets.Sprites.BLAZING_WRAITH);

        TextureFilm frames = new TextureFilm(texture, 16, 23);

        idle = new Animation(6, true);
        idle.frames(frames, 0, 1, 2, 3, 4, 5);

        attack = new Animation(12, false);
        attack.frames(frames, 6, 7, 8, 9);

        die = new Animation(16, false);
        die.frames(frames, 10, 11, 12, 13, 14, 15);

        run = new Animation(6, true);
        run.frames(frames, 16, 17, 18, 19, 20, 21);

        play(idle);
    }
}
