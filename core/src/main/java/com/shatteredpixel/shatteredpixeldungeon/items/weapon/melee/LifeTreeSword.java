/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2019 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TragicCode;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LifeTreeSword extends MeleeWeapon {

    {
        image = ItemSpriteSheet.LifeTreeSword;
        tier = 3;
        defaultAction = AC_SUMMON;
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof LifeTreant) {
                mob.die(null);
            }
        }
        return super.doUnequip(hero, collect, single);
    }

    @Override
    public String status() {
        return charge+"/"+MAX_CHARGE;
    }

    private int charge = 0;
    private final int MAX_CHARGE = 40;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_SUMMON)) {
            curUser = hero;
            curItem = this;
            if (charge >= MAX_CHARGE) {
                GameScene.selectCell(summoner);
            } else {
                GLog.w(Messages.get(this, "no_charge"));
            }
        }
    }

    @Override
    public int min(int lvl) {
        return 8 + lvl;
    }

    @Override
    public int max(int lvl) {
        return 18 + lvl * 3;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {

        int heal = Math.min(damage / 3, 3);

        if (heal > 0 && attacker.HP < attacker.HT) {
            charge+=heal;
        }

        if (charge > MAX_CHARGE) charge = MAX_CHARGE;
        updateQuickslot();

        return super.proc(attacker, defender, damage);
    }

    private void summon(int pos) {
        if (Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos] || Actor.findChar(pos) != null) {
            GLog.n(Messages.get(this, "bad_pos"));
            return;
        }

        LifeTreant ally = new LifeTreant();
        ally.setLevel(this,buffedLvl());
        ally.pos = pos;
        GameScene.add(ally);
        ally.HP = ally.HT;
        updateQuickslot();
        CellEmitter.get(pos).burst(Speck.factory(Speck.EVOKE), 6);
        charge = 0;
    }

    private CellSelector.Listener summoner = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) summon(target);
        }

        @Override
        public String prompt() {
            return Messages.get(LifeTreeSword.class, "prompt");
        }
    };

    public static final String AC_SUMMON = "SUMMON";

    @Override
    public String desc() {
        return Messages.get(this, "desc", charge, MAX_CHARGE);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("charge", charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt("charge");
    }

    public static class LifeTreant extends Mob {

        public MeleeWeapon weapon;

        {
            alignment = Alignment.ALLY;
            state = WANDERING;
            spriteClass = LifeTreantSprite.class;

            immunities.add(ToxicGas.class);

            properties.add(Property.ACIDIC);
            weapon = (MeleeWeapon) Dungeon.hero.belongings.weapon();
        }

        public void setLevel(Weapon weapon,int lvl) {
            weapon.level = lvl;
            HT = 15 + lvl * 4;
            defenseSkill = 6 + lvl;
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {

            GameScene.add(Blob.seed(pos, 20, ToxicGas.class));

            return super.defenseProc(enemy, damage);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(4 + weapon.level, 9 + weapon.level);
        }

        @Override
        public int attackSkill(Char target) {
            return 10 + weapon.level * 3;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(1, 3 + weapon.level/2);
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
            dmg = Math.min(dmg, 1);
            super.damage(dmg, src, type);
        }

        @Override
        public void die(Object cause) {
            super.die(cause);
            CellEmitter.get(pos).burst(Speck.factory(Speck.EVOKE), 3);
        }
    }

    public static class LifeTreantSprite extends com.shatteredpixel.shatteredpixeldungeon.sprites.KatydidSprites {
        public LifeTreantSprite(){
            super();
            tint(0.2f, 0.8f, 0.2f, 0.5f);
        }

        @Override
        public void resetColor() {
            super.resetColor();
            tint(0.2f, 0.8f, 0.2f, 0.5f);
        }
    }

    public static class PlaceHolder extends LifeTreeSword {
        {
            image = ItemSpriteSheet.LifeTreeSword;
        }

        @Override
        public boolean isSimilar(Item item) {
            return item instanceof LifeTreeSword && !item.isEquipped(Dungeon.hero);
        }

        @Override
        public String info() {
            return Messages.get(this, "error");
        }
    }


    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(TragicCode.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        int dmgBoost = augment.damageFactor(2 + buffedLvl());
        TragicCode.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 2 + buffedLvl() : 2;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 2 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}