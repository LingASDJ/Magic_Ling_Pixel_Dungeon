package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GunHuntsmanSprite extends MobSprite {

    private Animation cast;

    public GunHuntsmanSprite() {
        super();

        texture( "SRPD/Gunman.png" );
        TextureFilm var1 = new TextureFilm(this.texture, 20, 16);
        Integer var2 = 2;
        Integer var3 = 1;
        this.idle = new Animation(2, true);
        Animation var4 = this.idle;
        Integer var5 = 0;
        var4.frames(var1, var5, var5, var5, var3, var5, var5, var3, var3);
        this.run = new Animation(12, true);
        this.run.frames(var1, var2, 3, 4, 5, 6, var2);
        this.attack = new Animation(12, false);
        this.attack.frames(var1, 10, 11, 12,11);
        this.die = new Animation(12, false);
        this.die.frames(var1, 7, 8, 9);
        this.play(this.idle);
        cast = attack.clone();
        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cell, new Firebomb(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );

            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }
}
