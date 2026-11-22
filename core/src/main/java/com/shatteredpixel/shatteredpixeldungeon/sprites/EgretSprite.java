package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class EgretSprite extends MobSprite {

    private Animation zapFail;
    private Animation zapSuccess;
    private Animation zapParalysis;
    private Animation spurnShieldNuptial;
    private Animation baneDeadEnemy;

    public EgretSprite() {
        super();

        texture( Assets.Sprites.EGRET);

        TextureFilm frames = new TextureFilm( texture, 34, 32 );

        idle = new Animation( 5, true );
        idle.frames( frames, 0,1,2,3 );

        run = idle.clone();
        attack = idle.clone();
        die = idle.clone();

        zap = new Animation( 12, true );
        zap.frames( frames, 9,10,11,12,13,14);

        zapSuccess = new Animation( 12, true );
        zapSuccess.frames( frames, 15,16,17,18,0);

        zapFail = new Animation( 12, true );
        zapFail.frames( frames, 19,20,21,22,23);

        zapParalysis = new Animation(8, true );
        zapParalysis.frames( frames, 24,25,26,27);

        spurnShieldNuptial = new Animation(12, true );
        spurnShieldNuptial.frames( frames, 28,29,30,31,32,33,34,35);

        baneDeadEnemy = new Animation(12, true );
        baneDeadEnemy.frames( frames,  4,5,6,7,8,0);

        play( idle );
    }
}
