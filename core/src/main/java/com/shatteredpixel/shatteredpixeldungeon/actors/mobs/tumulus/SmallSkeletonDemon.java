package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallSkeletonDemonSprite;
import com.watabou.utils.Random;

public class SmallSkeletonDemon extends Mob {

    {
        spriteClass = SmallSkeletonDemonSprite.class;

        HP = HT = 50;
        defenseSkill = 15;

        EXP = 0;
        maxLvl = -1;
    }

    @Override
    protected boolean act() {
        if(HT == 1){
            damage(325799,this,DamageType.REAL);
        }
        reduceMaxHp(0.02f);
        return super.act();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        reduceMaxHp(0.02f);
        return super.attackProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        super.damage(dmg, src, type);
        reduceMaxHp(0.02f);
    }

    /**
     * 统一工具方法：按比例降低最大生命值HT
     * @param percent 削减比例 0.02 = 2%
     */
    private void reduceMaxHp(float percent) {
        int reduce = Math.max(1, (int) (HT * percent));
        HT -= reduce;
        if (HT < 1){
            HT = 1;
        }
        if (HP > HT) HP = HT;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 4, 18 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 15;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 2);
    }

}