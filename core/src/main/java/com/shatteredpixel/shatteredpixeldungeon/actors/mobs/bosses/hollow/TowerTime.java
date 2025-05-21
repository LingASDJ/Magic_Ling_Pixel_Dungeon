package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Senior;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlueWraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerTimeSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
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
    private static final int MIN_ABILITY_CD = 10;
    private static final int MAX_ABILITY_CD = 20;

    private static final int PLUS_MIN_ABILITY_CD = 7;
    private static final int PLUS_MAX_ABILITY_CD = 12;

    private int turnCount = 0;
    private boolean paralysis = false;
    private int summonedMobs = 1;

    {
        initProperty();
        initBaseStatus(10, 20, 33, 0, 300, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerTimeSprite.class;

        viewDistance = 100;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public int damageRoll() {
        boolean LastHP = HP <= 150;
        return LastHP ? Random.NormalIntRange( 20, 60 ) : Random.NormalIntRange( 15, 40 );
    }

    @Override
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        laserattack();
        ArrayList<Integer> positions = new ArrayList<>();
        if (buff(SummonColdDown.class) == null && state != SLEEPING && summonedMobs <= 5) {
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
            Buff.affect(this, SummonColdDown.class, HP < 151 ? 10f : 20f);
            summonedMobs++;
        }

        return super.act();
    }

   public static class DreamGolem extends Golem {

       {
           maxLvl = -31;
       }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 30, 40 );
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

   public static class DreamWarlock extends Warlock {

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

        @Override
        protected void zap() {
            spend( TIME_TO_ZAP );

            Invisibility.dispel(this);
            Char enemy = this.enemy;
            if (hit( this, enemy, true )) {
                //TODO would be nice for this to work on ghost/statues too
                if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                    Buff.prolong( enemy, Degrade.class, Degrade.DURATION );
                    Sample.INSTANCE.play( Assets.Sounds.DEGRADE );
                }

                int dmg = Random.NormalIntRange( 22, 38 );
                dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
                enemy.damage( dmg, new Warlock.DarkBolt() );

                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Badges.validateDeathFromEnemyMagic();
                    Dungeon.fail( this );
                    GLog.n( Messages.get(this, "bolt_kill") );
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
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
                if (	mob instanceof TowerTime) {
                    ((TowerTime) mob).summonedMobs--;
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
        mobTypes.add(DreamGolem.class);
        mobTypes.add(DreamWarlock.class);
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
        boolean LastHP = HP <= HT/2;

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


            int beams = 0;
            if (LastHP) {
                beams = 12;
            } else  {
                beams = 4;
            }

            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++) {

                int targetPos = hero.pos;
                if (i != 0) {
                    do {
                        targetPos = hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
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
                if(Random.Int(4) == 0 && LastHP){
                    //TODO 半血后有每次攻击有25%的概率造成地方的麻痹效果
                    paralysis = true;
                }
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path) {
                    sprite.parent.add(new TargetedCell(p, paralysis ? Window.CYELLOW : 0xFF0000));
                    affectedCells.add(p);
                }
            }

            //TODO 半血前，在19回合时发出警告 ： 半血后 在 11回合后发出警告
            if (turnCount == (LastHP ? 19 : 11)) {
                for (int i : affectedCells) {
                    sprite.parent.add(new TargetedCell(i,  paralysis ? Window.CYELLOW : 0xFF0000));
                }
            } else {
                spend(GameMath.gate(TICK, hero.cooldown(), 3 * TICK));
                hero.interrupt();
            }

            if(LastHP){
                abilityCooldown += Random.NormalFloat(PLUS_MIN_ABILITY_CD, PLUS_MAX_ABILITY_CD);
            } else {
                abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
            }


        }  {
            spend(TICK);
            if (abilityCooldown > 0) abilityCooldown--;
        }

        turnCount++; // 回合数自增
    }

    @Override
    public void damage(int dmg, Object src) {
        dmg -= dmg * (summonedMobs*5) / 100;
        super.damage(dmg, src);
    }

    protected void zap() {
        next();
    }

    /**
     * 半血是否造成麻痹效果 以及 激光伤害提升
     * @param LastHP 半血血量检测
     * @param ch Char对象
     */
    private void getLaserTargetDamage(boolean LastHP, Char ch) {
        if(LastHP){
            if(paralysis){
                Buff.affect(ch, Paralysis.class, Random.IntRange(2,4));
                paralysis = false;
            }
        }
        ch.damage(damageRoll(), new Eye.DeathGaze());
    }

    private static final String ABILITY_CD = "ability_cd";
    private static final String PARALYSIS = "paralysis";
    private static final String TURN_COUNT = "turn_count";
    private static final String TARGETED_CELLS = "targeted_cells";
    private static final String SUMMONED_MOBS = "summoned_mobs";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ABILITY_CD, abilityCooldown);
        bundle.put(PARALYSIS, paralysis);
        bundle.put(TURN_COUNT, turnCount);
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++) {
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
        bundle.put(SUMMONED_MOBS, summonedMobs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        abilityCooldown = bundle.getFloat(ABILITY_CD);
        paralysis = bundle.getBoolean(PARALYSIS);
        turnCount = bundle.getInt(TURN_COUNT);
        int[] bundleArr = bundle.getIntArray(TARGETED_CELLS);
        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }
        summonedMobs = bundle.getInt(SUMMONED_MOBS);
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

}
