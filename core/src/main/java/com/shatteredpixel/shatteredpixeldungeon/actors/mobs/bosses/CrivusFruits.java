package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.STRONGER_BOSSES;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.bossWeapons;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel2;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.crivusfruitslevel3;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.BRatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.ForestBossLasherTWOPos;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.RatKingRoom;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ForestBossLevel.WIDTH;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PhantomPiranha;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrivusFruitsFood;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CrivusFruitsFlake;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LifeTreeSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrivusFruitsSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

//克里弗斯之果 本体
public class CrivusFruits extends Boss {
    //the actual affected cells
    private HashSet<Integer> affectedCells;
    private static final int MIN_ABILITY_CD = 7;
    private int lastHeroPos;
    private static final int MAX_ABILITY_CD = 12;
    private ArrayList<Integer> targetedCells = new ArrayList<>();
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;


    @Override
    public int drRoll() {
        return Random.Int(2);
    }

    //基本属性
    {
        spriteClass = CrivusFruitsSprite.class;

        HP = HT = 120;
        defenseSkill = 4;

        EXP = 20;

        state = WANDERING;

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    private int phase = 1;

    private int summonsMade = 0;

    private float summonCooldown = 0;
    private float abilityCooldown = 3;
    private static final int MIN_COOLDOWN = 7;
    private static final int MAX_COOLDOWN = 11;

    private static float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f};
    private int wave=0;

    private int lastAbility = 0;
    private static final int NONE = 0;
    private static final int LINK = 1;
    private static final int TELE = 2;
    private static final int ENRAGE = 3;
    private static final int DEATHRATTLE = 4;
    private static final int SACRIFICE = 5;
    private static final int SUMMON = 6;

    private static final String PHASE = "phase";
    private static final String SUMMONS_MADE = "summons_made";

    private static final String SUMMON_CD = "summon_cd";
    private static final String ABILITY_CD = "ability_cd";
    private static final String LAST_ABILITY = "last_ability";

    private static final String TARGETED_CELLS = "targeted_cells";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( SUMMONS_MADE, summonsMade );
        bundle.put( SUMMON_CD, summonCooldown );
        bundle.put( ABILITY_CD, abilityCooldown );
        bundle.put( LAST_ABILITY, lastAbility );
        bundle.put("wavePhase2", wave);

        //暴力Boss
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt( PHASE );
        summonsMade = bundle.getInt( SUMMONS_MADE );
        summonCooldown = bundle.getFloat( SUMMON_CD );
        abilityCooldown = bundle.getFloat( ABILITY_CD );
        lastAbility = bundle.getInt( LAST_ABILITY );
        wave = bundle.getInt("wavePhase2");

        if (phase == 2) properties.add(Property.IMMOVABLE);

