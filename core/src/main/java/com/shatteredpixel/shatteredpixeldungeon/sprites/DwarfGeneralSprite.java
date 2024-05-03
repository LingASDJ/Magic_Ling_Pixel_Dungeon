package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.DwDart;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DwarfGeneralSprite extends MobSprite {

    private MovieClip.Animation skills;

    public DwarfGeneralSprite() {
        super();

        texture(Assets.Sprites.DGS);

        TextureFilm frames = new TextureFilm(texture, 32, 32);

        idle = new MovieClip.Animation(4, true);
        idle.frames(frames, 0, 1, 2, 3);

        run = new MovieClip.Animation(14, true);
        run.frames(frames, 4,5,6,7,8,9);

        attack = new MovieClip.Animation(14, false);
        attack.frames(frames, 10,11,12,13);

        skills = new MovieClip.Animation(10, false);
        skills.frames(frames, 14,15,16,17,18);

        die = new MovieClip.Animation(14, false);
        die.frames(frames, 19,20,21,23);

        play(idle);
    }

    public void skills( int cell ) {
        ((MissileSprite)parent.recycle( MissileSprite.class )).
                reset( this, cell, new DwDart(), new Callback() {
                    @Override
                    public void call() {
                        ((DwarfGeneral)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        play( skills );
    }

    public void leap( int cell ){
        turnTo( ch.pos, cell );
        play( skills );
    }


    @Override
    public void onComplete( Animation anim ) {
        if (anim == skills) {
            idle();
        }
        super.onComplete( anim );
    }
}
