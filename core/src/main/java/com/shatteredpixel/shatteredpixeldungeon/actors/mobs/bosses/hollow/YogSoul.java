package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YogSoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Random;

public class YogSoul extends Boss {

    {
        initProperty();
        initBaseStatus(0, 0, 40, 0, 700, 5, 12);
        initStatus(20);
        spriteClass = YogSoulSprite.class;

        properties.add(Property.BOSS);
        properties.add(Property.ACIDIC);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);

        state = WANDERING = new Waiting();

        noDropIceCoin = true;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 15, 50 );
    }
    @Override
    public int attackSkill( Char target ) {
        return 40;
    }


    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    private class Waiting extends Mob.Wandering{

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc(enemy, 0);
        enemy.damage( damageRoll(), this );
        return damage;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 1;
            lock.addTime(dmg*multiple);
        }
        super.damage(dmg, src, type);
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if(mob instanceof Morphs){
                ((Morphs) mob).phase+=0.40f;
            }
        }
    }

}
