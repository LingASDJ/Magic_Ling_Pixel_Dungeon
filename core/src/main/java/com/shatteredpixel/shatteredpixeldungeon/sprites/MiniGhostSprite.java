package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow.minigame.GhostTemplate;
import com.watabou.noosa.TextureFilm;

public class MiniGhostSprite extends MobSprite {

    protected int texOffset(){
        return 0;
    }
    public Animation crumple;

    public Animation moverun;
    public MiniGhostSprite() {
        super();

        int c = texOffset();

        texture( Assets.Sprites.GHOST_MINI );

        TextureFilm frames = new TextureFilm( texture, 21, 22 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0+c, 1+c, 2+c, 3+c, 4+c, 5+c );

        run = new Animation( 9, true );
        run.frames( frames, 6+c, 7+c, 8+c, 9+c,10+c );

        moverun = new Animation( 9, true );
        moverun.frames( frames, 6+c, 7+c, 8+c, 9+c,10+c );

        attack = new Animation( 11, false );
        attack.frames( frames, 11+c, 12+c, 13+c, 14+c );

        die = new Animation( 9, true );
        die.frames( frames, 15+c, 16+c, 17+c, 18+c );

        crumple = die.clone();

        play( idle );
    }

    public void crumple(){
        play(crumple);
    }

    public void moveGet(){
       run = crumple.clone();
       run();
    }

    public void moveNow(){
        run = moverun.clone();
        run();
    }

    public void endCrumple(){
        if (curAnim == crumple){
            idle();
        }
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (ch instanceof GhostTemplate){
            if(((GhostTemplate) ch).active){
                crumple();
            } else {
                endCrumple();
            }
        }
    }

    public static class BlueHappyGhost extends MiniGhostSprite{
        @Override
        protected int texOffset() {
            return 19;
        }
    }

    public static class PinkSadlyGhost extends MiniGhostSprite{
        @Override
        protected int texOffset() {
            return 38;
        }
    }

    public static class OrangeGhostHaste extends MiniGhostSprite{
        @Override
        protected int texOffset() {
            return 57;
        }
    }

}
