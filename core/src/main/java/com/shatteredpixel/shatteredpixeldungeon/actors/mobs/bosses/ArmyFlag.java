package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BrokenArmorFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ConfusionGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArmyFlagSprite;
import com.watabou.utils.Bundle;

public class ArmyFlag extends Mob {
    private int enemyPhase;
    private int friendPhase;

    public int ThreePhase;

    private float spawnCooldown = 0;

    {
        spriteClass = ArmyFlagSprite.class;

        HP = HT = 40;

        state = PASSIVE;

        maxLvl = -1;

        properties.add(Property.MINIBOSS);
        properties.add(Property.INORGANIC);
        properties.add(Property.ELECTRIC);
        properties.add(Property.IMMOVABLE);
        immunities.add(BrokenArmorFire.class);
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( CorrosiveGas.class );
        immunities.add( ConfusionGas.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
        immunities.add( CrivusFruits.DiedBlobs.class );
        immunities.add( ToxicGas.class );
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof DwarfGeneral) {
                if (((DwarfGeneral) mob).phase >= 2) {
                    if (ThreePhase < 5) {
                        ThreePhase++;
                    }
                }
            }
        }

    }

    @Override
    protected boolean act() {

        enemyPhase++;
        friendPhase++;

        if(enemyPhase ==9){
            for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (boss.alignment == Alignment.ENEMY) {
                    Buff.affect(boss, Adrenaline.class, 3f);
                    enemyPhase = 0;
                }
            }
            enemyPhase = 0;
        }

        if(friendPhase == 7){
             for (Mob boss : Dungeon.level.mobs.toArray(new Mob[0])) {
                 if (!(boss.alignment == Alignment.ENEMY)) {
                     Buff.affect(boss, Cripple.class, 2f);
                     Buff.affect(hero, Cripple.class, 2f);
                 }
             }
            friendPhase = 0;
        }

        alerted = false;
        state = PASSIVE;

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 1;
            lock.addTime(dmg*multiple);
        }

        super.damage(dmg, src);
    }

    private static final String FRIENPHASE = "frienphase";
    private static final String ENEMYPHASE = "enemyphase";
    public static final String SPAWN_COOLDOWN = "spawn_cooldown";

    public static final String THREE_PHAE = "three_phae";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN_COOLDOWN, spawnCooldown);
        bundle.put(FRIENPHASE,friendPhase);
        bundle.put(ENEMYPHASE,enemyPhase);
        bundle.put(THREE_PHAE,ThreePhase);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        enemyPhase = bundle.getInt(ENEMYPHASE);
        friendPhase = bundle.getInt(FRIENPHASE);
        spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
        ThreePhase = bundle.getInt(THREE_PHAE);
    }

}

