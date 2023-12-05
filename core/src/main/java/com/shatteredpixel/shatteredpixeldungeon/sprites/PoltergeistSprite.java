//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class PoltergeistSprite extends MobSprite {
    public PoltergeistSprite() {
        this.texture("SRPD/BlackGhost.png");
        TextureFilm var1 = new TextureFilm(this.texture, 14, 15);
        this.idle = new Animation(5, true);
        this.idle.frames(var1, 0, 1);
        this.run = new Animation(10, true);
        this.run.frames(var1, 0, 1);
        this.attack = new Animation(10, false);
        this.attack.frames(var1, 0, 2, 3, 4, 5);
        this.die = new Animation(8, false);
        this.die.frames(var1, 0, 6, 7, 8, 9);
        zap = attack.clone();
        this.play(this.idle);
    }

    public int blood() {
        return -2013265920;
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                3,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Elemental)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public static class WraithSpriteRed extends PoltergeistSprite  {

        public WraithSpriteRed(){
            super();
            tint(1, 1, 1, 0.3f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(1, 1, 1, 0.3f);
        }
    }
}
