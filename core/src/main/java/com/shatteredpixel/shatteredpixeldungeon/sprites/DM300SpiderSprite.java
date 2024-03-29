// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM300SpiderSprite extends MobSprite
{

    public DM300SpiderSprite()
    {
        texture("mobs/dm300spidermode.png");
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

    public int blood()
    {
        return -120;
    }

    public void onComplete(Animation animation,Animation anim)
    {
        super.onComplete(animation);
        if (anim == zap) {
            play( run );
        }
        if(animation == die)
            emitter().burst(Speck.factory(7), 15);
    }

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (parent != null) {
            parent.sendToBack(this);
            if (aura != null){
                parent.sendToBack(aura);
            }
        }
        renderShadow = false;
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
                        Sample.INSTANCE.play( Assets.Sounds.GAS );
                        ((DM200)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.MISS, 1f, 1.5f );
        GLog.w(Messages.get(OldDM300.class, "vent"));
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }

    public void attack(int var1) {
        if (!Dungeon.level.adjacent(var1, this.ch.pos)) {
            ((MissileSprite)this.parent.recycle(MissileSprite.class)).reset(this.ch.pos, var1, new Bomb(),
                    new Callback() {
                public void call() {
                    DM300SpiderSprite.this.ch.onAttackComplete();
                }
            });
            this.play(this.attack);
            this.turnTo(this.ch.pos, var1);
        } else {
            super.attack(var1);
        }

    }
}
