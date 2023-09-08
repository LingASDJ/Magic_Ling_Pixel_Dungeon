package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.FrostFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.NewDM720;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.custom.AncityArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SakaMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.levels.AncientMysteryCityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.CaveTwoBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SakaFishBossSprites;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

/**萨卡班甲鱼 Boss 难度：3.5级 */
public class SakaFishBoss extends Boss {
    private int leapPos = -1;
    private float leapCooldown = 0;
    private int lastEnemyPos = -1;

    private Ballistica beam;
    private int beamTarget = -1;
    private int beamCooldown;
    public boolean beamCharged;


    /**隐藏Boss 萨卡班甲鱼*/
    {
        spriteClass = SakaFishBossSprites.class;
        initBaseStatus(19, 25, 6, 0, 480, 5, 12);
        state = SLEEPING;
        initProperty();
        HUNTING = new Hunting();
        baseSpeed = 1.75f;
        initStatus(76);
        HP=480;
        defenseSkill = 10;
        HT=480;
        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        properties.add(Property.ICY);
        properties.add(Property.ELECTRIC);
        properties.add(Property.FIERY);

        viewDistance = 30;
    }

    private int pumpedUp = 0;
    private int healInc = 1;

    public void damage(int dmg, Object src) {
        if (!Dungeon.level.mobs.contains(this)){
            return;
        }
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        if (level==AncientMysteryCityBossLevel.State.FALL_BOSS){
            dmg = 10 + (int)(Math.sqrt(2*(dmg - 4) + 1) - 1)/2;
        } else if (dmg >= 20){
            dmg = 14 + (int)(Math.sqrt(8*(dmg - 4) + 1) - 1)/2;
        }

        int hpBracket = HT / 8;

        int beforeHitHP = HP;
        super.damage(dmg, src);
        dmg = beforeHitHP - HP;

        //tengu cannot be hit through multiple brackets at a time
        if ((beforeHitHP/hpBracket - HP/hpBracket) >= 2){
            HP = hpBracket * ((beforeHitHP/hpBracket)-1) + 1;
        }

        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            lock.addTime(dmg*3f);
        }


