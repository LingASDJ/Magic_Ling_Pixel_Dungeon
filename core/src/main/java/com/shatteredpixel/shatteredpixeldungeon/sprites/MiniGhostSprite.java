package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MiniGhostSprite extends MobSprite {

    protected int texOffset(){
        return 0;
    }
    private Animation crumple;
    public MiniGhostSprite() {
        super();

        int c = texOffset();

        texture( Assets.Sprites.GHOST_MINI );

        TextureFilm frames = new TextureFilm( texture, 21, 22 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0+c, 1+c, 2+c, 3+c, 4+c, 5+c );

        run = new Animation( 9, true );
        run.frames( frames, 6+c, 7+c, 8+c, 9+c,10+c );

        attack = new Animation( 11, false );
        attack.frames( frames, 11+c, 12+c, 13+c, 14+c );

        die = new Animation( 9, true );
        die.frames( frames, 15+c, 16+c, 17+c, 18+c );

        crumple = die.clone();

        play( idle );
    }

    public void crumple(){
        hideEmo();
        remove(State.PARALYSED);
        play(crumple);
    }

    @Override
    public void die() {
        if (curAnim == crumple){
            play(crumple);
        }
        super.die();
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
