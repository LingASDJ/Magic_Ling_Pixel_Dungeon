package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
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
import com.shatteredpixel.shatteredpixeldungeon.items.keys.SkeletonKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfGodIce;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesGirlDeadLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
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
        initBaseStatus(16, 22, 28, 16, 400, 4, 8);
        initStatus(76);
        HP=400;
        HT=400;
        viewDistance = 18;
    }
    //the actual affected cells
    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;
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

        immunities.add(Paralysis.class);
    }

    //0~7 phases. if health < threshold[phase], then go on.
    private static final int[] healthThreshold = new int[]{399, 330, 270, 210, 160, 120, 80, 40, -1000000};

    private int phase = 0;

    private float summonCD = 50f;

    private int lastTargeting = -1;


    @Override
    public String info(){
        return Messages.get(this, "desc", phase, HP - healthThreshold[phase]);
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
            places.add(5*Dungeon.level.width()+4);
            places.add(6*Dungeon.level.width()-5);
            places.add(17*Dungeon.level.width()+4);
            places.add(18*Dungeon.level.width()-5);
            Random.shuffle(places);
            for(int i=0;i<Math.min(phase/2, 4);++i){
                summonCaster(Random.Int(4), places.get(i),false);
            }
//        }else{
//            destroyAll();
//            for(int i=0;i<phase/2+1;++i){
//                summonCaster(Random.Int(6), findRandomPlaceForCaster(), false);
//            }
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
        fx(shot, new Callback() {
            @Override
            public void call() {
                onZap(shot);
            }
        }, ch);
    }

    protected void fx(Ballistica bolt, Callback callback, Char ch ) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        int maxDist = 4 + 4*4;
        int dist = Math.min(bolt.dist, maxDist);

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
                direction = i;
                break;
            }
        }

//        float strength = maxDist;
//        for (int c : bolt.subPath(1, dist)) {
//            strength--; //as we start at dist 1, not 0.
//            affectedCells.add(c);
//            if (strength > 1) {
//                spreadFlames(c + PathFinder.CIRCLE8[left(direction)], strength - 1);
//                spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);
//                spreadFlames(c + PathFinder.CIRCLE8[right(direction)], strength - 1);
//            } else {
//                visualCells.add(c);
//            }
//        }

        //going to call this one manually

        //this way we only get the cells at the tip, much better performance.
        MagicMissile.boltFromChar( ch.sprite.parent,
                MagicMissile.FROST,
                ch.sprite,
                bolt.path.get(dist/2),
                callback );
        if(Dungeon.level.heroFOV[bolt.sourcePos] || Dungeon.level.heroFOV[bolt.collisionPos]){
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
        }
    }


    //burn... BURNNNNN!.....
