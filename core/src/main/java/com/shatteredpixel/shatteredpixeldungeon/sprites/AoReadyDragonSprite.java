package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.AoReadyDragon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class AoReadyDragonSprite extends MobSprite {

    private static final float DURATION = 2f;
    private Animation cast;

    public AoReadyDragonSprite() {
        super();

        texture(Assets.Sprites.AOREADY);

        TextureFilm frames = new TextureFilm(texture, 16, 16);

        idle = new Animation(2, true);
        idle.frames(frames, 0, 1, 0, 1, 0, 1, 0);

        run = new Animation(6, true);
        run.frames(frames, 0, 2, 1);

        attack = new Animation(12, false);
        attack.frames(frames, 0, 2, 3, 4, 5);

        cast = attack.clone();

        die = new Animation(8, false);
        die.frames(frames, 0, 5, 6, 7, 8, 9, 9, 9);

        play(run.clone());
    }



    @Override
    public void zap(int cell) {

        turnTo(ch.pos, cell);
        play(zap);

        MagicMissile.boltFromChar( parent,
                MagicMissile.FROST,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((AoReadyDragon)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }
}
