/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostSoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefSoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ThiefSoul extends Mob {

    public boolean hasStolenSoul = false;

    {
        spriteClass = ThiefSoulSprite.class;

        HP = HT = 45;
        defenseSkill = 17;

        EXP = 9;
        maxLvl = 17;

        loot = Gold.class;
        lootChance = 0.5f;

        WANDERING = new Wandering();
        FLEEING = new Fleeing();

        properties.add(Property.TUMULUS);
    }

    public int mobUID;

    private static final String STOLEN_SOUL = "hasStolenSoul";
    private static final String MOB_UID = "mob_uid";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(STOLEN_SOUL, hasStolenSoul);
        bundle.put(MOB_UID, mobUID);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        hasStolenSoul = bundle.getBoolean(STOLEN_SOUL);
        mobUID = bundle.getInt(MOB_UID);
    }

    @Override
    protected boolean act() {
        if (hasStolenSoul && defenseSkill == 17){
            defenseSkill *= 0.8f;
        }
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 18);
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * 0.5f;
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, damage);

        if (alignment == Alignment.ENEMY && !hasStolenSoul && enemy instanceof Hero) {
            if (stealSoul((Hero) enemy)) {
                state = FLEEING;
            }
        }

        return damage;
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (state == FLEEING) {
            Dungeon.level.drop(new Gold(), pos).sprite.drop();
        }
        return super.defenseProc(enemy, damage);
    }

    protected boolean stealSoul(Hero hero) {
        LostSoul buff = Buff.append(hero, LostSoul.class);
        Buff.affect(hero, LostSoul.LostCount.class);
        buff.thiefSoulID = this.mobUID;
        hasStolenSoul = true;
        GLog.w(Messages.get(this, "soul_stolen"));
        return true;
    }

    public static class NoHero{};

    @Override
    public void die(Object cause) {
        if (hasStolenSoul && Dungeon.hero != null && cause != NoHero.class) {
            for (LostSoul buff : Dungeon.hero.buffs(LostSoul.class)) {
                if (buff.thiefSoulID == this.mobUID) {
                    buff.detach();
                    GLog.p(Messages.get(this, "soul_recovered"));
                    break;
                }
            }
        }
        super.die(cause);
    }

    @Override
    public String description() {
        String desc = super.description();
        if (hasStolenSoul) {
            desc += "\n\n" + Messages.get(this, "carries_soul");
        }
        return desc;
    }

    public class Wandering extends Mob.Wandering {
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            super.act(enemyInFOV, justAlerted);
            if (state == HUNTING && hasStolenSoul) {
                state = FLEEING;
            }
            return true;
        }
    }

    public class Fleeing extends Mob.Fleeing {
        @Override
        protected void escaped() {
            if (hasStolenSoul
                    && !Dungeon.level.heroFOV[pos]
                    && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {

                int count = 32;
                int newPos;
                do {
                    newPos = Dungeon.level.randomRespawnCell(ThiefSoul.this);
                    if (count-- <= 0) break;
                } while (newPos == -1 || Dungeon.level.heroFOV[newPos] || Dungeon.level.distance(newPos, pos) < (count / 3));

                if (newPos != -1) {
                    pos = newPos;
                    sprite.place(pos);
                    sprite.visible = Dungeon.level.heroFOV[pos];
                    if (Dungeon.level.heroFOV[pos]) {
                        CellEmitter.get(pos).burst(Speck.factory(Speck.WOOL), 6);
                    }
                }
                die(NoHero.class);
                GLog.n(Messages.get(ThiefSoul.class, "escapes_with_soul"));
                hasStolenSoul = false;
            }
            state = WANDERING;
        }
    }
}