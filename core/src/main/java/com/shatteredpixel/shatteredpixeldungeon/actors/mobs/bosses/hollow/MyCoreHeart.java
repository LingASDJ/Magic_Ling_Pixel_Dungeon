package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerGodsBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerMachineBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerMindBad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.bad.TowerTimeBad;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.GalaxyHeartDeadEndPlot;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MyCoreHeartSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class MyCoreHeart extends Boss {

    private int summonedmobsCount = 1;

    private int brokenCount;

    {
        initProperty();
        initBaseStatus(0, 0, 0, 0, 30, 0, 0);
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
        immunities.add(Charm.class);
        immunities.add(Barrier.class);

        state = WANDERING = new Waiting();
    }

    private Mob getSummonTimeMobs() {
        Mob mob = Reflection.newInstance(MobSpawner.getMobRotation(summonedmobsCount).get(0));
        return mob;
    }

    private boolean getHP = false;

    public int end;

    private boolean one = false;
    private boolean two = false;
    private boolean three = false;
    private boolean four = false;

    @Override
    public boolean isAlive() {
        return true;
    }

    boolean talk = false;
    public boolean act() {
        alerted = false;
        state = PASSIVE;
        if(HP == 0 && !talk){
            GalaxyHeartDeadEndPlot plot = new GalaxyHeartDeadEndPlot();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot, false));
                }
            });
            talk = true;
        }


        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if(mob instanceof TowerGodsBad && ((TowerGodsBad) mob).repiaer && !one && mob.buff(RepaierDown.class) == null){
                end++;
                one = true;
                break;
            }
            if(mob instanceof TowerMachineBad && ((TowerMachineBad) mob).repiaer && !two && mob.buff(RepaierDown.class) == null){
                end++;
                two = true;
                break;
            }
            if(mob instanceof TowerMindBad && ((TowerMindBad) mob).repiaer && !three && mob.buff(RepaierDown.class) == null){
                end++;
                three = true;
                break;
            }
            if(mob instanceof TowerTimeBad && ((TowerTimeBad) mob).repiaer && !four && mob.buff(RepaierDown.class) == null){
                end++;
                four = true;
                break;
            }
        }
        if(end >= 4){
            ScrollOfTeleportation.appear(this,562);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
               if(mob instanceof Morphs){
                   Bestiary.setSeen(mob.getClass());
                   ((Morphs) mob).EndStory = true;
                   ScrollOfTeleportation.appear(mob,312);
                   mob.yell(Messages.get(mob, "endtalk"));
               } else {
                   Buff.affect(mob, Bleeding.class).set(1000f);
               }
            }
            Bestiary.setSeen(getClass());
            destroy();
            sprite.killAndErase();
        }

        onZapComplete();

        TryGetSummonedMobs();

        return super.act();
    }


    public static int[] safePos = new int[] {
        301,587,323,37
    };


    public void onZapComplete(){
        for (Mob enemy : Dungeon.level.mobs.toArray(new Mob[0])) {
            if(enemy.isOldDay){
                for (Buff b : enemy.buffs(AllyBuff.class)){
                    if(b != null){
                        if (sprite.visible || enemy.sprite.visible) {
                            sprite.parent.add(new Beam.DeathRayS(sprite.center(), enemy.sprite.center()));
                        }
                        enemy.damage(10000,this,DamageType.REAL);
                        if (enemy.sprite.visible)
                            enemy.sprite.emitter().burst( Speck.factory( Speck.STAR ), 1 );
                    }
                }
            }
            next();
        }

    }

    public void TryGetSummonedMobs() {
        int spawnCount = 0;
        if(summonedmobsCount < 26){
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if(!mob.isOldDay){
                    if (buff(SummonColdDown.class) == null) {
                        if (summonedmobsCount < 5) {
                            spawnCount = 12;
                        } else if (summonedmobsCount < 10) {
                            spawnCount = 8;
                        } else if (summonedmobsCount < 15) {
                            spawnCount = 6;
                        } else if (summonedmobsCount < 20) {
                            spawnCount = 5;
                        } else {
                            spawnCount = 3;
                        }

                        for (int i = 0; i < spawnCount; i++) {
                            Mob testActor = getSummonTimeMobs();
                            testActor.pos = safePos[Random.Int(safePos.length)];

                            if(summonedmobsCount >= 15){
                                ChampionEnemy.rollForChampion(testActor);
                                ChampionEnemy.rollForStateLing(testActor);
                            } else if(summonedmobsCount >= 10){
                                if(Random.Float()>0.75f){
                                    ChampionEnemy.rollForChampion(testActor);
                                    ChampionEnemy.rollForStateLing(testActor);
                                } else {
                                    ChampionEnemy.rollForChampion(testActor);
                                }
                            } else {
                                if(Random.Float()>0.75f){
                                    ChampionEnemy.rollForChampion(testActor);
                                } else {
                                    ChampionEnemy.rollForStateLing(testActor);
                                }
                            }

                            testActor.isOldDay = true;
                            testActor.state = testActor.HUNTING;
                            GameScene.add(testActor);
                        }
                        Buff.affect(this, SummonColdDown.class, SummonColdDown.DURATION);
                        summonedmobsCount++;
                    }
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

    public static class RepaierDown extends FlavourBuff {


        {
            announced = true;
            type = buffType.POSITIVE;
        }


        public static final float DURATION	= 25f;
        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.ORAGNECOLOR);
        }
    }

    public static class SummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            skills = true;
        }

        public static final float DURATION	= 30f;

        public void detach() {
            super.detach();
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof MyCoreHeart) {
                    MyCoreHeart heart = (MyCoreHeart) mob;
                    heart.getHP = false;
                    heart.brokenCount = 0;
                    if(heart.summonedmobsCount % 5 == 0){
                        Statistics.RepaierTowerCount++;
                    }
                }
            }
        }


        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(Window.WATA_COLOR);
        }

        @Override
        public String desc() {
            int s = 0;
            int r = 0;
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof MyCoreHeart) {
                    s = ((MyCoreHeart) mob).summonedmobsCount;
                }
            }
            return Messages.get(this, "desc", dispTurns(visualcooldown()),s);
        }

    }

    private class Waiting extends Mob.Wandering{

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }


    private static final String STRING = "STRING";

    private static final String GETHP = "GETHP";

    private static final String BROKEN = "BROKEN";

    private static final String COUNS = "COUNS";

    private static final String ONE = "ONE";
    private static final String TWO = "TWO";
    private static final String THREE = "THREE";
    private static final String FOUR = "FOUR";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( STRING, summonedmobsCount );
        bundle.put(GETHP,getHP);
        bundle.put(BROKEN,brokenCount);
        bundle.put(COUNS,end);

        bundle.put(ONE,one);
        bundle.put(TWO,two);
        bundle.put(THREE,three);
        bundle.put(FOUR,four);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        summonedmobsCount = bundle.getInt( STRING );
        getHP = bundle.getBoolean(GETHP);
        brokenCount = bundle.getInt(BROKEN);
        end = bundle.getInt(COUNS);

        one = bundle.getBoolean(ONE);
        two = bundle.getBoolean(TWO);
        three = bundle.getBoolean(THREE);
        four = bundle.getBoolean(FOUR);

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {

        if(src != enemy){
            return;
        }

        if(enemy != null && enemy instanceof Mob){
            if(Dungeon.level.distance(pos,enemy.pos)<=1){
                dmg =  1;
                enemy.damage(HT,this);
                Buff.affect(enemy, Bleeding.class).set(1000f);
                brokenCount++;
            }
        } else {
            return;
        }
        BossHealthBar.assignBoss(this);

        super.damage(dmg, src, type);
    }

}
