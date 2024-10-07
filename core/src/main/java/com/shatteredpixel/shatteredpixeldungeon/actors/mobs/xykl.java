/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Crossbow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.RedBloodMoon;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SRPDHBLRTT;
import com.watabou.utils.Random;

public class xykl extends Mob {
    private static final String COMBO = "combo";
    public RedBloodMoon weapon;
    private int combo = 0;

    public xykl() {
        this.spriteClass = SRPDHBLRTT.class;
        this.HT = 3;
        this.HP = 3;
        this.defenseSkill = 0;
        this.EXP = 0;
        this.state = this.SLEEPING;
        this.loot = new Crossbow();
        this.lootChance = 0.05f;
    }

    public int attackSkill(Char target) {
        return 16;
    }

    public int damageRoll() {
        return Random.NormalIntRange(1, 5);
    }

    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, Messages.get(this,"attack_msg_"+Random.IntRange(1, 10)));
        }
        int damage2 = xykl.super.attackProc(enemy, this.combo + damage);
        this.combo++;
        if (Dungeon.level.flamable[enemy.pos]) {
            GameScene.add(Blob.seed(enemy.pos, 9, Fire.class));
        }
        if (enemy.buff(Burning.class) == null) {
            Buff.affect(enemy, Burning.class).reignite(enemy);
        }
        if (this.combo > 5) {
            this.combo = 1;
        }
        return damage2;
    }


    public void die(Object cause) {
        xykl.super.die(cause);
        if (cause != Chasm.class) {
            this.sprite.showStatus(16711680, Messages.get(this,"death_msg_"+Random.IntRange(1, 8)));
        }
    }

}
