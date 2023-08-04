package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.levels.AncientMysteryCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DictFishSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class DictFish extends Mob {

    {
        spriteClass = DictFishSprites.class;

        HP = HT = 200;
        defenseSkill = 4;

        EXP = 2;
        maxLvl = -8;
        baseSpeed = 0.5f;
        properties.add(Property.BOSS); //添加BOSS属性
        immunities.add(Grim.class); //添加Grim类
        immunities.add(ScrollOfPsionicBlast.class); //添加ScrollOfPsionicBlast类
        immunities.add(ScrollOfRetribution.class); //添加ScrollOfRetribution类
        immunities.add(Corruption.class);
    }

    @Override
    protected boolean act() {

        if (Statistics.sakaBackStage >= 2) {
            ((AncientMysteryCityBossLevel) Dungeon.level).progress();
        }
        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*0.4f);
        super.damage(dmg, src);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (Random.Int(10)==4 || Statistics.sakaBackStage >= 1) {
                Buff.affect(enemy, Vulnerable.class, 6f);
            } else {
                Buff.affect( enemy, Cripple.class, 8f);
        }
        return damage;
    }

    @Override
    public void die( Object cause ) {
        Statistics.sakaBackStage++;
        GLog.n(Messages.get( RoomStone.class, "angry" ));
        if (Statistics.sakaBackStage >= 2) {
            ((AncientMysteryCityBossLevel) Dungeon.level).progress();
        }
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (boss instanceof RoomStone) {
                boss.properties.add(Property.FIERY);
            }
        }

        super.die( cause );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 10;
    }

    @Override
    public int drRoll() {
        return 8;
    }
}

