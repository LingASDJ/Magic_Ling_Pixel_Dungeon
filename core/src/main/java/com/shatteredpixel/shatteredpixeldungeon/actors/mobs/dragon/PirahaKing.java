package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.dragon;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PirahaKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class PirahaKing extends Boss {

    {
        spriteClass = PirahaKingSprite.class;
        viewDistance = Light.DISTANCE;
        HP = HT = 500;
        initProperty();
        HUNTING = new Hunting();
    }
    @Override
    public void damage(int dmg, Object src) {
        Char dmgSource = null;
        if (src instanceof Char) dmgSource = (Char)src;
        if (src instanceof Wand) dmgSource = Dungeon.hero;

        if (dmgSource == null || !Dungeon.level.adjacent(pos, dmgSource.pos)){
            dmg = Math.round(dmg/2f); //halve damage taken if we are going to teleport
        }
        super.damage(dmg, src);

        if (isAlive() && !(src instanceof Corruption)) {
            if (dmgSource != null) {
                if (!Dungeon.level.adjacent(pos, dmgSource.pos)) {
                    ArrayList<Integer> candidates = new ArrayList<>();
                    for (int i : PathFinder.NEIGHBOURS8) {
                        if (Dungeon.level.water[dmgSource.pos + i] && Actor.findChar(dmgSource.pos + i) == null) {
                            candidates.add(dmgSource.pos + i);
                        }
                    }
                    if (!candidates.isEmpty()) {
                        ScrollOfTeleportation.appear(this, Random.element(candidates));
                        aggro(dmgSource);
                    } else {
                        teleportAway();
                    }
                }
            } else {
                teleportAway();
            }
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        return super.defenseProc(enemy, damage);
    }

    private boolean teleportAway(){

        ArrayList<Integer> inFOVCandidates = new ArrayList<>();
        ArrayList<Integer> outFOVCandidates = new ArrayList<>();
        for (int i = 0; i < Dungeon.level.length(); i++){
            if (Dungeon.level.water[i] && Actor.findChar(i) == null){
                if (Dungeon.level.heroFOV[i]){
                    inFOVCandidates.add(i);
                } else {
                    outFOVCandidates.add(i);
                }
            }
        }

        if(Random.NormalIntRange(0,100)<=25){
            dropRocks(this);
        }

        if (!outFOVCandidates.isEmpty()){
            if (Dungeon.level.heroFOV[pos]) GLog.i(Messages.get(this, "teleport_away"));
            ScrollOfTeleportation.appear(this, Random.element(outFOVCandidates));
            return true;
        } else if (!inFOVCandidates.isEmpty()){
            ScrollOfTeleportation.appear(this, Random.element(inFOVCandidates));
            return true;
        }

        return false;

    }

    @Override
    public int attackProc(Char enemy, int damage) {
        int dmg = super.attackProc(enemy, damage);
        if (enemy == Dungeon.hero && !Dungeon.level.adjacent(pos, enemy.pos) && dmg > 12){
            Buff.affect(enemy, Chill.class, 5f);
            GLog.n(Messages.get(this, "spear"));
        }
        return dmg;
    }


    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(2, 6);
    }
    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findStep( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee( this, target, Dungeon.level.water, fieldOfView, true );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    {
        for (Class c : new BlobImmunity().immunities()){
            if (c != Electricity.class && c != Freezing.class){
                immunities.add(c);
            }
        }
        immunities.add( Burning.class );
        immunities.add( HalomethaneBurning.class );
    }


    @Override
    public int damageRoll() {
        return Random.NormalIntRange(18, 22);
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){

                leapCooldown = Random.NormalIntRange(2, 4);

                if (rooted){
                    leapPos = -1;
                    return true;
                }

                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                leapPos = b.collisionPos;

                final Char leapVictim = Actor.findChar(leapPos);
                final int endPos;

                //ensure there is somewhere to land after leaping
                if (leapVictim != null){
                    int bouncepos = -1;
                    for (int i : PathFinder.NEIGHBOURS8){
                        if ((bouncepos == -1 || Dungeon.level.trueDistance(pos, leapPos+i) < Dungeon.level.trueDistance(pos, bouncepos))
                                && Actor.findChar(leapPos+i) == null && Dungeon.level.passable[leapPos+i]){
                            bouncepos = leapPos+i;
                        }
                    }
                    if (bouncepos == -1) {
                        leapPos = -1;
                        return true;
                    } else {
                        endPos = bouncepos;
                    }
                } else {
                    endPos = leapPos;
                }

                //do leap
                sprite.visible = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos] || Dungeon.level.heroFOV[endPos];
                sprite.jump(pos, leapPos, new Callback() {
                    @Override
                    public void call() {

                        if (leapVictim != null && alignment != leapVictim.alignment){
                            if (hit(PirahaKing.this, leapVictim, Char.INFINITE_ACCURACY, false)) {
                                Buff.affect(leapVictim, Cripple.class,0.75f * damageRoll());
                                leapVictim.sprite.flash();
                                Sample.INSTANCE.play(Assets.Sounds.HIT);
                            } else {
                                enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
                                Sample.INSTANCE.play(Assets.Sounds.MISS);
                            }
                        }

                        if (endPos != leapPos){
                            Actor.add(new Pushing(PirahaKing.this, leapPos, endPos));
                        }

                        pos = endPos;
                        leapPos = -1;
                        sprite.idle();
                        Dungeon.level.occupyCell(PirahaKing.this);
                        next();
                    }
                });
                return false;
            }

            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination( PirahaKing.this );
                    return true;
                }

                if (leapCooldown <= 0 && enemyInFOV && !rooted
                        && Dungeon.level.distance(pos, enemy.pos) >= 3) {

                    int targetPos = enemy.pos;
                    if (lastEnemyPos != enemy.pos){
                        int closestIdx = 0;
                        for (int i = 1; i < PathFinder.CIRCLE8.length; i++){
                            if (Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[i])
                                    < Dungeon.level.trueDistance(lastEnemyPos, enemy.pos+PathFinder.CIRCLE8[closestIdx])){
                                closestIdx = i;
                            }
                        }
                        targetPos = enemy.pos + PathFinder.CIRCLE8[(closestIdx+4)%8];
                    }

                    Ballistica b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    //try aiming directly at hero if aiming near them doesn't work
                    if (b.collisionPos != targetPos && targetPos != enemy.pos){
                        targetPos = enemy.pos;
                        b = new Ballistica(pos, targetPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);
                    }
                    if (b.collisionPos == targetPos){
                        //get ready to leap
                        leapPos = targetPos;
                        //don't want to overly punish players with slow move or attack speed
                        spend(GameMath.gate(attackDelay(), (int)Math.ceil(enemy.cooldown()), 3*attackDelay()));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
                            //GLog.w(Messages.get(PirahaKing.this, "leap"));
                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
                            ((PirahaKingSprite)sprite).leapPrep( leapPos );
                            Dungeon.hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1 / speed() );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend( TICK );
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( PirahaKing.this );
                    }
                    return true;
                }
            }
        }

    }

    private int phase = 1;

    private int summonsMade = 0;
    private static final int MIN_ABILITY_CD = 7;
    private int lastHeroPos;
    private static final int MAX_ABILITY_CD = 12;
    private float summonCooldown = 0;
    private float abilityCooldown = 3;
    private int wave=0;

    private int lastAbility = 0;

    private int leapPos = -1;
    private float leapCooldown = 0;
    private int lastEnemyPos = -1;

    private static final String PHASE = "phase";
    private static final String SUMMONS_MADE = "summons_made";

    private static final String SUMMON_CD = "summon_cd";
    private static final String ABILITY_CD = "ability_cd";
    private static final String LAST_ABILITY = "last_ability";

    private static final String TARGETED_CELLS = "targeted_cells";
    private ArrayList<Integer> targetedCells = new ArrayList<>();


    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( SUMMONS_MADE, summonsMade );
        bundle.put( SUMMON_CD, summonCooldown );
        bundle.put( ABILITY_CD, abilityCooldown );
        bundle.put( LAST_ABILITY, lastAbility );
        bundle.put("wavePhase2", wave);
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);

        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);
        bundle.put(LEAP_CD, leapCooldown);
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
        int[] bundleArr = new int[targetedCells.size()];
        for (int i = 0; i < targetedCells.size(); i++){
            bundleArr[i] = targetedCells.get(i);
        }
        bundle.put(TARGETED_CELLS, bundleArr);
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);
        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        Music.INSTANCE.play(Assets.Music.CAVES_BOSS_FINALE,true);
        leapPos = bundle.getInt(LEAP_POS);
        leapCooldown = bundle.getFloat(LEAP_CD);
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            yell(Messages.get(this, "notice"));
            Music.INSTANCE.play(Assets.Music.CAVES_BOSS_FINALE,true);
            Camera.main.shake(1f,3f);
        }
    }

    @Override
    public void die( Object cause ) {

        super.die( cause );

        Dungeon.level.unseal();

        PaswordBadges.KILL_FISH();

        PaswordBadges.UNLOCK_RICESWORD();

        GameScene.bossSlain();

        //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
        int blobs = Random.chances(new float[]{0, 0, 6, 3, 1});
        for (int i = 0; i < blobs; i++){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
            } while (!Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( new FrozenCarpaccio(), pos + ofs ).sprite.drop( pos );
        }

        Blacksmith.Quest.beatBoss();
        Sample.INSTANCE.playDelayed(Assets.Sounds.ROCKS, 0.1f);

        yell( Messages.get(this, "defeated") );
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (	mob instanceof RiceRat ||
                    mob instanceof PiraLand ||
                    mob instanceof CrystalMimic) {
                mob.die( cause );
            }
        }
    }

    @Override
    public boolean act() {
        doDiedLasers();
        if (state == WANDERING){
            leapPos = -1;
        }

//        if(!Dungeon.level.water[pos]){
//            teleportAway();
//            damage(5,this);
//            yell( Messages.get(this, "nowater") );
//        }

        AiState lastState = state;
        boolean result = super.act();
        if (paralysed <= 0) leapCooldown --;

        //if state changed from wandering to hunting, we haven't acted yet, don't update.
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = Dungeon.hero.pos;
            }
        }

        return result;
    }

    public void doDiedLasers(){
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
                    ch.damage(Random.NormalIntRange(14, 23),new YogFist.HaloFist.DarkBolt());

                    if (Dungeon.level.heroFOV[pos]) {
                        ch.sprite.flash();
                        CellEmitter.center(pos).burst(Speck.factory(Speck.COIN), Random.IntRange(6,12));
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
            if (abilityCooldown <= 0) {
                lastHeroPos = enemy.pos;

                int beams = (int) (4 + (HP * 1.5f / HT) * 2);
                for (int i = 0; i < beams; i++) {
                    int randompos = Random.Int(Dungeon.level.width()) + Dungeon.level.width() * 2;
                    targetedCells.add(randompos);
                }

                for (int i : targetedCells) {
                    Ballistica b = new Ballistica(i, enemy.pos, Ballistica.WONT_STOP);

                    for (int p : b.path) {
                        Game.scene().addToFront(new TargetedCell(p, Window.GDX_COLOR));
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
        if (abilityCooldown > 0) abilityCooldown--;
    }

    public void dropRocks( Char target ) {

        Dungeon.hero.interrupt();
        final int rockCenter;

        //knock back 2 tiles if adjacent
        if (Dungeon.level.adjacent(pos, target.pos)){
            int oppositeAdjacent = target.pos + (target.pos - pos);
            Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(target, trajectory, 2, false, false, this);
            if (target == Dungeon.hero){
                Dungeon.hero.interrupt();
            }
            rockCenter = trajectory.path.get(Math.min(trajectory.dist, 2));

            //knock back 1 tile if there's 1 tile of space
        } else if (fieldOfView[target.pos] && Dungeon.level.distance(pos, target.pos) == 2) {
            int oppositeAdjacent = target.pos + (target.pos - pos);
            Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(target, trajectory, 1, false, false, this);
            if (target == Dungeon.hero){
                Dungeon.hero.interrupt();
            }
            rockCenter = trajectory.path.get(Math.min(trajectory.dist, 1));

            //otherwise no knockback
        } else {
            rockCenter = target.pos;
        }

        int safeCell;
        do {
            safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (safeCell == pos
                || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                || (Blob.volumeAt(safeCell, CavesBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

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
                    rockCells.add(pos);
                }
                pos++;
            }
        }
        for (int i : rockCells){
            sprite.parent.add(new TargetedCell(i, 0xFF0000));
        }
        //don't want to overly punish players with slow move or attack speed
        Buff.append(this, DM300.FallingRockBuff.class, GameMath.gate(TICK, (int)Math.ceil(target.cooldown()), 3*TICK)).setRockPositions(rockCells);

    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

}
