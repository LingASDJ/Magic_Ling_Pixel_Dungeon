//package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;
//
//import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
//
//import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
//import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChaosTime;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MaxGuard;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.SoulMark;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
//import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
//import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
//import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
//import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
//import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
//import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
//import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
//import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
//import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
//import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
//import com.shatteredpixel.shatteredpixeldungeon.sprites.PillarSprite;
//import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
//import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
//import com.watabou.utils.Bundle;
//import com.watabou.utils.GameMath;
//import com.watabou.utils.PathFinder;
//import com.watabou.utils.Random;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//
//public abstract class SeptiumPillar extends Mob {
//
//    private boolean firstactive = false;
//    private boolean secondactive = false;
//    private boolean thirdactive = false;
//
//    {
//        HP = HT = 100;
//
//        viewDistance = 99;
//
//        //for doomed resistance
//        EXP = 25;
//        maxLvl = -2;
//        baseSpeed = 0;
//
//        state = PASSIVE;
//        alignment = Alignment.NEUTRAL;
//
//        properties.add(Property.BOSS);
//        properties.add(Property.KISEKI);
//        properties.add(Property.IMMOVABLE);
//        properties.add(Property.INORGANIC);
//        immunities.add( Terror.class );
//        immunities.add( Amok.class );
//        immunities.add( Charm.class );
//        immunities.add( Sleep.class );
//        immunities.add( Vertigo.class );
//        immunities.add( Paralysis.class );
//        immunities.add( SoulMark.class );
//        immunities.add( Weakness.class );
//        immunities.add( Blindness.class );
//    }
//
//    @Override
//    protected boolean act()
//
//    {
//        if (alignment == Alignment.NEUTRAL){
//            next();
//        }
//        else
//        {
//            onZapComplete();
//            state = PASSIVE;
//        }
//        return super.act();
//    }
//
//    @Override
//    public void notice() {
//        //do nothing
//    }
//
//
//
//    @Override
//    public void add(Buff buff) {
//        //immune to all buffs/debuffs when inactive
//        if (alignment != Alignment.NEUTRAL) {
//            super.add(buff);
//        }
//    }
//
//    private static final String ALIGNMENT = "alignment";
//
//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put(ALIGNMENT, alignment);
//
//    }
//
//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        alignment = bundle.getEnum(ALIGNMENT, Alignment.class);
//
//    }
//
//    @Override
//    public boolean interact(Char c) {
//        return true;
//    }
//
//    protected abstract void zap();
//
//
//
//    public void onZapComplete(){
//        zap();
//        next();
//    }
//
//
//
//    public void activate(){
//        alignment = Alignment.ENEMY;
//        ((PillarSprite) sprite).activate();
//    }
//
//    @Override
//    public String description() {
//        return Messages.get(SeptiumPillar.class, "desc") + "\n\n" + Messages.get(this, "desc");
//    }
//
//    {
//        immunities.add( Terror.class );
//        immunities.add( Amok.class );
//        immunities.add( Charm.class );
//        immunities.add( Sleep.class );
//        immunities.add( Vertigo.class );
//        immunities.add( Paralysis.class );
//        immunities.add( SoulMark.class );
//        immunities.add( Weakness.class );
//        immunities.add( Blindness.class );
//    }
//
//    public static class FlamePillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Flame.class;
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//        @Override
//        protected void zap() {
//            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
//                if(!mob.properties().contains(Property.KISEKI)) {
//                    Buff.affect(mob, FireImbue.class).set( 10f);
//                }
//                next();
//            }
//            GLog.w(Messages.get( this, "message") );
//            spend(15f);
//        }
//
//    }
//
//    public static class GroundPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Ground.class;
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//
//        @Override
//        protected void zap() {
//            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
//                if(!mob.properties().contains(Property.KISEKI)) {
//                    Buff.affect(mob, MaxGuard.class);
//                }
//                next();
//            }
//            GLog.w(Messages.get( this, "message") );
//            spend(15f);
//        };
//
//
//
//
//
//    }
//
//    public static class WaterPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Water.class;
//
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//        @Override
//        protected void zap() {
//            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
//                if(!mob.properties().contains(Property.KISEKI)) {
//                    if (mob.HP < mob.HT/3){
//
//                        mob.HP = mob.HP+20;
//
//                    }
//
//
//                }
//
//            }
//            GLog.w(Messages.get( this, "message") );
//            spend(15f);
//        };
//
//
//
//    }
//
//    public static class WindPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Wind.class;
//
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//        @Override
//        protected void zap() {
//            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
//                if(!mob.properties().contains(Property.KISEKI)) {
//                    Buff.affect(mob, Adrenaline.class,3f);
//                }
//                next();
//            }
//            GLog.w(Messages.get( this, "message") );
//            spend(15f);
//        }
//    }
//
//    public static class ChronosPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Chronos.class;
//
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//        @Override
//        protected void zap() {
//
//            spend(15f);
//            Buff.affect( hero, ChaosTime.class, 15f);
//            GLog.w(Messages.get( this, "message") );
//
//        }
//
//
//    }
//
//
//    public static class HeavenPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Heaven.class;
//        }
//
//        private ArrayList<Integer> targetedCells = new ArrayList<>();
//
//        private float abilityCooldown;
//        private static final int MIN_ABILITY_CD = 10;
//        private static final int MAX_ABILITY_CD = 15;
//
//        // 添加一个计数器，用来记录回合数
//        private int turnCount = 0;
//
//        @Override
//        protected boolean act() {
//            boolean terrainAffected = false;
//            HashSet<Char> affected = new HashSet<>();
//            //delay fire on a rooted hero
//            if (!hero.rooted && alignment == Alignment.ENEMY && firstActive) {
//                for (int i : targetedCells) {
//                    Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
//                    //shoot beams
//                    sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
//                    for (int p : b.path) {
//                        Char ch = Actor.findChar(p);
//                        if (ch != null && (ch.alignment != alignment || ch instanceof Bee)) {
//                            affected.add(ch);
//                        }
//                        if (Dungeon.level.flamable[p]) {
//                            Dungeon.level.destroy(p);
//                            GameScene.updateMap(p);
//                            terrainAffected = true;
//                        }
//                    }
//                }
//                if (terrainAffected) {
//                    Dungeon.observe();
//                }
//                for (Char ch : affected) {
//                    ch.damage(Random.NormalIntRange(20, 30), new Eye.DeathGaze());
//
//                    if (Dungeon.level.heroFOV[pos]) {
//                        ch.sprite.flash();
//                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
//                    }
//                    if (!ch.isAlive() && ch == hero) {
//                        Dungeon.fail(getClass());
//                        GLog.n(Messages.get(Char.class, "kill", name()));
//                    }
//                }
//                targetedCells.clear();
//            }
//
//            if (abilityCooldown <= 0 && alignment == Alignment.ENEMY && firstActive) {
//                int beams = 1;
//                GLog.w(Messages.get(this, "message"));
//                HashSet<Integer> affectedCells = new HashSet<>();
//                for (int i = 0; i < beams; i++) {
//
//                    int targetPos = hero.pos;
//                    if (i != 0) {
//                        do {
//                            targetPos = hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
//                        } while (Dungeon.level.trueDistance(pos, hero.pos)
//                                > Dungeon.level.trueDistance(pos, targetPos));
//                    }
//                    targetedCells.add(targetPos);
//                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
//                    affectedCells.addAll(b.path);
//                }
//
//                //remove one beam if multiple shots would cause every cell next to the hero to be targeted
//                boolean allAdjTargeted = true;
//                for (int i : PathFinder.NEIGHBOURS9) {
//                    if (!affectedCells.contains(hero.pos + i) && Dungeon.level.passable[hero.pos + i]) {
//                        allAdjTargeted = false;
//                        break;
//                    }
//                }
//                if (allAdjTargeted) {
//                    targetedCells.remove(targetedCells.size() - 1);
//                }
//                for (int i : targetedCells) {
//                    Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
//                    for (int p : b.path) {
//                        sprite.parent.add(new TargetedCell(p, 0xFF0000));
//                        affectedCells.add(p);
//                    }
//                }
//
//                // 在14回合时发出警告
//                if (turnCount == 14) {
//                    for (int i : affectedCells) {
//                        sprite.parent.add(new TargetedCell(i, 0xFF0000));
//                    }
//                }
//
//                // don't want to overly punish players with slow move or attack speed
//                spend(GameMath.gate(TICK, hero.cooldown(), 3 * TICK));
//                hero.interrupt();
//
//                abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
//
//            } else {
//                spend(TICK);
//                if (abilityCooldown > 0) abilityCooldown--;
//            }
//
//            turnCount++; // 回合数自增
//
//
//            return true;
//        }
//
//        @Override
//        protected void zap() {
//            next();
//        }
//    }
//
//
//
//
//
//
//    public static class PhantomPillar extends SeptiumPillar {
//
//        {
//            spriteClass = PillarSprite.Phantom.class;
//
//        }
//
//        @Override
//        protected boolean act() {
//            {
//                state = PASSIVE;
//            }
//            return super.act();
//        }
//
//        @Override
//        protected void zap() {
//
//            Buff.affect( hero, Vertigo.class, 3f);
//            for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
//                mob.beckon( hero.pos );
//            }
//            hero.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
//            GLog.w(Messages.get( this, "message") );
//            spend(15f);
//        }
//
//
//
//
//    }
//
//
//
//}
//
