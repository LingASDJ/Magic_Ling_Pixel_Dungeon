package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SpellCaster;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HaloFireImBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Sleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SnowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.BoneSoup;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfScale;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfSun;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfBlueFuck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfHightHunderStorm;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.hightwand.WandOfVenom;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesGirlDeadLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class MagicGirlDead extends Boss {
    {
        spriteClass = MagicGirlSprite.class;

        initProperty();


        if(Statistics.bossRushMode || Statistics.amuletObtained){
            initBaseStatus(25, 46, 28, 20, 1000, 4, 8);
        } else if(level instanceof ShopBossLevel) {
            initBaseStatus(19, 32, 28, 18, 400*((Dungeon.depth/10f) < 1 ? 1 : (Dungeon.depth/10f)), 4, 8);
        } else {
            initBaseStatus(16, 22, 28, 16, 400, 4, 8);
        }

        initStatus(76);
        viewDistance = 18;
    }


    @Override
    public String name() {
        return Statistics.attackIFGirl ? Messages.get(this,"name_alt") : super.name();
    }

    //the actual affected cells
    private HashSet<Integer> affectedCells;

    {
        immunities.add(Sleep.class);

        resistances.add(Terror.class);
        resistances.add(Charm.class);
        resistances.add(Vertigo.class);
        resistances.add(Cripple.class);
        resistances.add(Chill.class);
        resistances.add(Frost.class);
        resistances.add(Roots.class);
        resistances.add(Slow.class);

        if(Statistics.bossRushMode){
            immunities.add(Chill.class);
            immunities.add(Frost.class);
            immunities.add(FrostBurning.class);
        }

        immunities.add(Paralysis.class);
    }

    private static final int[] healthThreshold = new int[]{399, 330, 270, 210, 160, 120, 80, 40, -1000000};

    private static final int[] healthThresholdX = new int[]{900, 600, 550, 400, 300, 220, 120, 100, -1000000};

    private int phase = 0;

    public float summonCD = 50f;

    private int lastTargeting = -1;


    @Override
    public String info(){
        return Statistics.attackIFGirl ? Messages.get(this,"desc_alt") : Messages.get(this, "desc", phase, HP - (Statistics.bossRushMode? healthThresholdX[phase] : healthThreshold[phase]));
    }

    @Override
    public float speed(){
        return super.speed() * (0.6f + phase*0.05f);
    }

    protected void goOnPhase(){
        phase++;
        CellEmitter.center(pos).burst(SnowParticle.FACTORY, 30);
        Sample.INSTANCE.play( Assets.Sounds.CURSED );

        if(phase % 2 == 0){
            destroyAll();
            ArrayList<Integer> places = new ArrayList<>();
            if(level instanceof ShopBossLevel){
                places.add(10*Dungeon.level.width()+10);
                places.add(10*Dungeon.level.width()+24);
                places.add(24*Dungeon.level.width()+10);
                places.add(864);
                places.add(927);
                places.add(621);
                places.add(603);
                places.add(297);
                Random.shuffle(places);
                for(int i=0;i<Math.min(phase*2/2, 8);++i){
                    summonCaster(Random.Int(Statistics.bossRushMode? 8 : 4), places.get(i),false);
                }
            } else if(HP>50) {
                places.add(5*Dungeon.level.width()+4);
                places.add(6*Dungeon.level.width()-5);
                places.add(17*Dungeon.level.width()+4);
                places.add(18*Dungeon.level.width()-5);
                Random.shuffle(places);
                for(int i=0;i<Math.min(phase/2, 4);++i){
                    summonCaster(Random.Int(Statistics.bossRushMode? 8 : 4), places.get(i),false);
                }
            }


        }

        activateAll();

        lastTargeting = -1;
        Buff.affect(this, RageAndFire.class, 1f*phase + 5f);

        yell(Messages.get(this, "damaged"));
    }

    protected void onZap( Ballistica bolt ) {

        for( int cell : affectedCells){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            //only ignite cells directly near caster if they are flammable
            if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
                    || Dungeon.level.flamable[cell]){
                GameScene.add( Blob.seed( cell, 1+2, Freezing.class ) );
            }
        }
    }

    public void shoot(Char ch, int pos){
        final Ballistica shot = new Ballistica( ch.pos, pos, Ballistica.MAGIC_BOLT);
        fx(shot, () -> onZap(shot), ch);
    }

    protected void fx(Ballistica bolt, Callback callback, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        //the cells to trace fire shots to, for visual effects.
        HashSet<Integer> visualCells = new HashSet<>();

        int maxDist = 4 + 4*4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                break;
            }
        }

        MagicMissile.boltFromChar( ch.sprite.parent,
                MagicMissile.FROST,
                ch.sprite,
                bolt.path.get(dist/2),
                callback );
        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }

    @Override
    public boolean act(){

        if(level instanceof ShopBossLevel){
           properties.add(Property.ICY);
            Music.playModeBGM(Assets.Music.IFWAR,true);
        }

        if(paralysed>0){
            spend(TICK);
            summonCD -= 1/speed();
            return true;
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof RoseShiled) {
                buff.detach();
                GLog.b(Messages.get(this,"run"));
                Statistics.bossScores[2] -= 800;
            }
            if (buff instanceof HaloFireImBlue ||buff instanceof FireImbue) {
                buff.detach();
                GLog.b(Messages.get(this,"run"));
                Statistics.bossScores[2] -= 800;
            }
        }

        if(summonCD<0f){
            summonCD += Math.max(60f - phase * 2f, 40f);
            if(level instanceof ShopBossLevel){
                ArrayList<Integer> places = new ArrayList<>();
                places.add(611);
                places.add(613);
                places.add(577);
                places.add(647);
                Random.shuffle(places);
                boolean valid;
                for(int i=0;i<Math.min(phase/2, 4);++i){
                    valid = findChar(i) != null;
                    if(!valid){
                        summonCaster(Random.Int(4), places.get(i),false);
                    }
                }
            } else {
                summonCaster(Random.Int(Statistics.bossRushMode? 8 : 4), findRandomPlaceForCaster(), phase>5);
            }

        }
        summonCD -= 1/speed();
        return super.act();
    }


    @Override
    public void move(int step) {
        super.move(step);

        if (Dungeon.level.map[step] == Terrain.WATER && state == HUNTING) {
            if (!(level instanceof ShopBossLevel)) {
                Statistics.bossScores[2] -= 300;
                if (Dungeon.level.heroFOV[step]) {
                    if (buff(Haste.class) == null) {
                        Buff.affect(this, Haste.class, 10f);
                        Buff.affect(this, Healing.class).setHeal(42, 0f, 6);
                        new SRPDICLRPRO().spawnAround(pos);
                        yell(Messages.get(this, "arise"));
                        GLog.b(Messages.get(this, "shield"));
                        enemy.sprite.showStatus(0x00ffff, ("！！！"));
                    }
                    sprite.emitter().start(SparkParticle.STATIC, 0.05f, 20);
                }


                if (Dungeon.level.water[pos] && HP < HT) {
                    if (Dungeon.level.heroFOV[pos]) {
                        sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                    }
                    if (HP * 2 == HT) {
                        BossHealthBar.bleed(false);
                    }
                    HP++;
                }

                summonCD -= 24f;

            }
        }
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
            for (Char ch : Actor.chars()){
                if (ch instanceof DriedRose.GhostHero){
                    ((DriedRose.GhostHero) ch).sayBoss();
                }
            }
        }
    }

    @Override
    public void damage(int damage, Object src, DamageType type){

        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        } else if(level instanceof ShopBossLevel){
            BossHealthBar.assignBoss(this);
        }
        if (damage >= 30){
            damage = 30 + (int)(Math.sqrt(4*(damage - 14) + 1) - 1)/2;
        }

        if (HP <= 50){
            damage = 5;
        }

        if(buff(RageAndFire.class)!=null) damage = Math.round(damage*0.1f);

        int preHP = HP;
        super.damage(damage, src, type);
        int postHP = HP;
        if(preHP> (Statistics.bossRushMode? healthThresholdX[phase] : healthThreshold[phase]) && postHP<= (Statistics.bossRushMode? healthThresholdX[phase] : healthThreshold[phase])){
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    HP =  Statistics.bossRushMode? healthThresholdX[phase] : healthThreshold[phase];
                    goOnPhase();
                    return true;
                }
            });
        }

        if(phase>4) BossHealthBar.bleed(true);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(damage*2);
    }

    @Override
    public void die(Object src){
        Statistics.bossScores[2] += 5000;
        super.die(src);

        if(Statistics.attackIFGirl) {
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob instanceof FireMagicDied) {
                    mob.yell(Messages.get(mob,"sad",hero.name()));
                    ((FireMagicDied) mob).allDead = true;
                }
            }
        }

        Badges.validateBossSlain();
        if (Statistics.qualifiedForBossChallengeBadge){
            Badges.validateBossChallengeCompleted();
        }
        
        int pos = 175;

        if(Statistics.bossRushMode){
            GetBossLoot(pos);
            Buff.detach( hero, Doom.class );
        }

        if(level instanceof ShopBossLevel){
            Dungeon.level.drop(new Gold().quantity(Random.Int(500, 900)),717).sprite.drop();
            Wand woc = HightWand();
            woc.level(Random.NormalIntRange(0,2));
            woc.identify();
            Dungeon.level.drop(woc, 717).sprite.drop();

            Dungeon.level.drop(new BoneSoup(), 717).sprite.drop();

        } else {
            int shards = Random.chances(new float[]{0, 0, 6, 3, 1});
            for (int i = 0; i < shards; i++){
                int ofs;
                do {
                    ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (!Dungeon.level.passable[pos + ofs]);
                Dungeon.level.drop( new MetalShard(), pos + ofs ).sprite.drop( pos );
            }

            yell(Messages.get(this, "die"));

            Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
            GameScene.bossSlain();

            WandOfGodIce woc = new WandOfGodIce();
            woc.level(Random.NormalIntRange(2,6));
            woc.identify();

            Dungeon.level.drop(woc, pos).sprite.drop();

            Dungeon.level.drop(new Gold().quantity(Random.Int(1800, 1200)), pos).sprite.drop();
            Dungeon.level.drop(new PotionOfHealing().quantity(Random.NormalIntRange(1, 2)), pos).sprite.drop();
            Dungeon.level.drop(new ScrollOfMagicMapping().quantity(1).identify(), pos).sprite.drop();
        }

        Badges.KILLMG();

        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                m.die(this);
                Dungeon.level.mobs.remove(m);
            }
        }

    }

    private Wand HightWand() {
        Wand wand = null;
        switch (Random.Int(6)){
            default:
                wand = new WandOfVenom();
            break;
            case 1:
                wand = new WandOfScale();
            break;
            case 2:
                wand = new WandOfBlueFuck();
            break;
            case 3:
                wand = new WandOfGodIce();
            break;
            case 4:
                wand = new WandOfHightHunderStorm();
            break;
            case 5:
                wand = new WandOfSun();
            break;
        }
        return wand;
    }

    @Override
    protected boolean canAttack(Char enemy){
        if(enemy!=null && enemySeen){
            return Dungeon.level.distance(pos, enemy.pos) < 3;
        }
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put("phaseDM", phase);
        bundle.put("summonCD", summonCD);
        bundle.put("lastTargetingDM", lastTargeting);
        super.storeInBundle(bundle);

    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        phase = bundle.getInt("phaseDM");
        summonCD = bundle.getFloat("summonCD");
        lastTargeting = bundle.getInt("lastTargetingDM");

        BossHealthBar.assignBoss(this);
        if (phase>4) BossHealthBar.bleed(true);

    }

