package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.spical.GalaxyKeyBossLevel.RandomPos;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrivusFruits;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SothothEyeDiedSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SothothEyeDied extends Boss {

    {
        spriteClass = SothothEyeDiedSprite.class;

        HP = HT = 750;
        defenseSkill = 2;

        EXP = 0;

        maxLvl = 99;

        state = WANDERING;

        properties.add(Property.BOSS);
    }

    {
        //免疫全部气体
        immunities.add(Blob.class);

        immunities.add(Terror.class);
        immunities.add(Vertigo.class);
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Freezing.class);
        immunities.add(Frost.class);
    }

    public void onZapComplete() {
        next();
    }

    private static int BombReach;
    private boolean endAttack = false;
    private int endAttackCount;


    @Override
    protected boolean act() {

        laserattack();

        if(!endAttack){
            BombReach = 8;
        } else {
            BombReach = 24;
        }

        if(endAttack){
            endAttackCount++;
        }

        //谢幕
        if(endAttackCount > 10 && endAttack){
            final int finalTargetCell = 1182;
            final SothothEyeDied.DiedGalaxy.BombItem item = new SothothEyeDied.DiedGalaxy.BombItem();
            sprite.zap(finalTargetCell);
            endAttackCount = -1000;
            ((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
                    reset(this.sprite,
                            finalTargetCell,
                            item,
                            new Callback() {
                                @Override
                                public void call() {
                                    item.onThrow(finalTargetCell);
                                    SothothEyeDied.this.next();
                                }
                            });
        } else if(Statistics.killYogMobsAnargy >= 16){
            final int finalTargetCell = 1182;
            final SothothEyeDied.DiedGalaxy.BombItem item = new SothothEyeDied.DiedGalaxy.BombItem();
            sprite.zap(finalTargetCell);
            ((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
                    reset(this.sprite,
                            finalTargetCell,
                            item,
                            new Callback() {
                                @Override
                                public void call() {
                                    item.onThrow(finalTargetCell);
                                    SothothEyeDied.this.next();
                                }
                            });
            yell(Messages.get(this,"galaxy"));
            Statistics.killYogMobsAnargy = 0;
        }

        //敌方灵视
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob.buff(MindVision.class) == null && mob.alignment == Alignment.ENEMY){
                Buff.affect(mob, MindVision.class, 50f);
            }
        }




        //全局灵视 + 全局虚弱
        Buff.affect(hero, Weakness.class, 10);
        Buff.affect(hero, MindVision.class, 10);

        if(hero.buff(LockedFloor.class) != null){
            notice();
        }

        return super.act();
    }

    public boolean first = true;
    public boolean second = true;
    protected boolean hasRaged = false;

    private static final String FIRST = "first";
    private static final String SECOND = "first";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECOND, second);
        bundle.put("ENDATC2", endAttack);
        bundle.put("ENDATC", endAttackCount);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        second = bundle.getBoolean(SECOND);
        endAttack = bundle.getBoolean("ENDATC2");
        endAttackCount = bundle.getInt("ENDATC");
    }

    public synchronized boolean isAlive() {
        if (super.isAlive()){
            return true;
        } else {
            if (!hasRaged){
                triggerEnrage();
            }
            return !buffs(YogDiedGalaxy.class).isEmpty();
        }
    }

    public static class YogDiedGalaxy extends Buff {

        private ArrayList<Emitter> smokeEmitters = new ArrayList<>();

        @Override
        public boolean act() {
            spend(TICK);
            return true;
        }

    }

    protected void triggerEnrage(){
        Buff.affect(this, YogDiedGalaxy.class);
        endAttack = true;
        GLog.n(Messages.get(this, "deadsling"));
        spend( TICK );
        hasRaged = true;
    }



    private ArrayList<Integer> targetedCells = new ArrayList<>();

    private float abilityCooldown;
    private static final int MIN_ABILITY_CD = 10;
    private static final int MAX_ABILITY_CD = 15;
    private void laserattack() {
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();

        // Delay fire on a rooted hero
        if (!hero.rooted) {
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);

                // Shoot beams
                sprite.parent.add(new Beam.DeathRayS(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
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
                ch.damage(Random.NormalIntRange(16, 32), new Eye.DeathGaze());

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == hero) {
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }

            targetedCells.clear();
        }

        if (abilityCooldown <= 0){

            int beams = 4;
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++){

                int targetPos = Dungeon.hero.pos;
                if (i != 0){
                    do {
                        targetPos = Dungeon.hero.pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                    } while (Dungeon.level.trueDistance(pos, Dungeon.hero.pos)
                            > Dungeon.level.trueDistance(pos, targetPos));
                }
                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9){
                if (!affectedCells.contains(Dungeon.hero.pos + i) && Dungeon.level.passable[Dungeon.hero.pos + i]){
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted){
                targetedCells.remove(targetedCells.size()-1);
            }
            for (int i : targetedCells){
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path){
                    sprite.parent.add(new TargetedCell(p, 0xFF0000));
                    affectedCells.add(p);
                }
            }

            //don't want to overly punish players with slow move or attack speed
            spend(GameMath.gate(TICK, (int)Math.ceil(Dungeon.hero.cooldown()), 3*TICK));
            Dungeon.hero.interrupt();

            if(!buffs(YogDiedGalaxy.class).isEmpty()){
                abilityCooldown += 1;
            } else if(HP<=250) {
                abilityCooldown += Random.NormalFloat(5,10);
            } else {
                abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);
            }



        } else {
            spend(TICK);
            if (abilityCooldown > 0) {
                abilityCooldown--;
            }
        }
    }

    @Override
    public void damage(int dmg, Object src) {
        int count = 0;

        if(HP<=250 && second){
            Set<Integer> selectedPositions = new HashSet<>();
            int maxPositions = Math.min(RandomPos.length, 4);

            while (selectedPositions.size() < maxPositions) {
                int index = Random.Int(RandomPos.length);
                selectedPositions.add(RandomPos[index]);
            }

            for (int pos : selectedPositions) {
                ServantAvgomon csp = new ServantAvgomon();
                csp.pos = pos;
                GameScene.add(csp);
            }

            for (int pos : selectedPositions) {
                SothothLasher csp = new SothothLasher();
                csp.pos = pos;
                GameScene.add(csp);
            }

            Buff.affect( this, CrivusFruits.CFBarrior.class ).setShield(hero.belongings.weapon.max());

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof SothothLasher
                        || mob instanceof ServantAvgomon) {
                    Buff.affect(mob, Adrenaline.class,50f);
                }
            }
            HP = 250;
            second = false;
        }

        if(HP<=500 && first){
            Set<Integer> selectedPositions = new HashSet<>();
            int maxPositions = Math.min(RandomPos.length, 4);

            while (selectedPositions.size() < maxPositions) {
                int index = Random.Int(RandomPos.length);
                selectedPositions.add(RandomPos[index]);
            }

            for (int pos : selectedPositions) {
                ServantAvgomon csp = new ServantAvgomon();
                csp.pos = pos;
                GameScene.add(csp);
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof SothothLasher
                        || mob instanceof ServantAvgomon) {
                    Buff.affect(mob, Adrenaline.class,50f);
                }
            }
            HP = 500;
            first = false;
        }

        // 统计所有 Servant-Avgomon 的数量
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ServantAvgomon) {
                count += (int) mob.spawningWeight();
            }
        }

        //亚弗戈蒙之仆
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            if (Dungeon.level.passable[pos+n] && Actor.findChar( pos+n ) == null) {
                candidates.add( pos+n );
            }
        }

        if(Random.Float()<=0.15f && count<6){
            ServantAvgomon spawn = new ServantAvgomon();
            spawn.pos = Random.element( candidates );
            spawn.state = spawn.HUNTING;
            GameScene.add( spawn, 1 );
            Dungeon.level.occupyCell(spawn);
        }

        // 计算总的伤害减免百分比
        // 每个亚弗戈蒙之仆减少 10% 的伤害，总减免上限为 99%
        float reduction = Math.min(count * 0.10f, 0.99f);

        float reducedDamage = dmg * (1 - reduction);

        float scaleFactor = AscensionChallenge.statModifier(this);
        int scaledDmg = Math.round(reducedDamage / scaleFactor);

        if (scaledDmg >= 40) {
            scaledDmg = 24 + (int) (Math.sqrt(3 * (scaledDmg - 4) + 1) - 1) / 2;
        }

        dmg = (int) (scaledDmg * scaleFactor);
        super.damage(dmg, src);
    }


    //万物归一
    public static class DiedGalaxy extends Buff {

        public int bombPos = -1;
        private int timer = 10;

        private ArrayList<Emitter> smokeEmitters = new ArrayList<>();

        @Override
        public boolean act() {

            if (smokeEmitters.isEmpty()){
                fx(true);
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof SothothEyeDied){
                    ((SothothEyeDiedSprite)mob.sprite).setWWW();
                }
            }

            PointF p = DungeonTilemap.raisedTileCenterToWorld(bombPos);
            if (timer == 10) {
                FloatingText.show(p.x, p.y, bombPos, "♪~", CharSprite.WARNING);
            } else if (timer == 5){
                FloatingText.show(p.x, p.y, bombPos, "♪~", CharSprite.WARNING);
            } else if (timer == 3){
                FloatingText.show(p.x, p.y, bombPos, "♪~", CharSprite.WARNING);
            } else if(timer <= 0) {
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), SothothEyeDied.BombReach );
                for (int cell = 0; cell < PathFinder.distance.length; cell++) {

                    if (PathFinder.distance[cell] < Integer.MAX_VALUE) {
                        Char ch = Actor.findChar(cell);
                        if (ch != null && !(ch instanceof SothothEyeDied)) {
                            int dmg = 99;
                            ch.damage(dmg, this);

                            //最终审判
                            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                                if (mob instanceof SothothEyeDied) {

                                    Buff.detach(mob, RoseShiled.class);

                                    if(mob.HP<1){
                                        mob.die(true);
                                    }
                                }
                            }
                        }
                    }

                }

                Heap h = Dungeon.level.heaps.get(bombPos);
                if (h != null) {
                    for (Item i : h.items.toArray(new Item[0])) {
                        if (i instanceof SothothEyeDied.DiedGalaxy.BombItem) {
                            h.remove(i);
                        }
                    }
                }
                Sample.INSTANCE.play(Assets.Sounds.BLAST);
                detach();
                return true;
            }

            timer--;
            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {

            if (on && bombPos != -1){
                PathFinder.buildDistanceMap( bombPos, BArray.not( Dungeon.level.solid, null ), SothothEyeDied.BombReach );
                for (int i = 0; i < PathFinder.distance.length; i++) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        Emitter e = CellEmitter.get(i);
                        e.pour( RainbowParticle.BURST, 0.25f );
                        smokeEmitters.add(e);
                    }
                }
            } else if (!on) {
                for (Emitter e : smokeEmitters){
                    e.burst(RainbowParticle.BURST, 4);
                }
            }
        }

        private static final String BOMB_POS = "bomb_pos";

        private static final String TIMER = "timer";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put( BOMB_POS, bombPos );
            bundle.put( TIMER, timer );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            bombPos = bundle.getInt( BOMB_POS );
            timer = bundle.getInt( TIMER );
        }

        public static class BombItem extends Item {

            {
                dropsDownHeap = true;
                unique = true;

                image = ItemSpriteSheet.AMULET;
            }

            @Override
            public boolean doPickUp(Hero hero, int pos) {
                GLog.w( Messages.get(this, "cant_pickup") );
                return false;
            }

            @Override
            protected void onThrow(int cell) {
                super.onThrow(cell);

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                    if (mob instanceof SothothEyeDied) {
                        Buff.append(mob, DiedGalaxy.class).bombPos = 1182;
                        Buff.prolong(mob, RoseShiled.class, 1000f);
                    }
                }


            }

            @Override
            public Emitter emitter() {
                Emitter emitter = new Emitter();
                emitter.pos(7.5f, 3.5f);
                emitter.fillTarget = false;
                emitter.pour(SmokeParticle.SPEW, 0.05f);
                return emitter;
            }
        }
    }


    @Override
    public void notice()
     {
         super.notice();
         BossHealthBar.assignBoss(this);
     }


    //谢幕
    @Override
    public void die( Object cause ) {
        super.die( cause );
        Dungeon.level.unseal();
        GameScene.bossSlain();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SothothLasher
                    || mob instanceof ServantAvgomon) {
               mob.die(null);
            }
        }

        Dungeon.level.drop(new Gold().quantity(1000), pos).sprite.drop();

//        SPDSettings.unlockItem("avatars_warrior_3");
//        SPDSettings.unlockItem("avatars_mage_3");
//        SPDSettings.unlockItem("avatars_rogue_3");
//        SPDSettings.unlockItem("avatars_huntress_3");
//        SPDSettings.unlockItem("avatars_duelist_3");

        PaswordBadges.KILL_YOGSTS();
        cause = new EndYog();
        ((EndYog) cause).pos = pos;
        GameScene.add(((Mob) (cause)));
        Actor.addDelayed(new Pushing(((Char) (cause)), pos, ((EndYog) (cause)).pos), -1F);

    }

}
