package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.watabou.noosa.TextureFilm;
import com.watabou.utils.GameMath;

public class RedSprites extends MobSprite {

	private static final int[] TIER_FRAMES = {0, 21, 32, 43, 54, 65};

	public RedSprites() {
		super();

		texture("Boss/SLMKING0.png");

		TextureFilm frames = new TextureFilm(texture, 20, 14);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 2, 1, 2, 2);

		attack = new Animation(12, false);
		attack.frames(frames, 8, 9, 10);

		die = new Animation(12, false);
		die.frames(frames, 5, 6, 7);

		play(idle);
	}

	public void setArmor(int tier) {
		int offset = TIER_FRAMES[(int)GameMath.gate(0, tier, TIER_FRAMES.length - 1)];

		TextureFilm frames = new TextureFilm(texture, 12, 15);

		idle.frames(frames, 0 + offset, 0 + offset, 0 + offset, 0 + offset,
				0 + offset, 1 + offset, 1 + offset);
		run.frames(frames, 2 + offset, 3 + offset, 4 + offset,
				5 + offset, 6 + offset, 7 + offset);
		attack.frames(frames, 8 + offset, 9 + offset, 10 + offset);
		// 死亡动画始终使用未穿护甲的贴图。

		play(idle, true);
	}

	@Override
	public int blood() {
		return 0xFFCDCDB7;
	}
}


