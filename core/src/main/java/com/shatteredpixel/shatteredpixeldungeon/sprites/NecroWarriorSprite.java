package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/**
 * 死灵勇士（necro_warrior，16x17 帧）。
 *
 * <p>待机 0~5（速度 3）、行走 6~11（速度 6）、攻击 12~15（速度 12）、死亡 16~19（速度 12）。</p>
 */
public class NecroWarriorSprite extends MobSprite {

    public NecroWarriorSprite() {
        super();

        texture(Assets.Sprites.NECRO_WARRIOR);

        TextureFilm frames = new TextureFilm(texture, 16, 17);

        idle = new Animation(3, true);
        idle.frames(frames, 0, 1, 2, 3, 4, 5);

        run = new Animation(6, true);
        run.frames(frames, 6, 7, 8, 9, 10, 11);

        attack = new Animation(12, false);
        attack.frames(frames, 12, 13, 14, 15);

        die = new Animation(12, false);
        die.frames(frames, 16, 17, 18, 19);

        play(idle);
    }
}
