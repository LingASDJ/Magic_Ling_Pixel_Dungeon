package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DiamondKnight;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class DimandKingSprite extends MobSprite {
    private Emitter teleParticles;
    private Animation cast;

    public DimandKingSprite() {
        super();

        texture(Assets.Sprites.DIMK);

        TextureFilm frames = new TextureFilm( texture, 24, 24 );

        idle = new Animation( 7, true );
        idle.frames( frames, 0, 0, 0, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4);

        attack = new Animation( 15, false );
        attack.frames( frames, 5,6,7,8);

        cast = attack.clone();

        die = new Animation( 3, false );
        die.frames( frames, 9,10,11 );

        play( idle );
	}

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.SWORDLING,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((DiamondKnight)ch).onZapComplete();
                        }
                    } );

            play( cast );
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        teleParticles = emitter();
        teleParticles.autoKill = false;
        teleParticles.pour(ElmoParticle.FACTORY, 0.05f);
        teleParticles.on = false;
    }

    @Override
    public void update() {
        super.update();

        if (teleParticles != null){
            teleParticles.pos( this );
            teleParticles.visible = visible;
        }
    }

    @Override
    public void kill() {
        super.kill();

        if (teleParticles != null) {
            teleParticles.on = false;
        }
    }

    @Override
    public synchronized void play(Animation anim, boolean force) {
        if (teleParticles != null) teleParticles.on = false;
        super.play(anim, force);
    }

    @Override
    public int blood() {
        return 0xFF80706c;
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.ELMO,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((DiamondKnight)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    private boolean died = false;

    @Override
    public void onComplete( Animation anim ) {
        if (anim == die && !died) {
            died = true;
            emitter().burst( ElmoParticle.FACTORY, 4 );
        }
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    public static class PrismaticSprite extends MobSprite {

        private static final int FRAME_WIDTH	= 12;
        private static final int FRAME_HEIGHT	= 15;

        public PrismaticSprite() {
            super();

            texture( Dungeon.hero.heroClass.spritesheet() );
            updateArmor( 7 );
            idle();
        }

        @Override
        public void link( Char ch ) {
            super.link( ch );
            updateArmor( ((DiamondKnight)ch).armTier );
        }

        public void updateArmor( int tier ) {
            TextureFilm film = new TextureFilm( HeroSprite.tiers(), 7, FRAME_WIDTH, FRAME_HEIGHT );

            idle = new Animation( 1, true );
            idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

            run = new Animation( 20, true );
            run.frames( film, 2, 3, 4, 5, 6, 7 );

            die = new Animation( 20, false );
            die.frames( film, 0 );

            attack = new Animation( 15, false );
            attack.frames( film, 13, 14, 15, 0 );

            idle();
        }

        @Override
        public void update() {
            super.update();
            if (flashTime <= 0) {
                float interval = (Game.timeTotal % 9) / 3f;
                tint(0,
                        interval > 1 ? Math.max(0.9f, 1 - interval) : interval,
                        interval < 1 ? Math.min(153 + interval * 72, 255) : Math.max(153 - (interval - 1) * 72, 128),
                        0.5f);
            }
        }

    }
    
}
