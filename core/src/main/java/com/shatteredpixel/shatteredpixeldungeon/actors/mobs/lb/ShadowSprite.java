package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;

public class ShadowSprite extends MobSprite {

    public ShadowSprite() {
        super();

        texture(Dungeon.hero.heroClass.spritesheet());

        TextureFilm film = new TextureFilm(HeroSprite.tiers(), 7, 12, 15);

        idle = new Animation(1, true);
        idle.frames(film, 0, 0, 0, 1, 0, 0, 1, 1);

        run = new Animation(20, true);
        run.frames(film, 2, 3, 4, 5, 6, 7);

        die = new Animation(20, false);
        die.frames(film, 0);

        attack = new Animation(15, false);
        attack.frames(film, 13, 14, 15, 0);

        zap = attack.clone();
        toss = attack.clone();

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
        brightness(0.7f);
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        final Ballistica shot = new Ballistica( ch.pos, cell, ((BlackSoul)ch).wand.collisionProperties(cell));

        ((BlackSoul)ch).wand.fx(shot, ch, new Callback() {
            public void call() {
                ((BlackSoul)ch).onZapComplete();
            }
        });
    }

    public void toss( int cell ) {

        turnTo( ch.pos , cell );
        play( toss );

        ((MissileSprite)parent.recycle( MissileSprite.class )).
                reset(ch.pos, cell, ((BlackSoul)ch).missile, new Callback() {
                    public void call() {
                        ((BlackSoul)ch).onTossComplete();
                    }
                });
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap || anim == toss) {
            idle();
        }
        super.onComplete( anim );
    }
}
