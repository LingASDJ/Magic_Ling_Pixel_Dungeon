package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;

public class HelpTeleportPointSprites extends MobSprite {

    private Animation activeLX;
    public HelpTeleportPointSprites() {
        super();

        texture( Assets.Sprites.TELE_FOCU);

        perspectiveRaise = 8 / 16f;
        shadowOffset = 1.25f;
        shadowHeight = 0.4f;
        shadowWidth = 1f;

        TextureFilm frames = new TextureFilm( texture, 16, 25 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0 );

        run = idle.clone();

        attack = idle.clone();

        die = idle.clone();

        activeLX = new Animation( 8, true );
        activeLX.frames( frames, 1 );

        play( idle );
    }

    public void Activate(){
        idle = activeLX.clone();
        idle();
    }

    public void UnActivate(){
        idle = idle.clone();
        idle();
    }

    private float baseY = Float.NaN;

    @Override
    public void place(int cell) {
        super.place(cell);
        baseY = y;
    }

    @Override
    public void update() {
        super.update();

        if (!paused){
            if (Float.isNaN(baseY)) baseY = y;
            y = baseY + (float)(Math.sin(Game.timeTotal));
            shadowOffset = 1.25f - 0.9f*(float)(Math.sin(Game.timeTotal));
        }
    }
}