        //暴力Boss
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
    }

    //无敌也要扣减！
    public static class DiedDamager extends Buff {

        @Override
        public boolean act() {
            if (target.alignment != Alignment.ENEMY){
                detach();
            }
            spend( TICK );
            return true;
        }

        @Override
        public void detach() {
            super.detach();
            //遍历楼层生物，寻找CrivusFruits执行扣血，在触手死亡时强制扣除本体CrivusFruits
            for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
                if (m instanceof CrivusFruits){
                    m.damage(7, this);
                }
            }
        }
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return this.HP > 36 && effect != DiedDamager.class;
    }

    //对话框架
    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            GLog.n(Messages.get(this, "notice"));
            GameScene.flash(0x8000cc00);
            Camera.main.shake(1f,3f);
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    //回合
    @Override
    public void damage(int dmg, Object src) {
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg * 2);
        super.damage(dmg, src);
    }


    @Override
    protected boolean act() {

        alerted = false;
        super.act();

        if (alignment == Alignment.NEUTRAL){
            return true;
        }

        //毒雾扩散
        if(!crivusfruitslevel2){
            if(Dungeon.isChallenged(STRONGER_BOSSES)){
                GameScene.add(Blob.seed(pos, HP<65 ? 90 : 70, DiedBlobs.class));
            } else {
                GameScene.add(Blob.seed(pos, HP<65 ? 50 : 30, DiedBlobs.class));
            }
        } else {
            if(Dungeon.isChallenged(STRONGER_BOSSES)){
                GameScene.add(Blob.seed(pos, HP<36 ? 280 : 100, DiedBlobs.class));
            } else {
                GameScene.add(Blob.seed(pos, HP<36 ? 150 : 50, DiedBlobs.class));
            }
            //doYogLasers();
        }


        //判定是否第一次加进入游戏
        if( hero.buff(LockedFloor.class) != null){
            notice();
        }
        state = PASSIVE;

        //二阶段
        if( HP<65 && !crivusfruitslevel2){
            HP=HT=64;
            GLog.n(Messages.get(this,"anargy"));
            crivusfruitslevel2 = true;
            GameScene.flash(0x808c8c8c);
            //doYogLasers();
            for (int i : ForestBossLasherTWOPos) {
                CrivusFruitsLasher csp = new CrivusFruitsLasher();
                csp.pos = i;
                GameScene.add(csp);
                if(Dungeon.isChallenged(STRONGER_BOSSES)) {
                    Buff.affect(csp, CFBarrior.class).setShield(3 / csp.HT);
                }
            }

            if(Dungeon.isChallenged(STRONGER_BOSSES)){

                PhantomPiranha p = new PhantomPiranha();
                p.pos = 837;

                GameScene.add(p);

                PhantomPiranha x = new PhantomPiranha();
                x.pos = 890;
                GameScene.add(x);
            }

            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");

            //在二阶段就应该构建好房间
            //A号鼠王房间
            for (int i : RatKingRoom) {
                set( i, Terrain.WALL );
                GameScene.updateMap( i );
                set( WIDTH*11+6, Terrain.DOOR );
                GameScene.updateMap( WIDTH*11+6 );
            }
            //B号鼠王房间
            for (int i : BRatKingRoom) {
                set( i, Terrain.WALL );
                GameScene.updateMap( i );
                set( WIDTH*11+25, Terrain.DOOR );
                GameScene.updateMap( WIDTH*11+25 );
            }

        }

        //三阶段
        if(HP==36){
            alignment = Alignment.ENEMY;
            GameScene.flash(0x80009c9c);
            HP=HT=35;
            if(Dungeon.isChallenged(STRONGER_BOSSES)){
                Buff.affect(this, Barrier.class).setShield((int) (3f * this.HT + 10));
            }
            GLog.n(Messages.get(this,"died!!!"));
            GLog.w(Messages.get(this,"!!!"));
            Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
            this.sprite.showStatus(CharSprite.NEGATIVE, "!!!");
        }

        return super.act();
    }

    //红雾弥漫
    public static class DiedBlobs extends Blob implements Hero.Doom {

        //Boss死亡后改变描述
        @Override
        public String tileDesc() {
            return Messages.get(this, hero.buff(LockedFloor.class) != null? "desc" : "csed" );
        }
        @Override
        protected void evolve() {
            super.evolve();

            int damage = 6;

            Char ch;
            int cell;

            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0 && (ch = Actor.findChar( cell )) != null) {
                        if (!ch.isImmune(this.getClass())) {
                            if( hero.buff(LockedFloor.class) != null ) {
                                ch.damage(hero.buff(LockedFloor.class) != null ? damage : 0, this);
                                Statistics.bossScores[0] -= 200;
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );

            emitter.pour( Speck.factory( Speck.DIED ), 0.4f );
        }

        @Override
        public void onDeath() {
            //
        }
    }

    @Override
    public void beckon(int cell) {
        //do nothing
    }

    @Override
    public void die(Object cause) {
        super.die(cause);

        
        

        PotionOfPurity.PotionOfPurityLing potionOfPurityLing = Dungeon.hero.belongings.getItem(PotionOfPurity.PotionOfPurityLing.class);
        if(potionOfPurityLing != null){
            potionOfPurityLing.detachAll( hero.belongings.backpack );
        }

        //共存
        if(Statistics.bossRushMode){
            //克里弗斯之果二阶段死亡的时候的给予重新评估
            if(crivusfruitslevel2){
                crivusfruitslevel2 = false;
            }
            if(crivusfruitslevel3){
                crivusfruitslevel3 = false;
            }
        }

        Dungeon.level.unseal();
        GameScene.bossSlain();
        GLog.n(Messages.get(this,"dead"));
        Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos-2 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos-1 ).sprite.drop();
        Dungeon.level.drop( new IronKey( Dungeon.depth ), pos+1 ).sprite.drop();
        Badges.validateBossSlain();
        Statistics.bossScores[0] += 1000;

        if (!Badges.isUnlocked(Badges.Badge.KILL_APPLE)){
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
        } else if (Random.Float()<0.4f || Statistics.bossWeapons>=3) {
            bossWeapons++;
            Dungeon.level.drop( new LifeTreeSword(), pos ).sprite.drop();
            if(Statistics.bossWeapons>=3){
                Statistics.bossWeapons=0;
                GLog.w(Messages.get(this,"weapon"));
            }
        } else {
            Dungeon.level.drop( new Food(), pos ).sprite.drop();
        }

        Badges.KILLSAPPLE();

        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(3)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFood(), pos + ofs ).sprite.drop( pos );
        }

        int flakes = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < flakes; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(4)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new CrivusFruitsFlake(), pos + ofs ).sprite.drop( pos );
        }

      if(Statistics.bossRushMode){
            GetBossLoot();
        }
    }


    {
        immunities.add( Paralysis.class );
        immunities.add( Amok.class );
        immunities.add( Sleep.class );
        immunities.add( ToxicGas.class );
        immunities.add( DiedBlobs.class );
        immunities.add( Terror.class );
        immunities.add( Dread.class );
        immunities.add( Vertigo.class );
    }

    public void doForestLasers(){
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();
        //delay fire on a rooted hero
        if(enemy != null) {
            if (!enemy.rooted) {
                for (int i : targetedCells) {
                    Ballistica b = new Ballistica(i, lastHeroPos, Ballistica.WONT_STOP);
                    //shoot beams
                    sprite.parent.add(new Beam.DeathRayS(DungeonTilemap.raisedTileCenterToWorld(i),
                            DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
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
                    ch.damage(Random.NormalIntRange(8, 12),this);

                    if (Dungeon.level.heroFOV[pos]) {
                        ch.sprite.flash();
                        CellEmitter.center(pos).burst(Speck.factory(Speck.COIN), Random.IntRange(2, 3));
                    }
                    if (!ch.isAlive() && ch == Dungeon.hero) {
                        Dungeon.fail(getClass());
                        GLog.n(Messages.get(Char.class, "kill", name()));
                    }
                }
                targetedCells.clear();
            }
        }
        if(enemy != null) {
            if (abilityCooldown <= 0 && HP < HT * 0.8f) {
                lastHeroPos = enemy.pos;

                int beams = (int) (4 + (HP * 1.0f / HT) * 4);
                for (int i = 0; i < beams; i++) {
                    int randompos = Random.Int(Dungeon.level.width()) + Dungeon.level.width() * 2;
                    targetedCells.add(randompos);
                }

                for (int i : targetedCells) {
                    Ballistica b = new Ballistica(i, enemy.pos, Ballistica.WONT_STOP);

                    for (int p : b.path) {
                        Game.scene().addToFront(new TargetedCell(p, Window.DeepPK_COLOR));
                    }
                }

                spend(TICK * 1.5f);
                Dungeon.hero.interrupt();
                abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD - 2 * (1 - (HP * 1f / HT)),
                        MAX_ABILITY_CD - 5 * (1 - (HP * 1f / HT)));
            } else {
                spend(TICK);
            }
        }
        if (abilityCooldown > 0) abilityCooldown-= 5;
    }

    public static class CFBarrior extends Barrier {

        @Override
        public boolean act() {
            incShield();
            return super.act();
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }


}