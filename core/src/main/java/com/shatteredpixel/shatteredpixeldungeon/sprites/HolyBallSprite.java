package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

/**
 * 圣光球的 Sprite 基类。
 *
 * <p>红/绿/蓝三种球分别使用 {@link HolyBallRedSprite}、
 * {@link HolyBallGreenSprite}、{@link HolyBallBlueSprite}
 */
public class HolyBallSprite extends MobSprite {

    private static final int FRAMES_PER_ROW = 12;

    protected final TextureFilm frames;

    protected HolyBallSprite(int colorType) {
        super();

        texture(Assets.Sprites.ENERGY_CORE);

        frames = new TextureFilm(texture, 26, 20);
        int row = colorType * FRAMES_PER_ROW;

        idle = new Animation(12, true);
        idle.frames(frames,
                row + 1, row + 2, row + 3, row + 4, row + 5, row + 6);

        die = new Animation(16, false);
        die.frames(frames,
                row + 7, row + 8, row + 9, row + 10, row + 11);

        run = idle.clone();
        attack = idle.clone();

        play(idle);
    }
}
