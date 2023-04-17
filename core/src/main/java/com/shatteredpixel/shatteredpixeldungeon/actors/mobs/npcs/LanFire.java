package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.IceHealHP;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LanFireSprites;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class LanFire extends NPC {
    {
        spriteClass = LanFireSprites.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }
    public static boolean seenBefore = false;

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }
    @Override
    public boolean interact(Char c) {
        return false;
    }

    @Override
    protected boolean act() {
        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            seenBefore = true;
            Buff.affect(hero, IceHealHP.class).set( (100), 1 );
            GLog.p("篝火的温暖照亮每一个在寒冷中徒步的冒险者，在寒冷而又凛冽的地牢中，这里是唯一的休息站。");
            spend(TICK);
            return true;
        } else if(seenBefore && !Dungeon.level.heroFOV[pos]) {
            seenBefore = false;
            Buff.detach( hero, IceHealHP.class );
            GLog.n("离开温暖的篝火休息站，前方迎面而来的又是凛冽的寒风。");
            return true;
        }
        return super.act();
    }


}
