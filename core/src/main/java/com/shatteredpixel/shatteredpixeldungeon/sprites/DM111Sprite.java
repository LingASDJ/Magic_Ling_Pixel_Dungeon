package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.DM111;
import com.shatteredpixel.shatteredpixeldungeon.effects.Lightning;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PointF;

public class DM111Sprite extends MobSprite {

    public DM111Sprite () {
        super();

        texture( Assets.Sprites.DM111 );

        TextureFilm frames = new TextureFilm( texture, 16, 14 );

        idle = new Animation( 1, true );
        idle.frames( frames, 0, 1 );

        run = new Animation( 9, true );
        run.frames( frames, 6, 7, 8, 9 );

        attack = new Animation( 11, false );
        attack.frames( frames, 2, 3, 4, 5 );

        zap = new Animation( 8, false );
        zap.frames( frames, 4, 5, 1 );

        die = new Animation( 11, false );
        die.frames( frames, 10, 11, 12, 13 );

        play( idle );
    }

    public void zap( int pos ) {

        Char enemy = Actor.findChar(pos);

        //shoot lightning from eye, not sprite center.
        PointF origin = center();
        if (flipHorizontal){
            origin.y -= 6*scale.y;
            origin.x -= 1*scale.x;
        } else {
            origin.y -= 8*scale.y;
            origin.x += 1*scale.x;
        }
        if (enemy != null) {
            parent.add(new Lightning(origin, enemy.sprite.destinationCenter(), (DM111) ch));
        } else {
            parent.add(new Lightning(origin, pos, (DM111) ch));
        }
        Sample.INSTANCE.play( Assets.Sounds.LIGHTNING );

        super.zap( ch.pos );
        flash();
    }

    @Override
    public void die() {
        emitter().burst( Speck.factory( Speck.WOOL ), 5 );
        super.die();
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }
}