//    private void spreadFlames(int cell, float strength){
//        if (strength >= 0 && (Dungeon.level.passable[cell] || Dungeon.level.flamable[cell])){
//            affectedCells.add(cell);
//            if (strength >= 1.5f) {
//                visualCells.remove(cell);
//                spreadFlames(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
//                spreadFlames(cell + PathFinder.NEIGHBOURS9[direction], strength - 1.5f);
//                spreadFlames(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
//            } else {
//                visualCells.add(cell);
//            }
//        } else if (!Dungeon.level.passable[cell])
//            visualCells.add(cell);
//    }

    private int left(int direction){
        return direction == 0 ? 3 : direction-1;
    }

    private int right(int direction){
        return direction == 7 ? 0 : direction+1;
    }
    private static final float TIME_TO_BURN	= 6f;
    @Override
    public boolean act(){
        if(paralysed>0){
            spend(TICK);
            summonCD -= 1/speed();
            return true;
        }
        for (Buff buff : hero.buffs()) {
            if (buff instanceof RoseShiled) {
                buff.detach();
                GLog.b("……你妄图使用这种方法来逃脱吗？");
                Statistics.bossScores[2] -= 800;
            }
            if (buff instanceof HaloFireImBlue ||buff instanceof FireImbue) {
                buff.detach();
                GLog.b("……你妄图使用这种方法来逃脱吗？");
                Statistics.bossScores[2] -= 800;
            }
        }

//        if(buff(RageAndFire.class)!=null){
//            //if target is locked, fire, target = -1
//            if(lastTargeting != -1){
//                //no spend, execute next act
//                    //sprite.attack( enemy.pos );
//                    spend( attackDelay()*5f );
//                    if(pos == 0) {
//                        shoot(this, 1);
//                    } else {
//                        shoot(this, enemy.pos);
//                    }
//
//                return true;
//                //else try to lock target
//            }else if(findTargetLocation()) {
//                //if success, spend and ready to fire
//                return true;
//            }//else, just act
//        }
        if(summonCD<0f){
            summonCD += Math.max(60f - phase * 2f, 40f);
            summonCaster(Random.Int(4), findRandomPlaceForCaster(), phase>5);
        }
        summonCD -= 1/speed();
        return super.act();
    }


    @Override
    public void move(int step) {

        super.move(step);

        Camera.main.shake(  1, 0.25f );


        //冰雪魔女踩一次水扣300分
        if (Dungeon.level.map[step] == Terrain.WATER && state == HUNTING) {
            Statistics.bossScores[2] -= 300;
            if (Dungeon.level.heroFOV[step]) {
                if (buff(Haste.class) == null) {
                    Buff.affect(this, Haste.class, 10f);
                    Buff.affect(this, Healing.class).setHeal(42, 0f, 6);
                    new SRPDICLRPRO().spawnAround(pos);
                    yell( Messages.get(this, "arise") );
                    GLog.b(Messages.get(this, "shield"));
                    enemy.sprite.showStatus(0x00ffff, ("！！！"));
                }
                sprite.emitter().start(SparkParticle.STATIC, 0.05f, 20);
            }



            if (Dungeon.level.water[pos] && HP < HT) {
                if (Dungeon.level.heroFOV[pos] ){
                    sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                }
                if (HP*2 == HT) {
                    BossHealthBar.bleed(false);
                }
                HP++;
            }

            summonCD -= 24f;

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
    public void damage(int damage, Object src){
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "notice"));
        }
        if (damage >= 30){
            damage = 30 + (int)(Math.sqrt(4*(damage - 14) + 1) - 1)/2;
        }

        if (HP <= 50){
            damage = 5;
        }

        if(buff(RageAndFire.class)!=null) damage = Math.round(damage*0.1f);

        int preHP = HP;
        super.damage(damage, src);
        int postHP = HP;
        if(preHP>healthThreshold[phase] && postHP<=healthThreshold[phase]){
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    HP = healthThreshold[phase];
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
        if (Dungeon.isDLC(Conducts.Conduct.BOSSRUSH)) {

            GetBossLoot();
        }
        int shards = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < shards; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new MetalShard(), pos + ofs ).sprite.drop( pos );
        }

        yell(Messages.get(this, "die"));

        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(m instanceof SpellCaster){
                m.die(this);
                Dungeon.level.mobs.remove(m);
            }
        }

        Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
        GameScene.bossSlain();
        Badges.KILLMG();
        Badges.validateBossSlain();

        WandOfGodIce woc = new WandOfGodIce();
        woc.level(Random.NormalIntRange(2,6));
        woc.identify();

        Dungeon.level.drop(woc, pos).sprite.drop();

        Dungeon.level.drop(new Gold().quantity(Random.Int(1800, 1200)), pos).sprite.drop();
        Dungeon.level.drop(new PotionOfHealing().quantity(Random.NormalIntRange(1, 2)), pos).sprite.drop();
        Dungeon.level.drop(new ScrollOfMagicMapping().quantity(1).identify(), pos).sprite.drop();
        Dungeon.level.drop(new ScrollOfUpgrade().quantity(1).identify(), pos).sprite.drop();


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
                case BOUNCE: default:
                    caster = new SpellCaster.BounceCaster();
            }
            caster.pos = pos;
            GameScene.add(caster, Random.Float(2f, 8f));
            Dungeon.level.mobs.add(caster);
            fallingRockVisual(pos);
            if(activate) caster.activate();
            Dungeon.level.passable[pos] = false;
        }
    }

    protected int findRandomPlaceForCaster(){

        int[] ceil = GME.rectBuilder(pos, 4, 4);

        //shuffle
        for (int i=0; i < ceil.length - 1; i++) {
            int j = Random.Int( i, ceil.length );
            if (j != i) {
                int t = ceil[i];
                ceil[i] = ceil[j];
                ceil[j] = t;
            }
        }

        boolean valid;
        for(int i: ceil){
            valid = true;
            for(int j: PathFinder.NEIGHBOURS4){
                if(findChar(j+i)!=null){
                    valid = false;break;
                }
            }
            if(!valid) continue;
            if(findChar(i) == null && !Dungeon.level.solid[i] && !(Dungeon.level.map[i]==Terrain.INACTIVE_TRAP)){

                //caster.spriteHardlight();
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
        return HP>0 || healthThreshold[phase]>0;
    }
}

