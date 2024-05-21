package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM275Sprite extends MobSprite {

    public DM275Sprite() {
        super();
        texture( Assets.Sprites.DM275 );

        TextureFilm texturefilm = new TextureFilm(texture, 25, 22);
        idle = new Animation(10, true);
        idle.frames(texturefilm,0,1);

        run = new Animation(10, true);
        run.frames(texturefilm, 0,1,2);

        attack = new Animation(15, false);
        attack.frames(texturefilm,3,4,5,6,7);

        die = new Animation(20, false);
        die.frames(texturefilm, 0,7,0,7,0,7,0,7,0,7,0,7,8);
        play(idle);

        zap = attack.clone();
        toss = attack.clone();
    }

    public void toss( int cell ) {

        turnTo( ch.pos , cell );
        play( toss );

        ((MissileSprite)parent.recycle( MissileSprite.class )).
                reset(ch.pos, cell, ((DM275)ch).missile, new Callback() {
                    public void call() {
                        ((DM275)ch).onTossComplete();
                    }
                });
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.TOXIC_VENT,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((DM275)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.GAS );
        //GLog.w(Messages.get(DM275.class, "vent"));
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap || anim == toss) {
            idle();
        }
        super.onComplete( anim );
    }

}
