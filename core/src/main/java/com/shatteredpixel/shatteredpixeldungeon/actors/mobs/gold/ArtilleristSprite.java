package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class ArtilleristSprite  extends MobSprite {
    public ArtilleristSprite() {
        super();

        texture(Assets.Sprites.ARTILLERIST);

        TextureFilm frames = new TextureFilm( texture, 22, 16 );
        idle = new Animation( 1, true );
        idle.frames( frames, 0,0,1);

        run = new Animation( 11, true );
        run.frames( frames,  2,3,4, 5, 6 ,7);

        attack = new Animation( 9, false );
        attack.frames( frames, 8,9,10);

        zap = attack.clone();

        die = new Animation( 11, false );
        die.frames( frames, 11,12,13,14,15,16,17);

        play(idle);
    }
    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();

        }
        super.onComplete( anim );
    }
    public void zap( int cell ) {

        super.zap( cell );

        ((MissileSprite)parent.recycle( MissileSprite.class )).
                reset( this, cell, new Bomb(), new Callback() {
                    @Override
                    public void call() {
                        ((Artillerist)ch).onZapComplete(cell);
                    }
                } );
    }
    public void targeting( int pos ){
        turnTo(ch.pos, pos);
        play(idle);
    }
}
