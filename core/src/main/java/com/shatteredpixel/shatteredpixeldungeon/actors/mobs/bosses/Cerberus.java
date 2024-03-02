package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.CerDogBossLevel.FireWallDied;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.BGMPlayer;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Web;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Typhon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WoolParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CerberusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;


public class Cerberus extends Boss {

    {
        initProperty();
        initBaseStatus(20, 40, 30, 12, 800, 14, 9);
        initStatus(100);

        spriteClass = CerberusSprite.class;

        baseSpeed = 1.2f;

        HUNTING = new Hunting();

        immunities.add(Burning.class);
        immunities.add(ToxicGas.class);
        immunities.add(CorrosiveGas.class);
        immunities.add(Vertigo.class);
    }

    @Override
    public float speed() {
        if(hero.speed() >= 2f){
            return hero.speed();
        }
        return super.speed();
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );
        Dungeon.level.unseal();

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Typhon) {
                ((Typhon) mob).rd = false;
                ((Typhon) mob).secnod = false;
            }
        }

        Badges.KILL_DOG();
        GameScene.bossSlain();
        yell( Messages.get(this, "defeated",hero.name()) );
    }

    @Override
    public int attackSkill( Char target ) {
        return 26;
    }

    @Override
    public int damageRoll() {

        //2
        if (HP < 600 && HP> 300) {
            return Random.NormalIntRange( 28, 60 );
        //3
        } else if (HP<300){
            return Random.NormalIntRange( 40, 70 );
        //1
        } else {
            return Random.NormalIntRange( 18, 40 );
        }

    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(9, 18);
    }

    private Ballistica beam;
    private int beamTarget = -1;
    private int beamCooldown;
    public boolean beamCharged;
    private int soulDiedCount = 30;

    private int firewallCount = 50;

    private int tryToCount = 30;

    private int targetingPos = -1;

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private boolean ref=true;

    protected boolean hasRaged = false;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";
    private static final String REF = "ref";

    private static final String HAS_RAGED = "has_raged";

    @Override
    protected boolean canAttack( Char enemy ) {

        if (beamCooldown == 0) {
            Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID);

            if (enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)){
                beam = aim;
                beamTarget = aim.collisionPos;
                return true;
            } else
                //if the beam is charged, it has to attack, will aim at previous location of target.
                return beamCharged;
        } else
            return super.canAttack(enemy);
    }

    @Override
    protected boolean act() {
        if (beamCharged && state != HUNTING){
            beamCharged = false;
            sprite.idle();
        }

        if (beam == null && beamTarget != -1) {
            beam = new Ballistica(pos, beamTarget, Ballistica.MAGIC_BOLT);
            ((CerberusSprite)sprite).Skills( enemy.pos );
        }

        if (targetingPos != -1 && soulDiedCount > 2 ) {
            if (sprite != null && (sprite.visible || Dungeon.level.heroFOV[targetingPos])) {
                sprite.zap(targetingPos);
                return false;
            } else {

                soulDied();
                return true;
            }
        }

        //二阶段：火墙困境
        if( HP<600 && firewallCount >= 25){

            if(first){
                immunities.add(Freezing.class);
                immunities.add(Terror.class);
                immunities.add(HalomethaneBurning.class);
                Buff.detach( this, Frost.class );
                Buff.detach( this, HalomethaneBurning.class );
                Buff.detach( this, Terror.class );
                first = false;
            }


            for (int i : FireWallDied) {
                GameScene.add(Blob.seed(i, 16, NotBrokenWall.class));
            }

            if(secnod){
                yell(Messages.get(Cerberus.class,"firewall"));
                Integer[] coords = {291, 297, 421, 415};
                Random.shuffle(Arrays.asList(coords));
                for (int coord : coords) {
                    if(enemy != null){
                        ScrollOfTeleportation.appear(enemy, coord);
                    } else {
                        ScrollOfTeleportation.appear(hero, coord);
                    }
                }
                secnod = false;
            }

            firewallCount = 0;
        } else {
            firewallCount++;

            if(firewallCount == 20){
                for (int i : FireWallDied) {
                     Game.scene().addToFront(new TargetedCell(i, Window.DeepPK_COLOR));
                }

                if(HP < 300 ) {
                    int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

                    for (int i = 0; i < evaporatedTiles; i++) {
                        int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                        if (Dungeon.level.map[cell] == Terrain.EMPTY){
                            Level.set( cell, Terrain.FURROWED_GRASS);
                            GameScene.updateMap( cell );
                            CellEmitter.get( cell ).burst( Speck.factory( Speck.WOOL ), 10 );
                        }
                    }
                }
                GLog.n(Messages.get(this,"firewall_last5"));
            }
        }

        //三阶段 最终审判
        if(HP<300 && rd && HP>0){
            immunities.add(FrostBurning.class);
            immunities.add(MagicalSleep.class);
            immunities.add(Paralysis.class);
            DogGuard dogGuard = new DogGuard();
            dogGuard.state = dogGuard.WANDERING;
            dogGuard.pos = 356;



            Class<?extends ChampionEnemy> buffCls0;
            switch (Random.Int(2)){
                case 0: default:    buffCls0 = ChampionEnemy.Small.class;      break;
                case 1:             buffCls0 = ChampionEnemy.Middle.class;   break;
            }

            Buff.affect( dogGuard, buffCls0);

            Class<?extends ChampionEnemy> buffCls1;
            switch (Random.Int(2)){
                case 0: default:             buffCls1 = ChampionEnemy.Big.class;    break;
                case 1:             buffCls1 = ChampionEnemy.LongSider.class;        break;
            }

            Buff.affect( dogGuard, buffCls1);


            Class<?extends ChampionEnemy> buffCls2;
            switch (Random.Int(8)){
                case 0: default:    buffCls2 = ChampionEnemy.Blazing.class;      break;
                case 1:             buffCls2 = ChampionEnemy.Projecting.class;   break;
                case 2:             buffCls2 = ChampionEnemy.AntiMagic.class;    break;
                case 3:             buffCls2 = ChampionEnemy.Giant.class;        break;
                case 4:             buffCls2 = ChampionEnemy.Blessed.class;      break;
                case 5:             buffCls2 = ChampionEnemy.Growing.class;      break;
                case 6:             buffCls2 = ChampionEnemy.Halo.class;      	  break;
                case 7:             buffCls2 = ChampionEnemy.DelayMob.class;     break;
            }
            Buff.affect( dogGuard, buffCls2);


            GameScene.add(dogGuard);
            rd = false;
            yell(Messages.get(this,"your_must_died"));
        }

        return super.act();
    }

    public int attackProc(Char enemy, int damage) {
       damage = super.attackProc(enemy, damage);
        if (beamCooldown > 0)
            beamCooldown--;

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if ( mob instanceof DeathRong) {
                if(!((DeathRong) mob).rd){
                    for (Buff buff : hero.buffs()) {
                        if (buff instanceof ScaryBuff) {
                            ((ScaryBuff) buff).damgeScary( Random.NormalIntRange(9,12));
                        } else {
                            Buff.affect( enemy, ScaryBuff.class ).set( (100), 1 );
                        }
                    }
                }
            }
        }

        if(soulDiedCount>2){
            for (Buff buff : hero.buffs()) {
                if (buff instanceof ScaryBuff) {
                    ((ScaryBuff) buff).damgeScary( Random.NormalIntRange(10,20));
                } else {
                    Buff.affect( enemy, ScaryBuff.class ).set( (100), 1 );
                }
            }
        }

        return damage;
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        super.doAttack( enemy );

            if(soulDiedCount>2 && enemy == hero) {

                //set up an attack for next turn
                ArrayList<Integer> candidates = new ArrayList<>();
                for (int i : PathFinder.CROSS) {
                    int target = enemy.pos + i;
                    if (target != pos && new Ballistica(pos, target, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET).collisionPos == target) {
                        candidates.add(target);
                    }
                }

                if (!candidates.isEmpty()) {
                    targetingPos = Random.element(candidates);

                    for (int i : PathFinder.CROSS) {
                        if (!Dungeon.level.solid[targetingPos + i]) {
                            sprite.parent.addToBack(new TargetedCell(targetingPos + i, 0xFF0000));
                        }
                    }

                    yell(Messages.get(this, "charging"));
                    spend(GameMath.gate(TICK, (int) Math.ceil(hero.cooldown()), 3 * TICK));
                    hero.interrupt();
                }
            }

            if (beamCooldown > 0) {
                return super.doAttack(enemy);
            } else if (!beamCharged) {


                spend(1f);

                yell(Messages.get(this, "dreamdied"));
                Integer[] coords = {354, 358, 418, 294};
                Random.shuffle(Arrays.asList(coords));
                for (int coord : coords) {
                    ScrollOfTeleportation.appear(this, coord);
                }

                if (soulDiedCount < 4)
                    soulDiedCount++;

                beamCharged = true;
                return true;
            } else {
                spend(attackDelay());

                beam = new Ballistica(pos, beamTarget, Ballistica.PROJECTILE);

                deathDream();
                return true;
            }
    }

    protected void soulDied() {
        if (targetingPos != -1) {
            spend(2f);

            Invisibility.dispel(this);

            for (int i : PathFinder.CROSS) {
                if (!Dungeon.level.solid[targetingPos + i]) {
                    CellEmitter.get(targetingPos + i).burst(MagicFlameParticle.FACTORY, 5);
                    GameScene.add(Blob.seed(targetingPos + i, 8, SoulDiedBlobs.class));
                    Sample.INSTANCE.play(Assets.Sounds.ZAP);
                }
            }

        }

        targetingPos = -1;
        soulDiedCount = 0;
    }

    public void onZapComplete() {
        soulDied();
        next();
    }

    public void deathDream(){
        if (!beamCharged || beamCooldown > 0 || beam == null)
            return;

        beamCharged = false;
        beamCooldown = 2;

        for (int pos : beam.subPath(1, beam.dist)) {
            Game.scene().addToFront(new TargetedCell(pos, Window.RED_COLOR));
            GameScene.add(Blob.seed(pos, 2, WebBlob.class));
        }

        beam = null;
        beamTarget = -1;
    }

    public static class SoulDiedBlobs extends Blob {

        @Override
        protected void evolve() {

            int cell;

            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {
                        burn( cell );
                    }

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if(mob instanceof Cerberus){
                            if(mob.HP<600){
                                if(Random.Int(100) <= 40 && mob.buff(CrivusFruits.CFBarrior.class) == null) {
                                    Buff.affect(mob, CrivusFruits.CFBarrior.class).setShield(40);
                                }
                                Level.set(cell, Terrain.FURROWED_GRASS);
                                if(Random.Int(100) <= 40){
                                    GameScene.add(Blob.seed(cell, 16, NotBrokenWall.class));
                                    GameScene.add(Blob.seed(cell, 16, HalomethaneFire.class));
                                }
                            }
                        }
                    }


                    CellEmitter.get(cell).start(WoolParticle.FACTORY, 0.09f, 40);
                }
            }
        }

        public static void burn( int pos ) {
            Char ch = Actor.findChar( pos );



            if (ch != null && !ch.isImmune(NotBrokenWall.class)) {

                Buff.affect( ch, ScaryBuff.class ).set( (100), 1 );

                for (Buff buff : hero.buffs()) {
                    if (buff instanceof ScaryBuff) {
                        ((ScaryBuff) buff).damgeScary( Random.NormalIntRange(5,15));
                    }
                }
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.pour(  LeafParticle.GENERAL, 0.02f );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }
    }


    public static class WebBlob extends Blob {

        {
            actPriority = BUFF_PRIO - 1;
            alwaysVisible = true;
        }

        @Override
        protected void evolve() {

            int cell;
            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j* Dungeon.level.width();
                    off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

                    if (off[cell] > 0) {
                        volume += off[cell];
                    }

                    if (cur[cell] > 0 && off[cell] == 0) {

                        Char ch = Actor.findChar( cell );

                        if (ch instanceof Cerberus) {
                            if(Random.Int(100) <= 40 && ch.buff(CrivusFruits.CFBarrior.class) == null) {
                                Buff.affect(ch, CrivusFruits.CFBarrior.class).setShield(40);
                            }
                        }

                        if (ch != null && !ch.isImmune(Web.class) && !(ch instanceof Cerberus || ch instanceof DogGuard)) {
                            Buff.affect( ch, Roots.class, 3f );
                            ch.damage(Random.NormalIntRange(28, 62), ch.damageRoll());
                        }
                        Level.set( cell, Terrain.FURROWED_GRASS );
                        CellEmitter.get(cell).start(SparkParticle.STATIC, 0.09f, 80);
                    }
                }
            }
        }

        @Override
        public void use(BlobEmitter emitter) {
            super.use(emitter);

            emitter.pour( ElmoParticle.FACTORY, 0.02f );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }
    }

    private static final String BEAM_TARGET     = "beamTarget";
    private static final String BEAM_COOLDOWN   = "beamCooldown";
    private static final String BEAM_CHARGED    = "beamCharged";
    private static final String SOUL_DIEDCOUNT    = "soulDiedCount";

    private static final String FIRE_WALLCOUNT    = "fireWallCount";

    private static final String TRY_CRY_COUNT    = "try_cry_count";

    private static final String TARGETING_POS = "targeting_pos";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( BEAM_TARGET, beamTarget);
        bundle.put( BEAM_COOLDOWN, beamCooldown );
        bundle.put( BEAM_CHARGED, beamCharged );
        bundle.put( SOUL_DIEDCOUNT, soulDiedCount );
        bundle.put(TARGETING_POS, targetingPos);
        bundle.put(FIRE_WALLCOUNT,firewallCount);
        bundle.put(TRY_CRY_COUNT,tryToCount);

        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
        bundle.put(REF, ref);
        bundle.put(HAS_RAGED, hasRaged);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        if(HP<600){
            immunities.add(Freezing.class);
            immunities.add(Terror.class);
            immunities.add(HalomethaneBurning.class);
        }

        if(HP<300){
            immunities.add(FrostBurning.class);
            immunities.add(MagicalSleep.class);
            immunities.add(Paralysis.class);
        }

        if (bundle.contains(BEAM_TARGET))
            beamTarget = bundle.getInt(BEAM_TARGET);

        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamCharged = bundle.getBoolean(BEAM_CHARGED);
        soulDiedCount = bundle.getInt(SOUL_DIEDCOUNT);
        targetingPos = bundle.getInt(TARGETING_POS);
        firewallCount = bundle.getInt(FIRE_WALLCOUNT);
        tryToCount = bundle.getInt(TRY_CRY_COUNT);

        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
        ref = bundle.getBoolean(REF);
        hasRaged = bundle.getBoolean(HAS_RAGED);
    }

    @Override
    public void damage(int dmg, Object src) {

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 3;
            lock.addTime(dmg*multiple);
        }

        if (HP <= 300){
            dmg = Math.min(dmg, 20);
        } else {
            dmg = Math.min(dmg, 40);
        }

        super.damage(dmg, src);
    }

    @Override
    public void notice() {
        //super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            GameScene.flash(0x33ff0000);
            BGMPlayer.playBoss();
            GameScene.bossReady();
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public synchronized boolean isAlive() {
        if (super.isAlive()){
            return true;
        } else {
            if (!hasRaged){
                triggerEnrage();
            }
            return !buffs(Rage.class).isEmpty();
        }
    }

    protected void triggerEnrage(){
        Buff.affect(this, Rage.class).setShield(1000);

        if(!Statistics.happyMode){
            Typhon typhon = new Typhon();
            typhon.pos = 356;
            GameScene.add(typhon);
        }


        ref = false;
        spend( TICK );
        hasRaged = true;
    }


    public static class Rage extends ShieldBuff {

        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean act() {

            if (target.HP > 0){
                detach();
                return true;
            }

            absorbDamage( 2);

            target.baseSpeed = 2f;

            if (shielding() <= 0){
                target.die(null);
            }

            spend( TICK );

            return true;
        }

        @Override
        public int icon () {
            return BuffIndicator.FURY;
        }

        @Override
        public String toString () {
            return Messages.get(this, "name");
        }

        @Override
        public String desc () {
            return Messages.get(this, "desc", shielding());
        }

        {
            immunities.add(Terror.class);
        }
    }

    //恶灵之墙
    public static class NotBrokenWall extends Blob {

        @Override
        protected void evolve() {

            //boolean[] flamable = Dungeon.level.flamable;
            int cell;
            int fire = 0;

            Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );
            //燃烧效果粒子总和
            Level l = Dungeon.level;

            boolean[] flamable = Dungeon.level.flamable;
            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {

                        if (l.water[cell]){
                            cur[cell] = 0;
                        }

                        if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
                            freeze.clear(cell);
                            off[cell] = cur[cell] = 0;
                            continue;
                        }

                        burn( cell );

                        fire = cur[cell] - 1;


                    } else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0) {

                        if (flamable[cell]
                                && (cur[cell-1] > 0
                                || cur[cell+1] > 0
                                || cur[cell-Dungeon.level.width()] > 0
                                || cur[cell+Dungeon.level.width()] > 0)) {
                            fire = 4;
                            burn( cell );
                            area.union(i, j);
                        } else {
                            fire = 0;
                        }

                    } else {
                        fire = 0;
                    }

                    volume += (off[cell] = fire);
                }
            }

            Dungeon.observe();

        }




        public static void burn( int pos ) {
            Char ch = Actor.findChar( pos );
            if (ch != null && !ch.isImmune(NotBrokenWall.class) && !(ch instanceof Cerberus || ch instanceof DogGuard)) {

                Integer[] coords = {291, 297, 421, 415};
                Random.shuffle(Arrays.asList(coords));

                Buff.affect( ch, Burning.class ).reignite( ch );

                for (int coord : coords) {
                    ch.sprite.jump(ch.pos, coord, new Callback() {
                        @Override
                        public void call() {
                            ScrollOfTeleportation.appear(ch, coord);
                            Dungeon.observe();
                        }
                    });
                }

            }

            Heap heap = Dungeon.level.heaps.get( pos );
            if (heap != null) {
                heap.burn();
            }

            Plant plant = Dungeon.level.plants.get( pos );
            if (plant != null){
                plant.wither();
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use( emitter );
            emitter.pour( MagicFlameParticle.FACTORY, 0.03f );
        }

        @Override
        public String tileDesc() {
            return Messages.get(this, "desc");
        }
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            //even if enemy isn't seen, attack them if the beam is charged
            if (beamCharged && enemy != null && canAttack(enemy)) {
                enemySeen = enemyInFOV;
                return doAttack(enemy);
            }
            return super.act(enemyInFOV, justAlerted);
        }
    }

    public static class DogGuard extends Mob {

        //they can only use their chains once
        private boolean chainsUsed = false;

        private int canColl = 45;

        {
            spriteClass = DogGuardSprite.class;

            HP = HT = 240;

            defenseSkill = 10;

            properties.add(Property.BOSS);

            WANDERING = new Wandering();

            HUNTING = new Hunting();

            immunities.add(Burning.class);
            immunities.add(ToxicGas.class);
            immunities.add(CorrosiveGas.class);
            immunities.add(Vertigo.class);
            immunities.add(Freezing.class);
            immunities.add(Terror.class);
            immunities.add(FrostBurning.class);
            immunities.add(MagicalSleep.class);
            immunities.add(Paralysis.class);
            immunities.add(HalomethaneBurning.class);

        }

        @Override
        protected boolean act() {

            if(canColl < 35){
                canColl++;
            }

            return super.act();
        }

        private class Wandering extends Mob.Wandering {

            @Override
            public boolean act( boolean enemyInFOV, boolean justAlerted ) {
                if ( enemyInFOV ) {

                    enemySeen = true;

                    notice();
                    alerted = true;
                    state = HUNTING;


                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof Cerberus) {
                            target = mob.pos;
                        }
                    }



                } else {

                    enemySeen = false;

                    int oldPos = pos;

                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob instanceof Cerberus) {
                            target = mob.pos;
                        }
                    }

                    //always move towards the hero when wandering
                    if (getCloser( target )) {
                        spend( 1 / speed() );
                        return moveSprite( oldPos, pos );
                    } else {
                        spend( TICK );
                    }
                }

                return true;
            }

        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(24, 42);
        }

        private boolean chain(int target) {
            if (chainsUsed || enemy.properties().contains(Property.IMMOVABLE))
                return false;

            Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

            if (chain.collisionPos != enemy.pos
                    || chain.path.size() < 2
                    || Dungeon.level.pit[chain.path.get(1)])
                return false;
            else {
                int newPos = -1;
                for (int i : chain.subPath(1, chain.dist)) {
                    if (!Dungeon.level.solid[i] && Actor.findChar(i) == null) {
                        newPos = i;
                        break;
                    }
                }

                if (newPos == -1) {
                    return false;
                } else {
                    final int newPosFinal = newPos;
                    this.target = newPos;

                    if (sprite.visible || enemy.sprite.visible) {
                        yell(Messages.get(this, "scorpion"));
                        new Item().throwSound();
                        Sample.INSTANCE.play(Assets.Sounds.CHAINS);
                        sprite.parent.add(new Chains(sprite.center(),
                                enemy.sprite.destinationCenter(),
                                Effects.Type.RED_CHAIN,
                                new Callback() {
                                    public void call() {
                                        canColl = 0;
                                        Actor.add(new Pushing(enemy, enemy.pos, newPosFinal, new Callback() {
                                            public void call() {
                                                pullEnemy(enemy, newPosFinal);
                                            }
                                        }));
                                        next();
                                    }
                                }));
                    } else {
                        pullEnemy(enemy, newPos);
                    }
                }
            }

            //spend(35f);

            return true;
        }

        private void pullEnemy(Char enemy, int pullPos) {
            enemy.pos = pullPos;
            enemy.sprite.place(pullPos);
            Dungeon.level.occupyCell(enemy);
            Cripple.prolong(enemy, Cripple.class, 4f);
            if (enemy == Dungeon.hero) {
                Dungeon.hero.interrupt();
                Dungeon.observe();
                GameScene.updateFog();
            }
        }

        @Override
        public int attackSkill(Char target) {
            return 12;
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(0, 7);
        }

        @Override
        public float lootChance() {
            //each drop makes future drops 1/2 as likely
            // so loot chance looks like: 1/5, 1/10, 1/20, 1/40, etc.
            return super.lootChance() * (float) Math.pow(1 / 2f, Dungeon.LimitedDrops.GUARD_ARM.count);
        }

        @Override
        public Item createLoot() {
            Dungeon.LimitedDrops.GUARD_ARM.count++;
            return super.createLoot();
        }

        private final String CHAINSUSED = "chainsused";

        private final String CANROLL = "cantroll";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(CHAINSUSED, chainsUsed);
            bundle.put(CANROLL,canColl);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            chainsUsed = bundle.getBoolean(CHAINSUSED);
            canColl = bundle.getInt(CANROLL);
        }

        private class Hunting extends Mob.Hunting {
            @Override
            public boolean act(boolean enemyInFOV, boolean justAlerted) {
                enemySeen = enemyInFOV;

                if( canColl>14){
                    if (!chainsUsed
                            && enemyInFOV
                            && !isCharmedBy(enemy)
                            && !canAttack(enemy)
                            && Dungeon.level.distance(pos, enemy.pos) < 5
                            && chain(enemy.pos)) {
                        return !(sprite.visible || enemy.sprite.visible);
                    } else {
                        return super.act(enemyInFOV, justAlerted);
                    }
                } else
                    return super.act(enemyInFOV, justAlerted);

            }
        }
    }

    public static class DogGuardSprite extends MobSprite {

        @Override
        public void update() {
            super.update();

            if (flashTime <= 0){
                float interval = (Game.timeTotal % 9 ) /3f;
                tint(interval > 2 ? interval - 2 : Math.max(0, 1 - interval),
                        interval > 1 ? Math.max(0, 2-interval): interval,
                        interval > 2 ? Math.max(0, 3-interval): interval-1, 0.5f);
            }
        }

        public DogGuardSprite() {
            super();

            texture( Assets.Sprites.GUARD );

            TextureFilm frames = new TextureFilm( texture, 12, 16 );

            idle = new Animation( 2, true );
            idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

            run = new MovieClip.Animation( 15, true );
            run.frames( frames, 2, 3, 4, 5, 6, 7 );

            attack = new MovieClip.Animation( 12, false );
            attack.frames( frames, 8, 9, 10 );

            die = new MovieClip.Animation( 8, false );
            die.frames( frames, 11, 12, 13, 14 );

            play( idle );
        }

        @Override
        public void play( Animation anim ) {
            if (anim == die) {
                emitter().burst( ShadowParticle.UP, 4 );
            }
            super.play( anim );
        }
    }

}