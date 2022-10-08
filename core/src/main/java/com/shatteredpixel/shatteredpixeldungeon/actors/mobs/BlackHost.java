//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoltergeistSprite;
import com.watabou.utils.Random;

public class BlackHost extends Mob {
    public BlackHost() {
        this.spriteClass = PoltergeistSprite.class;
        this.HT = 14;
        this.HP = 14;
        this.defenseSkill = 5;
        this.EXP = 8;
        this.maxLvl = 15;
        this.flying = true;
    }

    public int attackProc(Char var1, int var2) {
        var2 = super.attackProc(var1, var2 / 2);
        if (Random.Int(2) == 0) {
            if(lanterfireactive) {
                if (Random.Float() <= 0.04f && enemy instanceof Hero && hero.lanterfire < 55) {
                    ((Hero) enemy).damageLantern(2);
                    hero.sprite.showStatus(0x808080, "2");
                    Buff.affect(var1, Bleeding.class).set((float)(var2));
                    Buff.affect(var1, Poison.class).set((float)(var2));
                    die(true);
                }
            }else{
                Buff.affect(var1, Bleeding.class).set((float)(var2));
                Buff.affect(var1, Poison.class).set((float)(var2));
            }
        }

        return var2;
    }

    public int attackSkill(Char var1) {
        return 18;
    }

    public int damageRoll() {
        return Random.NormalIntRange(6, 12);
    }

    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }
}
