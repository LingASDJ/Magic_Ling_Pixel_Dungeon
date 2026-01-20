package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PylonCS;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class FireCrystalSprites extends MobSprite {

    private Animation activeIdle;

    public  FireCrystalSprites() {
        super();

        perspectiveRaise = 5/16f; //1 pixel less
        renderShadow = false;

        texture( Assets.Sprites.FREA );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 1, false );
        idle.frames( frames, 0 );

        activeIdle = new Animation( 1, false );
        activeIdle.frames( frames, 1 );

        run = idle.clone();

        attack = idle.clone();

        die = new Animation( 1, false );
        die.frames( frames, 2 );

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch instanceof PylonCS && ch.alignment == Char.Alignment.ENEMY){
            activate();
        }
        renderShadow = false;
    }

    @Override
    public void place(int cell) {
        if (parent != null) parent.bringToFront(this);
        super.place(cell);
    }

    public void activate(){
        idle = activeIdle.clone();
        idle();
    }

    @Override
    public void play(Animation anim) {
        if (anim == die){
            turnTo(ch.pos, ch.pos+1); //always face right to merge with custom tiles
            emitter().burst(BlastParticle.FACTORY, 20);
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
        }
        super.play(anim);
    }

    @Override
    public void onComplete(Animation anim) {
        if (anim == attack){
            flash();
        }
        super.onComplete(anim);
    }

    @Override
    public int blood() {
        return 0xFFFFFF88;
    }
}

