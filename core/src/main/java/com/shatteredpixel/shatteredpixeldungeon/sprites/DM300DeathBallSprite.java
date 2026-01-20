// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class DM300DeathBallSprite extends MobSprite
{

    public DM300DeathBallSprite()
    {
        texture("mobs/dm300deathballmode.png");
        TextureFilm texturefilm = new TextureFilm(texture, 21, 21);
        idle = new Animation(10, true);
        idle.frames(texturefilm, 0, 1, 2, 3, 0, 1, 2, 3);
        run = new Animation(10, true);
        run.frames(texturefilm, 0, 1, 0, 1, 0, 1, 0, 1);
        attack = new Animation(15, false);
        attack.frames(texturefilm, 0, 1, 2, 3);
        die = new Animation(20, false);
        die.frames(texturefilm, 4, 5, 4, 5, 4, 5, 4, 5);
        play(idle);
    }

    public int blood()
    {
        return -120;
    }

    public void onComplete(Animation animation)
    {
        super.onComplete(animation);
        if(animation == die)
            emitter().burst(Speck.factory(7), 15);
    }
}
