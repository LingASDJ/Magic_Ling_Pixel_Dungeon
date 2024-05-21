package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.STRONGER_BOSSES;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.ColdChestBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DCrystalSprites;
import com.watabou.utils.Random;

public class DCrystal extends Mob {

    {
        spriteClass = DCrystalSprites.class;

        //水晶血量翻5倍
        HP = HT = Dungeon.isChallenged(STRONGER_BOSSES) ? 120 : 40;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);

        state = PASSIVE;
    }

    @Override
    protected boolean act() {

        ColdChestBossLevel.State level = ((ColdChestBossLevel) Dungeon.level).pro();
        if (level == ColdChestBossLevel.State.VSBOSS_START || level == ColdChestBossLevel.State.VSYOU_START || level
                == ColdChestBossLevel.State.VSLINK_START) {
            onZapComplete();
            spend(2f);
        }
        return super.act();
    }

    public void onZapComplete(){
        for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
            //如果宝箱王存在，且在五格范围内，给予它血量吧
            //困难模式治疗范围改为无限
            if(boss instanceof DiamondKnight && Dungeon.level.distance(pos, boss.pos) <= 5) {

                if(Dungeon.isChallenged(STRONGER_BOSSES)){
                    Buff.affect(boss, CrivusFruits.CFBarrior.class).setShield(Random.Int(5,12));
                }

                //最高加到半血
                if (boss.HP < boss.HT/2){

                    if (sprite.visible || boss.sprite.visible) {
                        sprite.parent.add(new Beam.DeathRayS(sprite.center(), boss.sprite.center()));
                    }

                    //困难模式+6血量 治疗血量翻倍
                    boss.HP = Math.min(boss.HP + 3, boss.HT/2);
                    if (boss.sprite.visible) boss.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

                    //不符合的情况下给予3回合激素涌动
                } else if (boss.buff(Adrenaline.class) == null) {

                    if (sprite.visible || boss.sprite.visible) {
                        sprite.parent.add(new Beam.HealthRay(sprite.center(), boss.sprite.center()));
                    }

                    Buff.affect(boss, Adrenaline.class, 3f);
                }
                next();
            }
        }

    }
}
