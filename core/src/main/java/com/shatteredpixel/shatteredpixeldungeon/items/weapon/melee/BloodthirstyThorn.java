package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class BloodthirstyThorn extends MeleeWeapon {

    {
        image = ItemSpriteSheet.BloodDir;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 0.9f;
        ACC = 1.75f;
        RCH=1;
        tier=6;
    }

    @Override
    public int min(int lvl) {
        return 4 +lvl*2;
    }

    @Override
    public int max(int lvl) {
        return  14 + lvl*7;
    }

    @Override
    public int STRReq(int lvl){
        return 8;
    }

    private void getHerodamageHp(Hero hero) {
        int damage = 2;
        hero.damage(damage,this);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        // 等级+3前每次攻击有概率扣除使用者2点生命值，并施加流血效果。
        // 等级+5及以后时攻击距离增加一格，并移除其负面影响
        if (level() < 4 && Random.Float() > 0.35f) {
            getHerodamageHp(hero);
            Buff.affect(attacker, Bleeding.class).set(7);
        } else if (level() >= 10) {
            //吸血为每次伤害/5 例如=50/5=10 Math.min()不超出。
            attacker.HP += Math.min( attacker.HT, damage/5);
//            hero.sprite.showStatus(CharSprite.POSITIVE, ("+" +Math.min( attacker.HT, damage/5) + "HP"));
        } else {
            getHerodamageHp(hero);
            Buff.affect(attacker, Bleeding.class).set(7);
        }

        //恐惧和流血
        if(level()>=0 && Random.Float()>0.45f){
            Buff.affect(defender, Bleeding.class).set(7);
            Buff.affect(defender, Terror.class, Terror.DURATION ).object = curUser.id();
        }
        return super.proc(attacker, defender, damage);

    }

    //动态改变图标1
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            super.image = ItemSpriteSheet.BloodDied;
            //在载入存档更新图标的同时更新攻击范围
            RCH=2;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
    }

    //动态改变图标2
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            //在保存存档更新图标的同时更新攻击范围
            super.image = ItemSpriteSheet.BloodDied;
            RCH=2;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
    }

    //动态改变图标3
    @Override
    public int image() {
        super.image = ItemSpriteSheet.BloodDir;
        if (level() >= 5) {
            super.image = ItemSpriteSheet.BloodDied;
            //在更新图标的同时更新攻击范围
            RCH=2;
        } else {
            super.image = ItemSpriteSheet.BloodDir;
        }
        return image;
    }

    @Override
    public String statsInfo(){
        if (isIdentified() && level() >= 10 ) {
            return Messages.get(this, "stats_desc")+Messages.get(this, "stats_descx");
        } else if (isIdentified() && level() >= 5) {
            return Messages.get(this, "stats_desc");
        } else {
            return "";
        }
    }

}
