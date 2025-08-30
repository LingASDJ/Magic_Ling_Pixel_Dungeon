package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MobSpawner;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MyCoreHeartSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

public class MyCoreHeart extends Boss {

    private int summonedmobsCount = 1;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 42, 0, 0);
        initStatus(0);

        spriteClass = MyCoreHeartSprite.class;

        viewDistance = 100;

        alignment = Alignment.ALLY;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(Blob.class);
        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Burning.class);
        immunities.add(Ooze.class);
        immunities.add(Terror.class);
        immunities.add(Hex.class);
        immunities.add(Vertigo.class);
        immunities.add(Blindness.class);
        immunities.add(Poison.class);
        immunities.add(TowerMachine.DeadAlive.class);
        immunities.add(Healing.class);

        state = WANDERING = new Waiting();
    }

    private Mob getSummonTimeMobs() {
        Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(summonedmobsCount).get(0));
        return mob;
    }

    public static class SummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }


    public boolean act() {
        alerted = false;
        state = PASSIVE;
        TryGetSummonedMobs();
        return super.act();
    }

    public void TryGetSummonedMobs() {
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if(!mob.isOldDay){
                if (buff(SummonColdDown.class) == null) {
                    for (int i = 0; i < 5; i++) {
                        Mob testActor = getSummonTimeMobs();
                        testActor.pos =Dungeon.level.randomDestination(MyCoreHeart.this);
                        testActor.isOldDay = true;
                        testActor.state = testActor.HUNTING;
                        GameScene.add(testActor);
                    }
                    Buff.affect(this, SummonColdDown.class, 30f);
                    summonedmobsCount++;
                }
            }
        }
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


    private static final String STRING = "STRING";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STRING, summonedmobsCount );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        BossHealthBar.assignBoss(this);
        summonedmobsCount = bundle.getInt( STRING );
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {

        if(src == hero){
            return;
        }

        if(enemy != null){
            dmg =  1;
            enemy.damage(HT,this);
        } else {
            return;
        }
        BossHealthBar.assignBoss(this);

        super.damage(dmg, src, type);
    }

}
