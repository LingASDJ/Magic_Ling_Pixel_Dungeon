package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DrakeSprite extends MobSprite {
    private Animation hiding;
    protected boolean submerged = false;
    public DrakeSprite(){
        super();

        texture(Assets.Sprites.DRAKE);

        TextureFilm frames = new TextureFilm( texture, 24,10);
        idle = new MovieClip.Animation( 5, true );
        idle.frames( frames, 0,1,2,3);

        run = new MovieClip.Animation( 9, true );
        run.frames( frames,  4, 5, 6 ,7,8,9);

        attack = new MovieClip.Animation( 11, false );
        attack.frames( frames, 10,11,12,13);

        die = new MovieClip.Animation( 9, false );
        die.frames( frames, 14,15,16,17,18,19,20,21);

        hiding = new Animation(5,false);
        hiding.frames(frames,22);

        play(idle);
    }
    public void hideDrake(){
        play(hiding);
        hideSleep();
    }

    @Override
    public void showSleep() {
        if (curAnim == hiding){
            return;
        }
        super.showSleep();
    }
}
