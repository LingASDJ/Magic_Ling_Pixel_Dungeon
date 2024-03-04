//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class MolotovHuntsmanSprite extends MobSprite {
    public MolotovHuntsmanSprite() {
        if(Dungeon.depth == 15 || Dungeon.hero == null || Dungeon.branch == 8){
            this.texture("sprites/boss/huntsman.png");
        } else {
            this.texture("SRPD/MolotovHuntsman.png");
        }


        TextureFilm var1 = new TextureFilm(this.texture, 16, 16);
        this.idle = new Animation(2, true);
        this.idle.frames(var1, 0, 0, 0, 1, 0, 0, 1, 1);
        this.run = new Animation(12, true);
        this.run.frames(var1, 2, 3, 4, 5, 6);
        this.attack = new Animation(12, false);
        this.attack.frames(var1, 11, 12, 13, 14);
        this.die = new Animation(12, false);
        this.die.frames(var1, 7, 8, 9, 10);
        this.play(this.idle);
    }

    public void attack(int var1) {
        if (!Dungeon.level.adjacent(var1, this.ch.pos)) {
            ((MissileSprite)this.parent.recycle(MissileSprite.class)).reset(this.ch.pos, var1, new RedBloodMoon.RedBloodMoonEX(),
                    new Callback() {
                public void call() {
                    MolotovHuntsmanSprite.this.ch.onAttackComplete();
                }
            });
            this.play(this.attack);
            this.turnTo(this.ch.pos, var1);
        } else {
            super.attack(var1);
        }

    }
}
