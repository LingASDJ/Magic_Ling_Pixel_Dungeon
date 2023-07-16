package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;

public class ShadowSprite extends MobSprite {

    private Emitter smoke;

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.RAINBOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((BlackSoul)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    public ShadowSprite() {
        super();

        texture(Dungeon.hero.heroClass.spritesheet());

        TextureFilm film = new TextureFilm(HeroSprite.tiers(), 6, 12, 15);

        idle = new Animation(1, true);
        idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(20, true);
        run.frames(film, 2, 3, 4, 5, 6, 7);

        die = new Animation(20, false);
        die.frames(film, 0);

        attack = new Animation(15, false);
        attack.frames(film, 13, 14, 15, 0);

        zap = attack.clone();

        idle();
        resetColor();
    }

    @Override
    public void onComplete(Tweener tweener) {
        super.onComplete(tweener);
    }

    @Override
    public void resetColor() {
        super.resetColor();
        alpha(0.8f);
        brightness(0.0f);
    }

    @Override
    public void update() {

        super.update();

        if (smoke != null) {
            smoke.visible = visible;
        }
    }

    @Override
    public void kill() {
        super.kill();

        if (smoke != null) {
            smoke.on = false;
        }
    }
}
