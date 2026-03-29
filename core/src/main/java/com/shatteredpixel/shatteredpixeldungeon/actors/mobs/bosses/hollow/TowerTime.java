package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueWraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerTimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TowerTime extends Boss {

    private ArrayList<Integer> targetedCells = new ArrayList<>();

    private float abilityCooldown;

    private int turnCount = 0;

    private int summonedMobs = 1;

    private boolean LastHP = HP*2 <= HT;

    private boolean paralysedAttackChane = false;

    private int beams = 0;

    {
        initProperty();
        initBaseStatus(10, 20, 33, 0, 400, 0, 0);
        initStatus(120);
        noDropIceCoin = true;
        spriteClass = TowerTimeSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Burning.class);
        immunities.add(Ooze.class);
        immunities.add(Terror.class);
        immunities.add(Hex.class);
        immunities.add(Vertigo.class);
        immunities.add(Blindness.class);

        immunities.add(Blob.class);
        immunities.add(TowerMachine.DeadAlive.class);
    }

    @Override
    public int damageRoll() {
        return 0;
    }

    public void TryGetSummonedMobs() {
        ArrayList<Integer> positions = new ArrayList<>();
        if (buff(TimeSummonColdDown.class) == null && summonedMobs <= 4) {
            Mob testActor = getSummonTimeMobs();
            testActor.state = testActor.HUNTING;
            GameScene.add(testActor);
            /****************/
            positions.add(437);
            positions.add(484);
            positions.add(562);
            positions.add(515);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor,positions.get(0));

            Mob testActor2 = getSummonTimeMobs();
            testActor2.state = testActor2.HUNTING;
            GameScene.add(testActor2);
            /****************/
            positions.add(437);
            positions.add(484);
            positions.add(562);
            positions.add(515);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor2,positions.get(0));
            Buff.affect(this, Barrier.class).setShield(100);
            Buff.affect(this, TimeSummonColdDown.class, 50f);
            summonedMobs+=2;
        }
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
         for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TowerMind || mob instanceof TowerTime||mob instanceof TowerGods||mob instanceof TowerMachine) {
                Buff.affect(mob, TowerParalysis.class).set((21), 1);
            }
             if(mob instanceof Morphs){
                 ((Morphs) mob).phase+=0.25f;
             }
        };
    }

    @Override
    public boolean act() {
        laserattack();
        TowerParalysis towerParalysis = buff(TowerParalysis.class);
        if(towerParalysis == null){
            TryGetSummonedMobs();
        }


        if (!LastHP) {
            beams = 11;
        } else  {
            beams = 8;
        }
        return super.act();
    }

   public static class DreamShaman extends Shaman {

       {
           maxLvl = -31;
       }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 10, 40 );
        }
        @Override
        public int attackSkill( Char target ) {
            return 60;
        }

       @Override
       public void die( Object cause ) {
           super.die( cause );
           for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
               if (	mob instanceof TowerTime) {
                  ((TowerTime) mob).summonedMobs--;
               }
           }
       }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(2, 5);
        }

       @Override
       protected void debuff(Char enemy) {

       }


   }

   public static class DreamSenior extends Senior {

       {
           maxLvl = -31;
       }

       @Override
       public void move( int step, boolean travelling) {
           if (travelling) focusCooldown -= 1.5f;
           super.move( step, travelling);
       }

       @Override
       public void die( Object cause ) {
           super.die( cause );
           for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
               if (	mob instanceof TowerTime) {
                   ((TowerTime) mob).summonedMobs--;
               }
           }
       }

       @Override
       public int attackSkill( Char target ) {
           return 40;
       }

       @Override
       public int damageRoll() {
           return Random.NormalIntRange( 45, 55);
       }
    }

   public static class DreamsElemental extends Elemental.ChaosElemental {

       {
           maxLvl = -31;
       }

        public static class DarkBolt{}
        private static final float TIME_TO_ZAP	= 1f;

       @Override
       public void die( Object cause ) {
           super.die( cause );
           for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
               if (	mob instanceof TowerTime) {
                   ((TowerTime) mob).summonedMobs--;
               }
           }
       }
    }

    public static class DreamFireGhost extends Mob {
        {
            spriteClass = BlueWraithSprite.class;
            HT = 75;
            HP = 75;
            defenseSkill = 10;
            EXP = 6;
            maxLvl = -31;
            flying = true;
            loot = new PotionOfLiquidFlame();
            lootChance = 0.1f;
            properties.add(Char.Property.FIERY);
        }

        @Override
        public void die( Object cause ) {
            super.die( cause );
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof TowerTime) {
                    ((TowerTime) mob).summonedMobs--;
                }
                if(mob instanceof DreamSenior || mob instanceof DreamShaman || mob instanceof DreamsElemental || mob instanceof DreamFireGhost){
                    mob.die(true);
                }
            }
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            int combo = 5;
            int damage2 = super.attackProc(enemy, combo + 2);
            if (Dungeon.level.flamable[enemy.pos]) {
                GameScene.add(Blob.seed(enemy.pos, 7, HalomethaneFire.class));
            }
            if (enemy.buff(HalomethaneBurning.class) == null) {
                Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy);
            }
            return damage2;
        }

        public int attackSkill(Char charR) {
            return 54;
        }

        public int damageRoll() {
            return Random.NormalIntRange(24, 36);
        }

        public int drRoll() {
            return Random.NormalIntRange(0, 2);
        }
    }

    private Mob getSummonTimeMobs() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(DreamShaman.random());
        mobTypes.add(DreamsElemental.class);
        mobTypes.add(DreamFireGhost.class);
        mobTypes.add(DreamSenior.class);
        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
        return mob;
    }

    private void laserattack(){
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();

        //TODO 瘫痪状态下无法使用任何技能
        TowerParalysis towerParalysis = buff(TowerParalysis.class);
        if(towerParalysis != null) return;

        if (!hero.rooted) {
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                //shoot beams

                sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                for (int p : b.path) {
                    Char ch = Actor.findChar(p);
                    if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
                        affected.add(ch);

                    }
                    if (Dungeon.level.flamable[p]) {
                        Dungeon.level.destroy(p);
                        GameScene.updateMap(p);
                        terrainAffected = true;

                    }
                }
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
            for (Char ch : affected) {

                getLaserTargetDamage(LastHP, ch);

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }
                if (!ch.isAlive() && ch == hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }
            targetedCells.clear();
        }

        if (abilityCooldown <= 0  && alignment == Alignment.ENEMY) {
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++) {

                int targetPos = hero.pos;
                if (i != 0) {
                    do {
                        targetPos = hero.pos + PathFinder.NEIGHBOURS8[Random.Int(beams)];
                    } while (Dungeon.level.trueDistance(pos, hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (!affectedCells.contains(hero.pos + i) && Dungeon.level.passable[hero.pos + i]) {
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted) {
                targetedCells.remove(targetedCells.size() - 1);
            }
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path) {
                    if(paralysedAttackChane){
                        sprite.parent.add(new ColorTargetedCell(p, Window.CYELLOW));
                    } else {
                        sprite.parent.add(new TargetedCell(p,  0xFF0000));
                    }

                    affectedCells.add(p);
                }
            }

            if (turnCount == 10) {
                for (int i : affectedCells) {
                    if(paralysedAttackChane){
                        sprite.parent.add(new ColorTargetedCell(i, Window.CYELLOW));
                    } else {
                        sprite.parent.add(new TargetedCell(i,  0xFF0000));
                    }
                }
            } else {
                spend(GameMath.gate(TICK, hero.cooldown(), 3 * TICK));
                hero.interrupt();
            }

            abilityCooldown += 9;

        }  {
            spend(TICK);
            if (abilityCooldown > 0) abilityCooldown--;
        }

        turnCount++; // 回合数自增
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if(src == TowerMachine.class){
            return;
        }
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }

        dmg -= dmg * (summonedMobs*5) / 100;
        super.damage(dmg, src, type);
    }

    protected void zap() {
        next();
    }

    /**
     * 半血前造成残废效果<br>
     * 半血50%概率造成麻痹效果 <br>
     * 半血后激光伤害提升
     * @param LastHP 半血血量检测
     * @param ch Char对象
     */
    private void getLaserTargetDamage(boolean LastHP, Char ch) {

        if(!LastHP && paralysedAttackChane){
            Buff.affect(ch, Paralysis.class, Random.IntRange(2,4));
            paralysedAttackChane = false;
        } else {
            Buff.affect(ch, Cripple.class, Random.IntRange(2,4));
        }
        if(!paralysedAttackChane && !LastHP && Random.Int(2) == 0){
            paralysedAttackChane = true;
        }
        ch.damage(LastHP ? 85 : 40, new Eye.DeathGaze());

        Statistics.bossScores[6] -= 400;
    }

    private static final String ABILITY_CD = "ability_cd";
    private static final String PARALYSIS = "paralysis";
    private static final String TURN_COUNT = "turn_count";
    private static final String TARGETED_CELLS = "targeted_cells";
    private static final String SUMMONED_MOBS = "summoned_mobs";
    private static final String LAST_HP = "last_hp";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ABILITY_CD, abilityCooldown);
        bundle.put(PARALYSIS, paralysedAttackChane);
        bundle.put(TURN_COUNT, turnCount);
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++) {
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
        bundle.put(SUMMONED_MOBS, summonedMobs);
        bundle.put(LAST_HP, LastHP);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        abilityCooldown = bundle.getFloat(ABILITY_CD);
        paralysedAttackChane = bundle.getBoolean(PARALYSIS);
        turnCount = bundle.getInt(TURN_COUNT);
        int[] bundleArr = bundle.getIntArray(TARGETED_CELLS);
        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }
        summonedMobs = bundle.getInt(SUMMONED_MOBS);
        LastHP = bundle.getBoolean(LAST_HP);
    }

    public static class TimeSummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
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

}