        //phase 1 of the fight is over
        if (HP <= HT/2 && level==AncientMysteryCityBossLevel.State.TWO_BOSS){
            HP = (HT/2);
            yell(Messages.get(this, "interesting"));
            ((AncientMysteryCityBossLevel)Dungeon.level).progress();
            BossHealthBar.bleed(true);
        }
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        return level==AncientMysteryCityBossLevel.State.END_BOSS;
    }

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
    public int damageRoll() {
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        if(level==AncientMysteryCityBossLevel.State.FALL_BOSS)
            return Random.NormalIntRange(40, 75);
        else
            return Random.NormalIntRange(30, 40);
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
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        return level == AncientMysteryCityBossLevel.State.FALL_BOSS ? 10 : 40;
    }


    public void activate(){
        ((SakaFishBossSprites) sprite).activate();
    }

    @Override
    protected boolean getCloser( int target ) {
        if (state == HUNTING) {
            return enemySeen && getFurther( target );
        } else {
            return super.getCloser( target );
        }
    }

    @Override
    protected boolean act() {
        AiState lastState = state;

        boolean result = super.act();
        if (paralysed <= 0) leapCooldown --;

        //if state changed from wandering to hunting, we haven't acted yet, don't update.
        if (!(lastState == WANDERING && state == HUNTING)) {
            if (enemy != null) {
                lastEnemyPos = enemy.pos;
            } else {
                lastEnemyPos = hero.pos;
            }
        }
        if (beamCharged && state != HUNTING){
            beamCharged = false;
            sprite.operate(this.pos);
        }
        if (beam == null && beamTarget != -1) {
            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
            sprite.turnTo(pos, beamTarget);
        }
        if (beamCooldown > 0)
            beamCooldown--;
        return result;
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        if (beamCooldown > 0) {
            return super.doAttack(enemy);
        } else if (!beamCharged){
            ((SakaFishBossSprites)sprite).charge( enemy.pos );

            spend( level == AncientMysteryCityBossLevel.State.FALL_BOSS ? attackDelay() : attackDelay()*2f );
            beamCharged = true;
            return true;
        } else if(HP*2>=HT) {

            spend( level == AncientMysteryCityBossLevel.State.FALL_BOSS ? attackDelay() : attackDelay()*3f );

            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos] ) {
                sprite.zap( beam.collisionPos );
                return false;
            } else {
                sprite.idle();
                deathGaze();
                return true;
            }
        } else {
            spend( level == AncientMysteryCityBossLevel.State.FALL_BOSS ? attackDelay() : attackDelay()*3f );

            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos] ) {
                sprite.zap( beam.collisionPos );
                return false;
            } else {
                sprite.idle();
                deathGaze();
                return true;
            }
        }

    }

    @Override
    public void die( Object cause ) {

        super.die( cause );


            Dungeon.level.unseal();

            GameScene.bossSlain();
            Dungeon.level.drop( new CrystalKey( Dungeon.depth ), pos ).sprite.drop();
            Dungeon.level.drop( new AncityArmor(), pos ).sprite.drop();
            //60% chance of 2 blobs, 30% chance of 3, 10% chance for 4. Average of 2.5
            int meets = Random.chances(new float[]{0, 0, 6, 3, 1});
            for (int i = 0; i < meets; i++){
                int ofs;
                do {
                    ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (!Dungeon.level.passable[pos + ofs]);
                Dungeon.level.drop( new SakaMeat(), pos + ofs ).sprite.drop( pos );
            }

            Dungeon.level.drop( new WaterSoul(), pos ).sprite.drop();
            Dungeon.level.drop( new SakaFishSketon(), pos ).sprite.drop();
            Dungeon.level.drop( new WaterSoul(), pos ).sprite.drop();

            Badges.KILLSAKA();

            yell( Messages.get(this, "defeated") );


    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            //Dungeon.level.seal();
            yell(Messages.get(this, "notice"));
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
            activate();
        }
    }

    private final String PUMPEDUP = "pumpedup";
    private final String HEALINC = "healinc";

    private static final String LAST_ENEMY_POS = "last_enemy_pos";
    private static final String LEAP_POS = "leap_pos";
    private static final String LEAP_CD = "leap_cd";

    private static final String BEAM_TARGET     = "beamTarget";
    private static final String BEAM_COOLDOWN   = "beamCooldown";
    private static final String BEAM_CHARGED    = "beamCharged";

    //二阶段
    private static final String TO_ATTACK   = "toattack";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        bundle.put( BEAM_TARGET, beamTarget);
        bundle.put( BEAM_COOLDOWN, beamCooldown );
        bundle.put( BEAM_CHARGED, beamCharged );
        bundle.put( PUMPEDUP , pumpedUp );
        bundle.put( HEALINC, healInc );
        bundle.put(LAST_ENEMY_POS, lastEnemyPos);
        bundle.put(LEAP_POS, leapPos);
        bundle.put(LEAP_CD, leapCooldown);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        pumpedUp = bundle.getInt( PUMPEDUP );
        if (state != SLEEPING) BossHealthBar.assignBoss(this);
        if ((HP*2 <= HT)) BossHealthBar.bleed(true);

        //if check is for pre-0.9.3 saves
        healInc = bundle.getInt(HEALINC);

        if (bundle.contains(BEAM_TARGET))
            beamTarget = bundle.getInt(BEAM_TARGET);
        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamCharged = bundle.getBoolean(BEAM_CHARGED);

        lastEnemyPos = bundle.getInt(LAST_ENEMY_POS);
        leapPos = bundle.getInt(LEAP_POS);
        leapCooldown = bundle.getFloat(LEAP_CD);
    }

    public void dropRocks( Char target ) {

        hero.interrupt();
        final int rockCenter;
        if (Dungeon.level.adjacent(pos, target.pos)){
            int oppositeAdjacent = target.pos + (target.pos - pos);
            Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(target, trajectory, 20, false, false, getClass());
            if (target == hero){
                hero.interrupt();
            }
            rockCenter = trajectory.path.get(Math.min(trajectory.dist, 200));
        } else {
            rockCenter = target.pos;
        }

        Actor a = new Actor() {

            {
                actPriority = HERO_PRIO+1;
            }
            private static final float TIME_TO_ZAP	= 1f;

            @Override
            protected boolean act() {

                //pick an adjacent cell to the hero as a safe cell. This cell is less likely to be in a wall or containing hazards
                int safeCell;
                do {
                    safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (safeCell == pos
                        || (Dungeon.level.solid[safeCell] && Random.Int(2) == 0)
                        || (Blob.volumeAt(safeCell, CaveTwoBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

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
                            GameScene.add(Blob.seed(pos, 1, NewDM720.FallingRocks.class));
                        }
                        pos++;
                    }
                }
                Actor.remove(this);
                return true;
            }
        };
        Actor.addDelayed(a, Math.min(target.cooldown(), 3*TICK));

    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {

            if (leapPos != -1){

                leapCooldown = Random.NormalIntRange(2, 4);
                Ballistica b = new Ballistica(pos, leapPos, Ballistica.STOP_TARGET | Ballistica.STOP_SOLID);

                //check if leap pos is not obstructed by terrain
                if (rooted || b.collisionPos != leapPos){
                    leapPos = -1;
                    return true;
                }

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
                    sprite.dirtcar(pos, leapPos, new Callback() {
                        @Override
                        public void call() {
                            AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
                            if (leapVictim != null && alignment != leapVictim.alignment){
                                enemy.damage( Random.NormalIntRange( 40, 60 ), this );
                                if(level == AncientMysteryCityBossLevel.State.FALL_BOSS){
                                    //三阶段 魔法风暴
                                    FishStorm(sprite.ch);
                                }
                                Sample.INSTANCE.play(Assets.Sounds.ROCKS);
                                leapVictim.sprite.flash();
                                Sample.INSTANCE.play(Assets.Sounds.HIT);
                            }

                            Char ch = hero;
                            if(hero != null){
                                if (!ch.isAlive()) {
                                    Dungeon.fail( getClass() );
                                    GLog.n( Messages.get(SakaFishBoss.class, "dictcar_kill"),Dungeon.hero.name() );
                                }
                            }

                            if (endPos != leapPos){
                                Actor.addDelayed(new Pushing(SakaFishBoss.this, leapPos, endPos), -1);
                            }

                            pos = endPos;
                            leapPos = -1;
                            sprite.operate(pos);
                            Dungeon.level.occupyCell(SakaFishBoss.this);
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
                    target = Dungeon.level.randomDestination( SakaFishBoss.this );
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
                        spend(GameMath.gate(TICK, enemy.cooldown(), 3*TICK));
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[leapPos]){
                            GLog.w(Messages.get(SakaFishBoss.this, "leap"));
                            sprite.parent.addToBack(new TargetedCell(leapPos, 0xFF0000));
                            ((SakaFishBossSprites)sprite).leapPrep( leapPos );
                            hero.interrupt();
                        }
                        return true;
                    }
                }

                int oldPos = pos;
                if (target != -1 && getCloser( target )) {

                    spend( 1f );
                    return moveSprite( oldPos,  pos );

                } else {
                    spend(TICK);
                    if (!enemyInFOV) {
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination( SakaFishBoss.this );
                    }
                    return true;
                }
            }
        }

    }
    public static class DeathGaze{}
    public void deathGaze(){
        if (!beamCharged || beamCooldown > 0 || beam == null)
            return;
        AncientMysteryCityBossLevel.State level = ((AncientMysteryCityBossLevel)Dungeon.level).pro();
        beamCharged = false;
        beamCooldown = Random.IntRange(4, 6);

        boolean terrainAffected = false;

        for (int pos : beam.subPath(1, beam.dist)) {

            if (Dungeon.level.flamable[pos]) {

                Dungeon.level.destroy( pos );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

            Char ch = Actor.findChar( pos );
            if (ch == null) {
                continue;
            }

            if (hit( this, ch, true )) {
                ch.damage( Random.NormalIntRange( 30, 60 ), new DeathGaze() );
                if(level == AncientMysteryCityBossLevel.State.FALL_BOSS){
                    dropRocks(enemy);
                }

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center( pos ).burst( RainbowParticle.BURST, Random.IntRange( 1, 2 ) );
                }

                if (!ch.isAlive() && ch == hero) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(this, "deathgaze_kill"),Dungeon.hero.name() );
                }
            } else {
                ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        beam = null;
        beamTarget = -1;
    }



    //萨卡班甲鱼魔法风暴
    public void FishStorm(Char ch){
        Ballistica aim;
        aim = new Ballistica(ch.pos, ch.pos - 1, Ballistica.WONT_STOP);
        int projectileProps = Ballistica.STOP_SOLID | Ballistica.STOP_TARGET;
        int aoeSize = 8;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);

        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FROSTFIRE,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
            if( Dungeon.level.water[pos] || Random.Int(10)==0){
                GameScene.add(Blob.seed(ray.path.get(ray.dist), 30, FrostFire.class));
                Level.set(ray.path.get(ray.dist), Terrain.EMPTY);
                GameScene.updateMap( ray.path.get(ray.dist) );
            } else {
                Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
            }
        }
    }

}
