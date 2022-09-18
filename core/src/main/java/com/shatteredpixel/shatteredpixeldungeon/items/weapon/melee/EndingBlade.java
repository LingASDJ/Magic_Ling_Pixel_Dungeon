package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.LaserPython;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class EndingBlade extends Weapon {
    {
        image = ItemSpriteSheet.ENDDIED;
        tier = 4;
        cursed = true;
        enchant(Enchantment.randomCurse());
    }

    //三大基础静态固定字节
    public static final String AC_LASERCRYSTAL	= "lastcrystal";
    public static final String AC_DIEDGHOST	    = "diedghost";
    public static final String AC_HEALRESET	    = "healreset";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(isEquipped( hero )) {
            //激光晶柱
            actions.add(AC_LASERCRYSTAL);
            //亡灵宣告
            actions.add(AC_DIEDGHOST);
            //再生之力
            actions.add(AC_HEALRESET);

        }
        return actions;
    }

    public int fireenergy;

    private boolean firstx=true;

    private static final String FIRST = "first";
    private static final String INTRPS = "intrps";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, firstx);
        bundle.put(INTRPS, fireenergy);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        firstx = bundle.getBoolean(FIRST);
        fireenergy = bundle.getInt(INTRPS);
    }

    public String desc() {
        return Messages.get(this, "desc")+"_"+fireenergy+"_";
    }

    //每100点浊焰能量自动升级
    @Override
    public int level() {
        return fireenergy/100;
    }

    public void execute( Hero hero, String action ) {
        super.execute( hero, action );

            switch (action) {
                case AC_LASERCRYSTAL:
                    if(level >= 4 && firstx){
                        firstx = false;
                        new LaserPython().quantity(1).identify().collect();
                        GLog.n("你突然感觉你的背包鼓鼓的……");
                    } else if(!firstx) {
                        GLog.n("你尚未使用激光晶柱，无法继续使用");
                    } else {
                        GLog.n("等级不足");
                    }
                    break;
                case AC_DIEDGHOST:
                    GLog.p("2");
                    break;
                case AC_HEALRESET:
                    GLog.w("3");
                    break;
            }
    }




    @Override
    public int STRReq(int C) {
        return 15+fireenergy/100;
    }

    public int proc(Char attacker, Char defender, int damage ) {
        //常规加+1浊焰能量
        ++fireenergy;
        int dmg;
        if(level >= 10){
            fireenergy += 0;
            //武器最高级
        } else if(defender.properties().contains(Char.Property.BOSS) && defender.HP <= damage){
            //目标Boss血量小于实际伤害判定为死亡,+20浊焰能量
            fireenergy+=20;
        } else if(defender.properties().contains(Char.Property.MINIBOSS) && defender.HP <= damage){
            //目标迷你Boss血量小于实际伤害判定为死亡,+10浊焰能量
            fireenergy+=10;
        } else if (defender.HP <= damage){
            //目标血量小于实际伤害判定为死亡,+5浊焰能量
            fireenergy+=5;
        }

        /*
         * 10级 死神附魔
         * 8级  紊乱附魔
         * 6级  电击附魔
         * 4级  烈焰附魔
         * 2级  恒动附魔
         */
        if(level>= 10){
            dmg = (new Grim()).proc(this, attacker, defender, damage) + 5;
            damage = dmg;
        } else if (level>= 8) {
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

    @Override
    public boolean isUpgradable() {
        return false;
    }

    public int tier;

    @Override
    public String info() {

        String info = desc();

        if (levelKnown) {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_known", tier, augment.damageFactor(min()), augment.damageFactor(max()), STRReq());
            if (STRReq() > Dungeon.hero.STR()) {
                info += " " + Messages.get(Weapon.class, "too_heavy");
            } else if (Dungeon.hero.STR() > STRReq()){
                info += " " + Messages.get(Weapon.class, "excess_str", Dungeon.hero.STR() - STRReq());
            }
        } else {
            info += "\n\n" + Messages.get(MeleeWeapon.class, "stats_unknown", tier, min(0), max(0), STRReq(0));
            if (STRReq(0) > Dungeon.hero.STR()) {
                info += " " + Messages.get(MeleeWeapon.class, "probably_too_heavy");
            }
        }

        switch (augment) {
            case SPEED:
                info += " " + Messages.get(Weapon.class, "faster");
                break;
            case DAMAGE:
                info += " " + Messages.get(Weapon.class, "stronger");
                break;
            case NONE:
        }

        if (enchantment != null && (cursedKnown || !enchantment.curse())){
            info += "\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name());
            info += " " + Messages.get(enchantment, "desc");
        }

        if (cursed && isEquipped( Dungeon.hero )) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed_worn");
        } else if (cursedKnown && cursed) {
            info += "\n\n" + Messages.get(Weapon.class, "cursed");
        } else if (!isIdentified() && cursedKnown){
            info += "\n\n" + Messages.get(Weapon.class, "not_cursed");
        }

        return info;
    }

    @Override
    public int min(int lvl) {
        return 10+(lvl*2)*(Dungeon.hero.lvl/10);
        //10+20x3=70
    }

    @Override
    public int max(int lvl) {
        return 15+(lvl*3)*(Dungeon.hero.lvl/10);
        //15+30x3=15+90=105
    }

    //Max 70~105
}
