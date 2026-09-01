package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.bossrush.Rival;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb.BlackSoul;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

//诅咒之刃
//三阶，力量需求14
//初始5-25，成长1-5
//你造成和受到的所有伤害翻倍。武器每有五级，再翻一倍。
//拿起它的时候，想必你已经知晓了代价。
public class CursedBlade extends MeleeWeapon {
    {
        image = ItemSpriteSheet.CURSED_SWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 3;
    }
    @Override
    public int max(int lvl) { return 25 + lvl * 5; }

    @Override
    public int min(int lvl) { return 5 + lvl; }
    // 伤害倍率
    public long cursedCost(){
        return 1L << Math.min(1 + buffedLvl()/5, 16);
    }

    @Override
    public String statsInfo() {
        return Messages.get(this, "stats_desc", cursedCost());
    }

    // 判断角色是否持有诅咒之剑：英雄检查主/副手，怪物检查其持武字段（Statue 及其子类、Rival、BlackSoul）
    public static CursedBlade heldBy(Char ch) {
        if (ch == null) return null;
        if (ch instanceof Hero) {
            Belongings b = ((Hero) ch).belongings;
            if (b != null) {
                if (b.weapon() instanceof CursedBlade) return (CursedBlade) b.weapon();
                if (b.secondWep() instanceof CursedBlade) return (CursedBlade) b.secondWep();
            }
        } else if (ch instanceof Statue) {
            if (((Statue) ch).weapon instanceof CursedBlade) return (CursedBlade) ((Statue) ch).weapon;
        } else if (ch instanceof Rival) {
            if (((Rival) ch).weapon instanceof CursedBlade) return (CursedBlade) ((Rival) ch).weapon;
        } else if (ch instanceof BlackSoul) {
            if (((BlackSoul) ch).weapon instanceof CursedBlade) return (CursedBlade) ((BlackSoul) ch).weapon;
        }
        return null;
    }
}