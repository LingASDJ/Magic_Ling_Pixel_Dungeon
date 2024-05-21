package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.DiedClearElemet;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DiedClearElementalSprites extends MobSprite {
    protected int texOffset(){
        return 0;
    }
    public DiedClearElementalSprites() {
        super();

        int c = texOffset();

        texture( Assets.Sprites.DER );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new MovieClip.Animation( 10, true );
        idle.frames( frames, 0+c, 1+c, 2+c );

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 0+c, 1+c, 2+c );

        attack = new MovieClip.Animation( 15, false );
        attack.frames( frames, 1+c, 1+c, 2+c, 2+c );

        die = new MovieClip.Animation( 5, false );
        die.frames( frames, 3+c, 4+c, 5+c, 6+c, 7+c,8+c,9+c );

        play( idle );
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {

            MagicMissile.boltFromChar( parent,
                    MagicMissile.RAINBOW,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((DiedClearElemet)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            play(idle);
            turnTo( ch.pos , cell );

        } else {

            super.attack( cell );

        }
    }

    public static class Red extends DiedClearElementalSprites{
        @Override
        protected int texOffset() {
            return 0;
        }

        @Override
        public void attack( int cell ) {
            if (!Dungeon.level.adjacent(cell, ch.pos)) {

                MagicMissile.boltFromChar( parent,
                        MagicMissile.SHAMAN_RED,
                        this,
                        cell,
                        new Callback() {
                            @Override
                            public void call() {
                                ((DiedClearElemet)ch).onZapComplete();
                            }
                        } );
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                play(idle);
                turnTo( ch.pos , cell );

            } else {

                super.attack( cell );

            }
        }
    }

    public static class Dark extends DiedClearElementalSprites{
        @Override
        protected int texOffset() {
            return 10;
        }
        @Override
        public void attack( int cell ) {
            if (!Dungeon.level.adjacent(cell, ch.pos)) {

                MagicMissile.boltFromChar( parent,
                        MagicMissile.SHAMAN_BLUE,
                        this,
                        cell,
                        new Callback() {
                            @Override
                            public void call() {
                                ((DiedClearElemet)ch).onZapComplete();
                            }
                        } );
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                play(idle);
                turnTo( ch.pos , cell );

            } else {

                super.attack( cell );

            }
        }
    }

    public static class Green extends DiedClearElementalSprites{
        @Override
        protected int texOffset() {
            return 20;
        }
        @Override
        public void attack( int cell ) {
            if (!Dungeon.level.adjacent(cell, ch.pos)) {

                MagicMissile.boltFromChar( parent,
                        MagicMissile.TOXIC_VENT,
                        this,
                        cell,
                        new Callback() {
                            @Override
                            public void call() {
                                ((DiedClearElemet)ch).onZapComplete();
                            }
                        } );
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                play(idle);
                turnTo( ch.pos , cell );

            } else {

                super.attack( cell );

            }
        }
    }

    public static class Pure extends DiedClearElementalSprites{
        @Override
        protected int texOffset() {
            return 30;
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
                                ((DiedClearElemet)ch).onZapComplete();
                            }
                        } );
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                play(idle);
                turnTo( ch.pos , cell );

            } else {

                super.attack( cell );

            }
        }
    }

    public static class Gold extends DiedClearElementalSprites{
        @Override
        protected int texOffset() {
            return 40;
        }
        @Override
        public void attack( int cell ) {
            if (!Dungeon.level.adjacent(cell, ch.pos)) {

                MagicMissile.boltFromChar( parent,
                        MagicMissile.WARD,
                        this,
                        cell,
                        new Callback() {
                            @Override
                            public void call() {
                                ((DiedClearElemet)ch).onZapComplete();
                            }
                        } );
                Sample.INSTANCE.play( Assets.Sounds.ZAP );
                play(idle);
                turnTo( ch.pos , cell );

            } else {

                super.attack( cell );

            }
        }
    }


}