//caster ability logic

    private static final int FROST = 0;
    private static final int EXPLODE = 1;
    private static final int LIGHT = 2;
    private static final int HALOFIRE = 3;
    private static final int BOUNCE = 4;

    private static final int DEGRADE = 5;
    private static final int DEATHRAY = 6;

    protected void fallingRockVisual(int pos){
        Camera.main.shake(0.4f, 2f);
        CellEmitter.get( pos - Dungeon.level.width() ).start(Speck.factory(Speck.RED_LIGHT), 0.08f, 10);
    }

    protected void activateVisual(int pos){
        CellEmitter.get( pos ).start(Speck.factory(Speck.STAR), 0.14f, 8);
    }

    protected void summonCaster(int category, int pos, boolean activate){
        if(pos != -1){
            SpellCaster caster;
            switch (category){
                case FROST:
                    caster = new SpellCaster.FrostCaster();
                    break;
                case EXPLODE:
                    caster = new SpellCaster.ExplosionCaster();
                    break;
                case LIGHT:
                    caster = new SpellCaster.LightCaster();
                    break;
                case HALOFIRE:
                    caster = new SpellCaster.HaloFireCaster();
                    break;
                case DEGRADE:
                    caster = new SpellCaster.DegradeCaster();
                    break;
                case DEATHRAY:
                    caster = new SpellCaster.DeadLingCaster();
                    break;
                case BOUNCE: default:
                    caster = new SpellCaster.BounceCaster();
                    break;
            }
            caster.pos = pos;
            GameScene.add(caster, Random.Float(2f, 8f));
            Dungeon.level.mobs.add(caster);
            fallingRockVisual(pos);
            if(activate) caster.activate();
            Dungeon.level.passable[pos] = false;
        }
    }

    protected int findRandomPlaceForCaster() {

        int[] ceil = GME.rectBuilder(pos, 4, 4);

        for (int i = 0; i < ceil.length - 1; i++) {
            int j = Random.Int(i, ceil.length);
            if (j != i) {
                int t = ceil[i];
                ceil[i] = ceil[j];
                ceil[j] = t;
            }
        }

        boolean valid;
        for (int i : ceil) {
            valid = true;
            for (int j : PathFinder.NEIGHBOURS4) {
                if (findChar(j + i) != null) {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;
            if (findChar(i) == null && !Dungeon.level.solid[i] && !(Dungeon.level.map[i] == Terrain.INACTIVE_TRAP)) {
                return i;
            }
        }

        return -1;
    }

    protected void activateAll(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                if(m.alignment == Alignment.NEUTRAL) {
                    ((SpellCaster) m).activate();
                    activateVisual(m.pos);
                }
            }
        }
    }

    protected void destroyAll(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                if(m.alignment == Alignment.NEUTRAL) continue;
                Ballistica beam = new Ballistica(m.pos, hero.pos, Ballistica.WONT_STOP);
                m.sprite.parent.add(new BeamCustom(
                        DungeonTilemap.raisedTileCenterToWorld(m.pos),
                        DungeonTilemap.tileCenterToWorld(beam.collisionPos),
                        Effects.Type.DEATH_RAY).setLifespan(0.9f));
                for(int i: beam.path){
                    Char ch = findChar(i);
                    if(ch!=null){
                        if(ch.alignment != Alignment.ENEMY){
                            SpellCaster.zapDamage(ch, 20, 30, 0.85f, m);
                        }
                    }
                }
                m.die(this);
                Dungeon.level.mobs.remove(m);
            }
        }
    }

    public void onZapComplete(){
        ventGas(enemy);
        next();
    }

    public void ventGas( Char target ){
        hero.interrupt();

        int gasVented = 0;

        Ballistica trajectory = new Ballistica(pos, target.pos, Ballistica.STOP_TARGET);

        int gasMulti = 2 ;

        for (int i : trajectory.subPath(0, trajectory.dist)){
            GameScene.add(Blob.seed(i, 20*gasMulti, ToxicGas.class));
            gasVented += 20*gasMulti;
        }

        GameScene.add(Blob.seed(trajectory.collisionPos, 100*gasMulti, ToxicGas.class));

        if (gasVented < 250*gasMulti){
            int toVentAround = (int)Math.ceil(((250*gasMulti) - gasVented)/8f);
            for (int i : PathFinder.NEIGHBOURS8){
                GameScene.add(Blob.seed(pos+i, toVentAround, ToxicGas.class));
            }

        }

    }
    public boolean supercharged = false;
    public boolean isSupercharged(){
        return supercharged;
    }

    public void dropRocks( Char target ) {

        hero.interrupt();
        final int rockCenter;

        if (Dungeon.level.adjacent(pos, target.pos)){
            int oppositeAdjacent = target.pos + (target.pos - pos);
            Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(target, trajectory, 2, false, false, getClass());
            if (target == hero){
                hero.interrupt();
            }
            rockCenter = trajectory.path.get(Math.min(trajectory.dist, 2));
        } else {
            rockCenter = target.pos;
        }

        int safeCell;
        do {
            safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (safeCell == pos
                || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                || (Blob.volumeAt(safeCell, CavesGirlDeadLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

        ArrayList<Integer> rockCells = new ArrayList<>();

        int start = rockCenter - Dungeon.level.width() * 3 - 3;
        int pos;
        for (int y = 0; y < 7; y++) {
            pos = start + Dungeon.level.width() * y;
            for (int x = 0; x < 7; x++) {
                if (!Dungeon.level.insideMap(pos)) {
                    pos++;
                    continue;
                }
                //add rock cell to pos, if it is not solid, and isn't the safecell
                if (!Dungeon.level.solid[pos] && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
                    //don't want to overly punish players with slow move or attack speed
                    rockCells.add(pos);
                }
                pos++;
            }
        }
        Buff.append(this, DM300.FallingRockBuff.class, Math.min(target.cooldown(), 3*TICK)).setRockPositions(rockCells);

    }

    public void onSlamComplete(){
        dropRocks(enemy);
        next();
    }
    public static class RageAndFire extends FlavourBuff {
        Emitter charge;
        @Override
        public void fx(boolean on){
            if(on) {
                charge = target.sprite.emitter();
                charge.autoKill = false;
                charge.pour(SparkParticle.STATIC, 0.05f);
                //charge.on = false;
            }else{
                if(charge != null) {
                    charge.on = false;
                    charge = null;
                }

            }
        }
    }

    @Override
    public boolean isAlive(){
        return HP>0 || (Statistics.bossRushMode ? healthThresholdX[phase] : healthThreshold[phase])>0;
    }
}

