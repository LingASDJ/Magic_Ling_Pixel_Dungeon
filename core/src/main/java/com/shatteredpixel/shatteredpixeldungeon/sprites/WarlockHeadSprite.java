package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.WarlockHead;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WarlockHeadSprite extends MobSprite {

    public WarlockHeadSprite() {
        super();

        texture(Assets.Sprites.WARLOCKHEAD);

        TextureFilm frames = new TextureFilm(texture, 12, 15);

        idle = new Animation(1, true);
        idle.frames(frames, 0, 0, 1);

        run = new Animation(11, true);
        run.frames(frames,  2,3,4,5,6
        );

        attack = new Animation(11, false);
        attack.frames(frames, 7,8);

        zap = attack.clone();

        die = new Animation(11, false);
        die.frames(frames, 9,10,11,12);

        play(idle);
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((WarlockHead)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}

