package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;


import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.FALSEPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.TRUEPosition;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeamTowerAdbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HellBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShopLimitLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDICLRPRO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SmallLeafHardDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class FireMagicDied extends Boss implements Callback, Hero.Doom {

    private static final float TIME_TO_ZAP = 4f;

    {
        HP = HT = Statistics.bossRushMode && !Statistics.amuletObtained ? 270 * (Dungeon.depth/5) : (Statistics.amuletObtained || Statistics.RandMode) ? 2024 : 270 * (Statistics.deepestFloor/5);
        EXP = 80;
        defenseSkill = 4 + (5*Dungeon.depth/5);
        spriteClass = FireMagicGirlSprite.class;
        flying = true;
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Terror.class);
        immunities.add(HellBurning.class);

        if(Statistics.bossRushMode){
            immunities.add(Burning.class);
            immunities.add(Vertigo.class);
            immunities.add(Corrosion.class);
            immunities.add(Chill.class);
        }
    }



    private int pumpedUp = 0;

    public boolean allDead = false;

    @Override
    public int damageRoll() {
        int min = 1;
        int max = (HP*2 <= HT) ? 18+Dungeon.depth : 22+Dungeon.depth;
        if (pumpedUp > 0) {
            pumpedUp = 0;
            return Random.NormalIntRange( min*3, max*3 );
        } else {
            return Random.NormalIntRange( min, max );
        }
    }

    @Override
    public int attackSkill( Char target ) {
        int attack = 10;
        if (HP*2 <= HT) attack = 15;
        if (pumpedUp > 0) attack *= 2;
        return attack;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return (int)(super.defenseSkill(enemy) * ((HP*2 <= HT)? 1.5 : 1));
    }

    @Override
    public int drRoll() {
        return 7;
    }
    private int phase = 1;
    int preHP = HP;
    private float summonCooldown = 0;
    private float abilityCooldown = 6;
    private final ArrayList<Integer> targetedCells = new ArrayList<>();


    @Override
    public float speed() {
        if(allDead){
            return 2f;
        }
        return super.speed();
    }

    public static void Storm(Char ch){
        Ballistica aim;
        aim = new Ballistica(ch.pos, ch.pos - 1, Ballistica.STOP_TARGET);
        int projectileProps = Ballistica.IGNORE_SOFT_SOLID;
        int aoeSize = 6;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);
        GameScene.flash(0x00dd00);
        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FROST,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
    }

    @Override
    public boolean act() {

        if(allDead){
            immunities.add(Burning.class);
            immunities.add(HalomethaneBurning.class);
            immunities.add(FrostBurning.class);
        }

        if (phase == 1) {
            int dmgTaken = preHP - HP;
            abilityCooldown -= dmgTaken/8f;
            summonCooldown -= dmgTaken/8f;
            if (HP <= HT/2) {
                for (int i : CryStalPosition) {
                    Buff.append(hero, BeamTowerAdbility.class).towerPos = i;
                }
                sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
                Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
                phase = 2;
                Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
                int w = Dungeon.level.width();
                int dx = enemy.pos % w - pos % w;
                int dy = enemy.pos / w - pos / w;
                int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
                direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
                Buff.affect(this, FireMagicDied.YogScanHalf.class).setPos(pos, direction);
                sprite.showStatus(0xff0000, Messages.get(this, "dead"));

                if(Statistics.attackIFGirl) {
                    MagicGirlDead boss = new MagicGirlDead();
                    boss.state = boss.WANDERING;
                    boss.pos = 547;
                    boss.summonCD = 1f;
                    BossHealthBar.assignBoss(boss);
                    GameScene.add(boss);
                    Storm(boss);
                    GLog.b(Messages.get(this,"wakeup"));
                    yell(Messages.get(this,"sister",hero.name()));
                }

                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
                Buff.affect(this, DwarfMaster.DKBarrior.class).setShield(HT/2);
                HP = HT/2;
            }
        } else if (phase == 2 && shielding() == 0 && HP <= HT/3) {
            yell(  Messages.get(this, "enraged" ));
            ScrollOfTeleportation.teleportToLocation(this, ShopBossLevel.throneling);
            GLog.pink(  Messages.get(this, "xslx") );
            for (int i : CryStalPosition2) {
                Buff.append(hero, BeamTowerAdbility.class).towerPos = i;
                CrystalDiedTower csp = new CrystalDiedTower();
                csp.pos = i;
                GameScene.add(csp);
            }

            HP = HT/2;
            //T3 阶段
            CrystalLingTower abc = new CrystalLingTower();
            abc.pos = TRUEPosition;
            GameScene.add(abc);

            this.pos = FALSEPosition;

            Buff.affect(this, DwarfMaster.DKBarrior.class).setShield(HT/3);

            if(Statistics.amuletObtained|| Statistics.RandMode){
                Buff.append(hero, BeamTowerAdbility.class).towerPos = TRUEPosition;
            }
            Buff.append(hero, BeamTowerAdbility.class).towerPos = TRUEPosition;

            for (Buff buff : hero.buffs()) {
                if (buff instanceof FireMagicDied.KingDamager) {
                    buff.detach();
                }
            }
            //actScanning();
            phase = 3;
            sprite.idle();
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, FireMagicDied.YogScanHalf.class).setPos(pos, direction);
            sprite.showStatus(0xff0000, Messages.get(this, "dead"));
            Buff.affect(this, ChampionEnemy.Halo.class);
            Buff.affect(this, Adrenaline.class, 50f);
            Buff.affect(this,  Invulnerability.class, 20f);
        } else if (phase == 3 && preHP > 10 && HP <= 20){
            yell( Messages.get(this, "losing") );
            die(Dungeon.hero);
            Dungeon.hero.interrupt();
            GameScene.flash(0x80FFFFFF);
        }
        return super.act();
    }
    private static final String PHASE = "phase";
    private static final String ABILITY_CD = "ability_cd";
    private static final String SUMMON_CD = "summon_cd";
    private static final String TARGETED_CELLS = "targeted_cells";

    private static final String ALL_DEAD = "all_dead";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PHASE, phase);

        bundle.put(ABILITY_CD, abilityCooldown);
        bundle.put(SUMMON_CD, summonCooldown);

        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);

        bundle.put(ALL_DEAD,allDead);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt(PHASE);
        if (phase != 0) BossHealthBar.assignBoss(this);

        abilityCooldown = bundle.getFloat(ABILITY_CD);
        summonCooldown = bundle.getFloat(SUMMON_CD);

        for (int i : bundle.getIntArray(TARGETED_CELLS)){
            targetedCells.add(i);
        }

        allDead = bundle.getBoolean(ALL_DEAD);
    }


    @Override
    public void damage(int dmg, Object src, DamageType type) {
        super.damage(dmg, src, type);
        BossHealthBar.assignBoss(this);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
            else                                                    lock.addTime(dmg*1.5f);
        }
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (pumpedUp > 0) {
            return Dungeon.level.distance(enemy.pos, pos) <= 2
                    && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica(enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else if (HP < HT / 2) {
            return Dungeon.level.distance(enemy.pos, pos) <= 3
                    && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
                    && new Ballistica(enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
        } else {
            return super.canAttack(enemy);
        }
    }

    public void bolt(Integer target, final Char mob){
        if (target != null) {

            final Ballistica shot = new Ballistica( mob.pos, target, Ballistica.PROJECTILE);

            fx(shot, () -> onHit(shot, mob));
        }
    }
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( Dungeon.hero.sprite.emitter(), MagicMissile.WARD, Dungeon.hero.sprite,
                bolt.collisionPos,
                callback);
    }

    protected void onHit(Ballistica bolt, Char mob) {

        //presses all tiles in the AOE first

        if (mob != null){
            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Buff.affect( this, MagicImmune.class, MagicImmune.DURATION );
            }
        }

    }

    private void zap() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            //TODO would be nice for this to work on ghost/statues too
            if (enemy == Dungeon.hero && Random.Int( 2 ) == 0) {
                Buff.prolong( enemy, Degrade.class, Degrade.DURATION );
                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
            }

            int dmg = Random.NormalIntRange(2+Dungeon.depth, 4+Dungeon.depth );

            enemy.damage( dmg, new ColdGurad.DarkBolt() );


            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "bolt_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    @Override
    public void call() {
        next();
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if(enemy != null){
            if(HP > HT/2){
                if (Random.Int( 3 ) == 0) {
                    Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, 7f );
                    enemy.sprite.burst( 0x000000, 5 );
                }
            } else if (HP < HT/2) {
                if (Random.NormalFloat( 0,100 ) <= 10) {
                    GLog.n( Messages.get(FireMagicDied.class, "died_kill",Dungeon.hero.name()) );
                    bolt(damage/3,enemy);
                } else {
                    zap();
                }
            } else {
                if (Random.Int( 3 ) == 0) {
                    Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, 24f );
                    enemy.sprite.burst( 0x000000, 5 );
                }
            }


            if (pumpedUp > 0) {
                Camera.main.shake( 3, 0.2f );
            }
        }
        return damage;
    }

    @Override
    public void updateSpriteState() {
        super.updateSpriteState();

        if (pumpedUp > 0){
            ((FireMagicGirlSprite)sprite).pumpUp( pumpedUp );
        }
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if (pumpedUp == 1) {
            pumpedUp++;
            ((FireMagicGirlSprite)sprite).pumpUp( pumpedUp );

            spend( attackDelay() );

            return true;
        } else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 5 ) > 0) {

            boolean visible = Dungeon.level.heroFOV[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((FireMagicGirlSprite) sprite).pumpAttack();
                } else {
                    sprite.zap(enemy.pos);
                    spend(3f);
                }
            } else {
                if (pumpedUp >= 2){
                    ((FireMagicGirlSprite)sprite).triggerEmitters();
                }
                attack( enemy );
                Invisibility.dispel(this);
                spend( attackDelay() );
            }

            return !visible;

        } else {

            pumpedUp++;

            ((FireMagicGirlSprite)sprite).pumpUp( pumpedUp );


            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!!!") );
            }

            spend( attackDelay() );

            return true;
        }
    }


    @Override
    public boolean isAlive() {
        if(phase>=3){
            return super.isAlive();
        } else {
            return true;
        }
    }

    @Override
    public void die( Object cause ) {
        if(Statistics.bossRushMode){
            GetBossLoot(pos);
        }

        if(Statistics.RandMode){
            SmallLeafHardDungeon smallLeafHardDungeon = new SmallLeafHardDungeon();
            smallLeafHardDungeon.pos = pos;
            Dungeon.level.mobs.add(smallLeafHardDungeon);
            GameScene.add( smallLeafHardDungeon );
            Dungeon.level.occupyCell( smallLeafHardDungeon );
        }

        if(Statistics.amuletObtained|| Statistics.RandMode){
            Dungeon.level.drop(new IceCyanBlueSquareCoin(15),pos);
            Buff.detach(hero, BeamTowerAdbility.class);
        }

        super.die( cause );
        Statistics.bossScores[3] += 1000 * Dungeon.depth/5;
        //Dungeon.level.drop(new BackGoKey().quantity(1).identify(), pos).sprite.drop();
        Dungeon.level.drop(new ScrollOfMagicMapping().quantity(1).identify(), pos).sprite.drop();


        if(Dungeon.isChallenged(CS)){
            Dungeon.level.drop(new Gold().quantity(1012), pos).sprite.drop();
            Dungeon.level.drop(new ScrollOfUpgrade().quantity(1).identify(), pos).sprite.drop();
        } else {
            Dungeon.level.drop(new Gold().quantity(720), pos).sprite.drop();
            if(Random.Int(100)<=20){
                Dungeon.level.drop(new ScrollOfUpgrade().quantity(1).identify(), pos).sprite.drop();
            } else {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.WAND ) ).upgrade(), hero.pos );
            }
        }

        Dungeon.level.unseal();

        Buff.affect(hero, ShopLimitLock.class).set((1), 1);

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof SRPDICLRPRO ||mob instanceof Skeleton||mob instanceof DM100|| mob instanceof BlackHost|| mob instanceof Warlock|| mob instanceof Monk|| mob instanceof CrystalDiedTower|| mob instanceof CrystalLingTower) {
                mob.die( cause );
            }
        }

        GameScene.bossSlain();
        Buff.detach(hero, MagicGirlSayTimeLast.class);

        PaswordBadges.KILLFIREGIRL();

        yell( Messages.get(this, "defeated",Dungeon.hero.name()) );
    }

    @Override
    public void notice() {
        Dungeon.level.playBossMusic();
        BossHealthBar.assignBoss(this);
    }

    @Override
    public void onDeath() {
        Statistics.bossScores[3] -= 1500;
    }


    public static class YogScanHalf extends Buff implements ScanningBeam.OnCollide{
        private int left = 5;
        //00:x- 01:x+ 10:y- 11:y+
        private int direction = 0;
        private int center = -3;

        public YogScanHalf setPos(int c, int d){
            this.center = c;
            this.direction = d;
            return this;
        }

        @Override
        public void storeInBundle(Bundle b){
            super.storeInBundle(b);
            b.put("centerPos", center);
            b.put("fourDirections", direction);
            b.put("leftTime", left);
        }

        @Override
        public void restoreFromBundle(Bundle b){
            super.restoreFromBundle(b);
            center = b.getInt("centerPos");
            direction = b.getInt("fourDirections");
            left = b.getInt("leftTime");
        }

        @Override
        public boolean act(){
            spend(TICK);

            if(left > 0){
                renderWarning((direction & 2) == 0, (direction & 1) != 0);
                --left;
            }else {
                renderSkill((direction & 2) == 0, (direction & 1) != 0);
                diactivate();
            }

            return true;
        }
        //warning
        protected void renderWarning(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int h = Dungeon.level.height();
            int xOfs = center % w;
            int yOfs = center / w;
            int startX; int startY;
            int endX; int endY;
            if(isx){
                startX = xOfs + (5 - left) * (positive ? 1: -1) * 2;
                endX = startX;
                startY = 1;
                endY = h - 1;
            }else{
                startY = yOfs + (5 - left) * (positive ? 1: -1) * 2;
                endY = startY;
                startX = 1;
                endX = w - 1;
            }
            target.sprite.parent.add(new BeamCustom(
                    new PointF(startX, startY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    new PointF(endX, endY).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE),
                    Effects.Type.LIGHT_RAY)
                    .setLifespan(0.7f).setColor(0xff0000)
            );
        }
        //damage
        protected void renderSkill(boolean isx, boolean positive){
            int w = Dungeon.level.width();
            int xOfs = center % w;
            int yOfs = center / w;
            float startX; float startY;
            float xsp = 0; float ysp = 0;
            float ang;
            float r;
            if(isx){
                startX = xOfs;
                startY = 3;
                xsp = 10f * (positive ? 1f : -1f);
                ang = 90f;
                r = w - 6;
            }else{
                startY = yOfs;
                startX = 3;
                ysp = 10f * (positive ? 1f : -1f);
                ang = 0f;
                r = Dungeon.level.height() - 6;
            }

            ScanningBeam.setCollide(this);
            target.sprite.parent.add(new ScanningBeam(Effects.Type.LIGHT_RAY, BallisticaReal.STOP_TARGET,
                            new ScanningBeam.BeamData()
                                    .setPosition(startX+0.8f, startY + 0.8f, ang, r)
                                    .setSpeed(xsp, ysp, 0f)
                                    .setTime(0.32f, 2.5f, 0.5f)
                    ).setDiameter(3f)
            );
            VirtualActor.delay(1.8f, ()->{
                detach();
                Camera.main.shake(2f, 0.3f);
            });

            Camera.main.shake(2f, 100f);

        }

        @Override
        public int onHitProc(Char ch) {
            if(ch.alignment == Alignment.ENEMY) return 0;
            //改为魔法伤害
            ch.damage( Random.Int(15, 30), new DM100.LightningBolt() );
            Buff.affect( ch, HalomethaneBurning.class ).reignite( ch, 7f );
            if(ch == Dungeon.hero){
                Sample.INSTANCE.play(Assets.Sounds.BLAST, Random.Float(1.1f, 1.5f));
                if(!ch.isAlive()) Dungeon.fail(getClass());
            }
            ch.sprite.centerEmitter().burst( PurpleParticle.BURST, Random.IntRange( 15, 10 ) );ch.sprite.flash();
            return 1;
        }

        @Override
        public int cellProc(int i) {
            if(Dungeon.level.flamable[i]){
                Dungeon.level.destroy(i);
                GameScene.updateMap( i );
            }
            return 0;
        }
    }

    public static class StrengthEmpower extends FlavourBuff {
        Emitter charge;
        @Override
        public void fx(boolean on){
            if (on && charge == null) {
                charge = target.sprite.emitter();

                charge.pour(Speck.factory(Speck.UP), 0.7f);

            } else {
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }
            }
        }
        @Override
        public boolean attachTo(Char target){
            target.sprite.showStatus(0x00FF00, Messages.get(DwarfMaster.class, "str_empower"));
            return super.attachTo(target);
        }
    }

    public static class KingDamager extends Buff {

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
            for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
                if (m instanceof FireMagicDied ){
                    m.damage(30, this, DamageType.REAL);
                }
            }
        }
    }

}
