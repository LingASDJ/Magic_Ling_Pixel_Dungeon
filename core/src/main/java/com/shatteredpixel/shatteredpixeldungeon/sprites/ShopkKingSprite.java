package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.PixelParticle;

public class ShopkKingSprite extends MobSprite {

    private PixelParticle coin;

    public ShopkKingSprite() {
        super();

        texture( Assets.Sprites.KEEPERKING );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 10, true );
        idle.frames( film, 0, 0, 1, 1, 2, 2, 3, 3, 0,0 );

        die = new Animation( 20, false );
        die.frames( film, 0 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        if (visible && anim == idle) {
            if (coin == null) {
                coin = new PixelParticle();
                parent.add( coin );
            }
            coin.reset( x + (flipHorizontal ? 0 : 13), y + 7, 0x00ffff, 1, 0.5f );
            coin.speed.y = -10;
            coin.acc.y = +110;
        }
    }

    public void link(Char sp) {
        super.link(sp);
        if(Dungeon.depth != 0) this.add(State.ROSESHIELDED);
    }

    @Override
    public void die() {
        super.die();

        remove(State.SHIELDED);
        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );

        if (visible) {
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }
    }
}

