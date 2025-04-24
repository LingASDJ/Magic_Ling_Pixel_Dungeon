package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPHeal;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPRed;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.custom.CPShield;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class GoblinShaman extends GoldMob {

    {
        HP = HT = 70;
        defenseSkill = 5;
        viewDistance = 4;
        baseSpeed = 1.25f;
        EXP = 10;
        maxLvl = 15;
        immunities.add(Terror.class);

        loot = Generator.Category.RING;
        lootChance = 0.03f;
    }

    @Override
    public Item createLoot() {
        Dungeon.LimitedDrops.GOLIN_RING.count++;
        return super.createLoot();
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 1/3 as likely
        // so loot chance looks like: 1/33, 1/100, 1/300, 1/900, etc.
        return super.lootChance() * (float)Math.pow(1/3f, Dungeon.LimitedDrops.GOLIN_RING.count);
    }

    public Buff buff;

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 9, 20 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(1, 4);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy)
                || (new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos&&(distance(enemy)<5));
    }

    @Override
    public boolean attack(Char enemy, float dmgMulti, float dmgBonus, float accMulti) {
        ArrayList<Char> charsToEmpower = new ArrayList<>();
        for (Mob ch: Dungeon.level.mobs) {
            if (distance(ch) < 5 && ch.alignment == alignment && !(this instanceof ShamanRegen && ch.HP>=ch.HT)) {
                charsToEmpower.add(ch);
            }
        }
        if (!charsToEmpower.isEmpty()) {
            if (buff!=null) {
                Char empoweredChar = charsToEmpower.get(Random.Int(charsToEmpower.size()));
                if (this instanceof ShamanRegen){
                    for (int i = 0; i < 10;i++) if (empoweredChar.HP<empoweredChar.HT) empoweredChar.HP++;
                } else if (buff instanceof Barrier) {
                    Buff.affect(empoweredChar, Barrier.class).setShield(Random.Int(3,6));
                } else if (buff instanceof FlavourBuff) {
                    FlavourBuff buff2 = (FlavourBuff) buff;
                    Buff.affect(empoweredChar, buff2.getClass(), 10);
                } else Buff.affect(empoweredChar, buff.getClass());
                if (this instanceof ShamanRegen) {
                    CellEmitter.floor(empoweredChar.pos).start(CPHeal.UP, 0.005f, 10);
                } else if (this instanceof ShamanStrength) {
                    CellEmitter.floor(empoweredChar.pos).start(CPRed.UP, 0.005f, 10);
                } else if (this instanceof ShamanShield) {
                    CellEmitter.floor(empoweredChar.pos).start(CPShield.TOCENTER, 0.005f, 15);
                }
            }
        }
        return true;
    }

    public static class ShamanStrength extends GoblinShaman {
        {
            buff = new ChampionEnemy.Blessed();
            spriteClass = GoblinShamanSprite.ShamanStrength.class;

            Buff giantBuff = buff(ChampionEnemy.Blessed.class);
            if(giantBuff != null){
                giantBuff.detach();
            }
        }
    }
    public static class ShamanShield extends GoblinShaman {
        {
            spriteClass = GoblinShamanSprite.ShamanShield.class;
            buff = new Barrier();
        }
    }
    public static class ShamanRegen extends GoblinShaman {
        {
            spriteClass = GoblinShamanSprite.ShamanRegen.class;
            buff = new Regeneration();
        }
    }
    public static class ShamanFake extends GoblinShaman {
        {
            spriteClass = GoblinShamanSprite.ShamanFake.class;
            buff = new Bless();
        }
    }

    public static Class<? extends GoblinShaman> random(){
        float roll = Random.Float();
        if (roll < 0.3f){
            return GoblinShaman.ShamanRegen.class;
        } else if (roll < 0.6f){
            return GoblinShaman.ShamanStrength.class;
        } else if (roll < 0.9f) {
            return GoblinShaman.ShamanShield.class;
        }
        return GoblinShaman.ShamanFake.class;
    }
    public static Mob randomInstance(){
        float roll = Random.Float();
        if (roll < 0.3f){
            return new GoblinShaman.ShamanRegen();
        } else if (roll < 0.6f){
            return new GoblinShaman.ShamanStrength();
        } else if (roll < 0.9f) {
            return new GoblinShaman.ShamanShield();
        }
        return new GoblinShaman.ShamanFake();
    }

}
