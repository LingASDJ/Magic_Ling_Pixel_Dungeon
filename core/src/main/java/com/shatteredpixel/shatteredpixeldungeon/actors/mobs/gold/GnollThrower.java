package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Gnoll;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class GnollThrower extends Gnoll {
    @Override
    public String info(){
        String desc = super.description();

        desc += "\n\n" + Messages.get(GoldMob.class,"infos");

        return desc;
    }

    @Override
    public void die( Object cause ) {
        super.die( cause );
        if(Statistics.RandomQuest == 2){
            if(Statistics.GoldMobDead>=15){
                Statistics.goldRefogreCount++;
                Statistics.GoldMobDead = 0;
            }

        }
    }

    {
        spriteClass = GnollThrowerSprite.class;
        viewDistance = 4;

        HP = HT = 30;
        defenseSkill = 10;

        EXP = 5;
        maxLvl = 12;

        loot = Random.oneOf(Generator.Category.FOOD,Generator.Category.STONE);
        lootChance =0.4f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 14 );
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return (new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos||super.canAttack(enemy));
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(1, 5);
    }

}
