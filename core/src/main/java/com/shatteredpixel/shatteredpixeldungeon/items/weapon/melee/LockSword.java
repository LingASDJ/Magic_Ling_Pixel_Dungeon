//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

public class LockSword extends MeleeWeapon {
    public int lvl = 0;

    public LockSword() {
        super.image = ItemSpriteSheet.DG3;
        super.tier = 5;
    }

    @Override
    public int min(int lvl) {

        return  Math.round(1.0f*(tier+1)) +     //15 base, down from 30
                lvl*Math.round(0.5f*(tier+1));  //+3 per level, down from +6
    }

    @Override
    public int max(int lvl) {

        return  Math.round(1.5f*(tier+1)) +     //15 base, down from 30
                lvl*Math.round(0.5f*(tier+1));  //+3 per level, down from +6
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+lvl+"_";
    }

    public int image() {
        if (lvl == 150) {
            super.image = ItemSpriteSheet.DG4;
        } else if (lvl >= 350) {
            super.image = ItemSpriteSheet.DG5;
        }
        return image;
    }

    public int proc(Char attacker, Char defender, int damage ) {
        ++lvl;
        int dmg;

        if(level >= 10){
            lvl += 0;
        } else if(defender.properties().contains(Char.Property.BOSS) && defender.HP <= damage){
            //目标Boss血量小于实际伤害判定为死亡,+20
            lvl+=20;
        } else if(defender.properties().contains(Char.Property.MINIBOSS) && defender.HP <= damage){
            //目标迷你Boss血量小于实际伤害判定为死亡,+10
            lvl+=10;
        } else if (defender.HP <= damage){
            //目标血量小于实际伤害判定为死亡,+5
            lvl+=5;
        }

         if (level>= 8) {
            dmg = (new Unstable()).proc(this, attacker, defender, damage) + 4;
            damage = dmg;
        } else if (level>= 6){
            dmg = (new Shocking()).proc(this, attacker, defender, damage) + 3;
            damage = dmg;
        } else if (level>= 4){
            dmg = (new Blazing()).proc(this, attacker, defender, damage) + 2;
            damage = dmg;
        } else if (level>= 2){
            dmg = (new Kinetic()).proc(this, attacker, defender, damage) + 1;
            damage = dmg;
        }

        return super.proc(attacker, defender, damage);


    }

    public void restoreFromBundle(Bundle var1) {
        super.restoreFromBundle(var1);
        super.image = ItemSpriteSheet.DG3;

        if (lvl == 150) {
            super.image = ItemSpriteSheet.DG4;
        } else if (lvl >= 350) {
            super.image = ItemSpriteSheet.DG5;
        }

        this.lvl = var1.getInt("lvl");
    }

    public void storeInBundle(Bundle var1) {
        super.storeInBundle(var1);
        super.image = ItemSpriteSheet.DG3;

        if (lvl == 150) {
            super.image = ItemSpriteSheet.DG4;
        } else if (lvl >= 350) {
            super.image = ItemSpriteSheet.DG5;
        }

        var1.put("lvl", this.lvl);
    }
}
