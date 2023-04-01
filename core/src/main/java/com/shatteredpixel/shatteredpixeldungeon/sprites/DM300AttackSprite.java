// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DMZERO;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM300AttackSprite extends MobSprite
{

    private int cellToAttack;

    public DM300AttackSprite()
    {
        texture("mobs/dm300attackmode.png");
        TextureFilm texturefilm = new TextureFilm(texture, 22, 20);
        idle = new Animation(10, true);
        idle.frames(texturefilm,0,1);
        run = new Animation(10, true);
        run.frames(texturefilm, 2,3);
        attack = new Animation(15, false);
        attack.frames(texturefilm,4,5,6);
        die = new Animation(20, false);
        die.frames(texturefilm, 0,7,0,7,0,7,0,7,0,7,0,7,8);
        play(idle);
    }

    @Override
    public int blood() {
        return 0xFF44FF22;
    }

    @Override
    public void attack( int cell ) {
        if (!Dungeon.level.adjacent( cell, ch.pos )) {

            cellToAttack = cell;
            turnTo( ch.pos , cell );
            play( zap );

        } else {

            super.attack( cell );

        }
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();

            ((MissileSprite)parent.recycle( MissileSprite.class )).
                    reset( this, cellToAttack, new DM300AttackSprite.ScorpioShot(), new Callback() {
                        @Override
                        public void call() {
                            ch.onAttackComplete();
                        }
                    } );
        } else {
            super.onComplete( anim );
        }
    }

    public class ScorpioShot extends Item {
        {
            image = ItemSpriteSheet.FISHING_SPEAR;
        }
    }

    public void zap( int cell ) {

        turnTo( ch.pos , cell );
        play( zap );

        MagicMissile.boltFromChar( parent,
                MagicMissile.CORROSION,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((DMZERO.DM300AttackMode)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }
}
