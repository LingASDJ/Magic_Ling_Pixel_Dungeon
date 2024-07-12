package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.FALSEPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.TRUEPosition;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeamTowerAdbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShopLimitLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDICLRPRO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class FireMagicDied extends Boss implements Callback, Hero.Doom {

    private static final float TIME_TO_ZAP = 6f;

    {
        //TODO 喜欢返程抢劫 2024血量完全体浊焰魔女莲娜小姐来教你做人了
        HP = HT = (Statistics.amuletObtained || Statistics.RandMode) ? 2024 : 270 * (Dungeon.depth/5);
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
    }



    private int pumpedUp = 0;

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

    protected static boolean isSummonedByDK(Mob m){
        return (m instanceof FireMagicDied.ColdGuradA || m instanceof FireMagicDied.ColdGuradB || m instanceof FireMagicDied.ColdGuradC);
    }

    private int aliveSummons(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                count++;
            }
        }
        return count;
    }

    //读取召唤
    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
            if (m.alignment == alignment && (m instanceof ColdGurad || m instanceof SRPDICLRPRO)){
                subjects.add(m);
            }
        }
        return subjects;
    }

    private boolean lifeLinkSubject(){
        Mob furthest = null;

        for (Mob m : getSubjects()){
            boolean alreadyLinked = false;
            for (LifeLink l : m.buffs(LifeLink.class)){
                if (l.object == id()) alreadyLinked = true;
            }
            if (!alreadyLinked) {
                if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                    furthest = m;
                }
            }
        }

        if (furthest != null) {
            Buff.append(furthest, LifeLink.class, 100f).object = id();
            Buff.append(this, LifeLink.class, 100f).object = furthest.id();
            yell(Messages.get(this, "lifelink_" + Random.IntRange(1, 2)));
            Buff.affect(this, Healing.class).setHeal(5, 0f, 6);
            sprite.parent.add(new Beam.HealthRay(sprite.destinationCenter(), furthest.sprite.destinationCenter()));
            return true;

        }
        return false;
    }

    int preHP = HP;
    private int beamCD = 23;
    private float summonCooldown = 0;
    private float abilityCooldown = 6;

    private static final int MIN_ABILITY_CD = 7;
    private static final int MAX_ABILITY_CD = 12;
    private ArrayList<Integer> targetedCells = new ArrayList<>();
    private int lastHeroPos;

    private static final int MIN_COOLDOWN = 7;
    private static final int MAX_COOLDOWN = 11;
    private int summonsMade = 0;

    private int lastAbility = 0;
    private static final int NONE = 0;
    private static final int LINK = 1;
    private static final int TELE = 2;
    private static final int ENRAGE = 3;
    private static final int DEATHRATTLE = 4;
    private static final int SACRIFICE = 5;
    private static final int SUMMON = 6;

    private boolean teleportSubject(){
        if (enemy == null) return false;

        Mob furthest = null;

        for (Mob m : getSubjects()){
            if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
                furthest = m;
            }
        }

        if (furthest != null){

            float bestDist;
            int bestPos = pos;

            Ballistica trajectory = new Ballistica(enemy.pos, pos, Ballistica.STOP_TARGET);
            int targetCell = trajectory.path.get(trajectory.dist);
            //if the position opposite the direction of the hero is open, go there
            if (Actor.findChar(targetCell) == null && !Dungeon.level.solid[targetCell]){
                bestPos = targetCell;

                //Otherwise go to the neighbour cell that's open and is furthest
            } else {
                bestDist = Dungeon.level.trueDistance(pos, enemy.pos);

                for (int i : PathFinder.NEIGHBOURS8){
                    if (Actor.findChar(pos+i) == null
                            && !Dungeon.level.solid[pos+i]
                            && Dungeon.level.trueDistance(pos+i, enemy.pos) > bestDist){
                        bestPos = pos+i;
                        bestDist = Dungeon.level.trueDistance(pos+i, enemy.pos);
                    }
                }
            }

            Actor.add(new Pushing(this, pos, bestPos));
            pos = bestPos;

            //find closest cell that's adjacent to enemy, place subject there
            bestDist = Dungeon.level.trueDistance(enemy.pos, pos);
            bestPos = enemy.pos;
            for (int i : PathFinder.NEIGHBOURS8){
                if (Actor.findChar(enemy.pos+i) == null
                        && !Dungeon.level.solid[enemy.pos+i]
                        && Dungeon.level.trueDistance(enemy.pos+i, pos) < bestDist){
                    bestPos = enemy.pos+i;
                    bestDist = Dungeon.level.trueDistance(enemy.pos+i, pos);
                }
            }

            if (bestPos != enemy.pos) ScrollOfTeleportation.appear(furthest, bestPos);
            yell(Messages.get(this, "teleport_" + Random.IntRange(1, 2)));
            return true;
        }
        return false;
    }

    private void enrageSubject(){
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                Buff.affect(m, Healing.class).setHeal(42, 0f, 6);
            }
        }
        new Flare(5, 32).color(0xFF6060, false).show(sprite, 1.5f);
        yell(Messages.get(this,"buff_all"));
    }

    private void sacrificeSubject(){
        Buff.affect(this,  DwarfMaster.SacrificeSubjectListener.class, 3f);
        new Flare(6, 32).color(0xFF22FF, false).show(sprite, 1.5f);
        yell(Messages.get(this, "sacrifice"));
    }

    private void deathRattleSubject(){
        int count = 0;
        for(Mob m: Dungeon.level.mobs.toArray(new Mob[0])){
            if(isSummonedByDK(m)){
                //theoretically multiple death rattles is ok but,
                //problems: 1. vfx  2. mobs at one tile
                if(m.buff(DwarfMaster.DeathRattleSummon.class)==null) {
                    Buff.affect(m, DwarfMaster.DeathRattleSummon.class).setId(Random.chances(new float[]{9f, 4f, 4f}));
                    count++;
                }
            }
            if(count>=4) break;
        }
        new Flare(7, 32).color(0x303030, false).show(sprite, 1.5f);
        yell(Messages.get(this,"death_rattle"));
    }

    private static float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f};
    private void rollForAbility(){
        lastAbility = Random.chances(chanceMap);
        chanceMap[lastAbility] /= 4f;
        if(chanceMap[lastAbility] < 0.0001f) resetChanceMap();
    }
    private void resetChanceMap(){
        for(int i=1;i<chanceMap.length;++i){
            chanceMap[i]=100f;
        }
        chanceMap[0]=0f;
    }


    @Override
    public boolean act() {
        int healInc = 1;
        if (phase == 1 && HP <= HT/3) {
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    if (Dungeon.level.water[pos] && HP < HT) {
                        HP += healInc;

                        if (Dungeon.level.heroFOV[pos]) {
                            sprite.emitter().burst(Speck.factory(Speck.HEALING), healInc);
                        }
                        if (HP * 2 > HT) {
                            BossHealthBar.bleed(false);
                            ((FireMagicGirlSprite) sprite).spray(false);
                            HP = Math.min(HP, HT);
                        }
                    }
                    return true;
                }
            });
            //actScanning();

        }else if (phase == 2 && HP < HT/2) {
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);

                    if (paralysed > 0){
                        spend(TICK);
                        return true;
                    }

                    if (abilityCooldown <= 0){
                        int alive = aliveSummons();
                        //NEVER use skills if no mobs alive.
                        if(alive == 0) {
                            lastAbility = NONE;
                            //summon faster when no ability is available.
                            summonCooldown -= 2f;
                        }
                        else {
                            do {
                                rollForAbility();
                            } while ((lastAbility == ENRAGE || lastAbility == SACRIFICE) && alive < 2);
                        }
                        if(buff(DwarfMaster.SacrificeSubjectListener.class)!= null){
                            lastAbility = NONE;
                            //cd faster while prepare to sacrifice
                            abilityCooldown --;
                        }

                        if (lastAbility == LINK && lifeLinkSubject()){
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        } else if (lastAbility == TELE && teleportSubject()) {
                            lastAbility = TELE;
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        }else if(lastAbility == ENRAGE){
                            enrageSubject();
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                            return true;
                        }else if(lastAbility == DEATHRATTLE){
                            deathRattleSubject();
                            lastAbility = DEATHRATTLE;
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }else if(lastAbility == SACRIFICE){
                            sacrificeSubject();
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }else if(lastAbility == SUMMON){
                            abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                            spend(TICK);
                        }

                    } else {
                        abilityCooldown--;
                    }
                    return true;
                }
            });

        } else if (phase == 3){
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    return true;
                }
            });

        } else if (phase == 4 && buffs(FireMagicDied.Summoning.class).size() < 4){
            Actor.add(new Actor() {

                {
                    actPriority = VFX_PRIO;
                }

                @Override
                protected boolean act() {
                    Actor.remove(this);
                    return true;
                }
            });
            return true;
        }
        //actScanning();
        if (Dungeon.level.water[pos] && HP < HT) {
            HP += healInc;

            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null) lock.removeTime(healInc * 2);

            if (Dungeon.level.heroFOV[pos]) {
                sprite.emitter().burst(Speck.factory(Speck.HEALING), healInc);
            }
            if (HP * 2 > HT) {
                BossHealthBar.bleed(false);
                ((FireMagicGirlSprite) sprite).spray(false);
                HP = Math.min(HP, HT);
            }
        }

        return super.act();
    }
    private static final String PHASE = "phase";
    private static final String ABILITY_CD = "ability_cd";
    private static final String SUMMON_CD = "summon_cd";
    private static final String SUMMONS_MADE = "summons_made";
    private int wave=0;
    private static final String TARGETED_CELLS = "targeted_cells";

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
    }

//    @Override
//    protected boolean canAttack( Char enemy ) {
//        if (pumpedUp > 0) {
//            //we check both from and to in this case as projectile logic isn't always symmetrical.
//            //this helps trim out BS edge-cases
//            return Dungeon.level.distance(enemy.pos, pos) <= 2
//                    && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
//                    && new Ballistica(enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
//        } else if (HP < HT / 2) {
//            return Dungeon.level.distance(enemy.pos, pos) <= 3
//                    && new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos
//                    && new Ballistica(enemy.pos, pos, Ballistica.PROJECTILE).collisionPos == pos;
//        } else {
//            return super.canAttack(enemy);
//        }
//    }

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

            int dmg = Random.NormalIntRange(2+Dungeon.depth, 5+Dungeon.depth );

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
        if(HP > HT/2){
            if (Random.Int( 3 ) == 0) {
                Buff.affect( enemy, HalomethaneBurning.class ).reignite( enemy, 7f );
                enemy.sprite.burst( 0x000000, 5 );
            }
        } else if (HP < HT/2) {
            if (Random.NormalFloat( 0,100 ) <= 10) {
                GLog.n( Messages.get(FireMagicDied.class, "died_kill",Dungeon.hero.name()) );
                bolt(damage/2,enemy);
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
        return super.isAlive() || Dungeon.level.mobs.contains(this) && HP>0;
    }
    @Override
    public void damage(int dmg, Object src) {

        if (!Dungeon.level.mobs.contains(this)){
            return;
        }

        if (!BossHealthBar.isAssigned()){
            BossHealthBar.assignBoss( this );
        }
        boolean bleeding = (HP*2 <= HT);

        super.damage(dmg, src);
        int hpBracket = HT / 8;

        int curbracket = hpBracket == 0 ? 1 : HP / hpBracket;

        int beforeHitHP = HP;


        //cannot be hit through multiple brackets at a time
        if (HP <= (curbracket-1)*hpBracket){
            HP = (curbracket-1)*hpBracket + 1;
        }

        int newBracket =  HP / hpBracket;
        dmg = beforeHitHP - HP;



        if ((HP*2 <= HT) && !bleeding){
            BossHealthBar.bleed(true);
            ((FireMagicGirlSprite)sprite).spray(true);
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);

        if (phase == 1) {
            int dmgTaken = preHP - HP;
            abilityCooldown -= dmgTaken/8f;
            summonCooldown -= dmgTaken/8f;
            if (HP <= HT/2) {
                for (int i : CryStalPosition) {
                    Buff.append(hero, BeamTowerAdbility.class).towerPos = i;
                }
                //properties.add(Property.IMMOVABLE);

                ////doYogLasers();
                sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
                Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
                phase = 2;
                Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
                int w = Dungeon.level.width();
                int dx = enemy.pos % w - pos % w;
                int dy = enemy.pos / w - pos / w;
                int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
                direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
                Buff.affect(this, FireMagicDied.YogScanHalf.class).setPos(pos, direction);;
                beamCD = 40 + 8 - (phase == 10 ? 38 : 0);
                sprite.showStatus(0xff0000, Messages.get(this, "dead"));
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
                Buff.affect(this, DwarfMaster.DKBarrior.class).setShield(12*25+HT/3);
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
            if(Statistics.amuletObtained || Statistics.RandMode){
                for (int i : CryStalPosition2) {
                    Buff.append(hero, BeamTowerAdbility.class).towerPos = i;
                    ColdGuradA csp = new ColdGuradA();
                    csp.pos = i;
                    GameScene.add(csp);
                }
            }
            HP = HT/2;
            //T3 阶段
            CrystalLingTower abc = new CrystalLingTower();
            abc.pos = TRUEPosition;
            GameScene.add(abc);

            this.pos = FALSEPosition;

            Buff.affect(this, DwarfMaster.DKBarrior.class).setShield(HT/4);

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
            summonsMade = 0;
            sprite.idle();
            Char enemy = (this.enemy == null ? Dungeon.hero : this.enemy);
            int w = Dungeon.level.width();
            int dx = enemy.pos % w - pos % w;
            int dy = enemy.pos / w - pos / w;
            int direction = 2 * (Math.abs(dx) > Math.abs(dy) ? 0 : 1);
            direction += (direction > 0 ? (dy > 0 ? 1 : 0) : (dx > 0 ? 1 : 0));
            Buff.affect(this, FireMagicDied.YogScanHalf.class).setPos(pos, direction);;
            beamCD = 40 + 8 - (phase == 10 ? 38 : 0);
            sprite.showStatus(0xff0000, Messages.get(this, "dead"));
            Buff.affect(this, ChampionEnemy.Halo.class);
            Buff.affect(this, ChampionEnemy.AntiMagic.class);
            Buff.affect(this, Adrenaline.class, 100f);
            Buff.affect(this, RoseShiled.class, 20f);
        } else if (phase == 3 && preHP > 10 && HP <= 20){
            yell( Messages.get(this, "losing") );
            die(Dungeon.hero);
            Dungeon.hero.interrupt();
            GameScene.flash(0x80FFFFFF);
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.fadeOut(5f, new Callback() {
                        @Override
                        public void call() {
                            Music.INSTANCE.end();
                        }
                    });
                }
            });
        } else if (newBracket != curbracket) {
        //let full attack action complete first
        Actor.add(new Actor() {

            {
                actPriority = VFX_PRIO;
            }

            @Override
            protected boolean act() {
                Actor.remove(this);
                return true;
            }
        });
    }

    }

    @Override
    public void die( Object cause ) {
        if(Statistics.bossRushMode){
            GetBossLoot();
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
            if (mob instanceof FireMagicDied.ColdGuradA ||  mob instanceof SRPDICLRPRO ||mob instanceof Skeleton||mob instanceof DM100|| mob instanceof BlackHost|| mob instanceof Warlock|| mob instanceof Monk|| mob instanceof CrystalDiedTower|| mob instanceof CrystalLingTower) {
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

        BossHealthBar.assignBoss(this);

       playBGM(Assets.BGM_SHOP, true);
        yell( Messages.get(this, "notice") );
        //summon();
    }

    @Override
    public void onDeath() {
        Dungeon.fail( getClass() );
        GLog.n( Messages.get(this, "ondeath") );
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
                    Effects.Type.RED_CHAIN)
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
            target.sprite.parent.add(new ScanningBeam(Effects.Type.RED_CHAIN, BallisticaReal.STOP_TARGET,
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

    public class Summoning extends Buff {

        private int delay;
        private int pos;
        private Class<?extends Mob> summon;

        private Emitter particles;

        public int getPos() {
            return pos;
        }

        @Override
        public boolean act() {

            delay--;
            if (delay <= 0){
                if (summon == FireMagicDied.ColdGuradA.class){
                    particles.burst(ShadowParticle.CURSE, 10);
                    Sample.INSTANCE.play(Assets.Sounds.CURSED);
                } else if (summon == FireMagicDied.ColdGuradB.class){
                    particles.burst( ShadowParticle.MISSILE, 10 );
                    Sample.INSTANCE.play(Assets.Sounds.BURNING);
                } else {
                    particles.burst(EnergyParticle.FACTORY, 10);
                    Sample.INSTANCE.play(Assets.Sounds.READ);
                }
                particles = null;

                if (Actor.findChar(pos) != null){
                    ArrayList<Integer> candidates = new ArrayList<>();
                    for (int i : PathFinder.NEIGHBOURS8){
                        if (Dungeon.level.passable[pos+i] && Actor.findChar(pos+i) == null){
                            candidates.add(pos+i);
                        }
                    }
                    if (!candidates.isEmpty()){
                        pos = Random.element(candidates);
                    }
                }

                if (Actor.findChar(pos) == null) {
                    Mob m = Reflection.newInstance(summon);
                    m.pos = pos;
                    m.maxLvl = -2;
                    GameScene.add(m);
                    m.state = m.HUNTING;
                    if (((FireMagicDied)target).phase == 2){
                        Buff.affect(m, FireMagicDied.KingDamager.class);
                    }
                } else {
                    if (((FireMagicDied)target).phase == 2 && HP > HT/2){
                        target.damage(30, new FireMagicDied.KingDamager());
                    }
                }

                detach();
            }

            spend(TICK);
            return true;
        }

        @Override
        public void fx(boolean on) {
            if (on && particles == null) {
                particles = CellEmitter.get(pos);

                if (summon == FireMagicDied.ColdGuradA.class){
                    particles.pour(ShadowParticle.UP, 0.1f);
                } else if (summon == FireMagicDied.ColdGuradB.class){
                    particles.pour(ElmoParticle.FACTORY, 0.1f);
                } else {
                    particles.pour(Speck.factory(Speck.RATTLE), 0.1f);
                }

            } else if (!on && particles != null) {
                particles.on = false;
            }
        }

        private static final String DELAY = "delay";
        private static final String POS = "pos";
        private static final String SUMMON = "summon";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DELAY, delay);
            bundle.put(POS, pos);
            bundle.put(SUMMON, summon);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            delay = bundle.getInt(DELAY);
            pos = bundle.getInt(POS);
            summon = bundle.getClass(SUMMON);
        }
    }

    //亡魂显现
    public static class ColdGuradA extends ColdGurad {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;

            HP=HT=10;
        }

        @Override
        protected boolean act() {
            return super.act();
        }

        @Override
        public int damageRoll(){
            boolean str = buff(FireMagicDied.StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
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

    public static class ColdGuradB extends Skeleton {
        {
            state = HUNTING;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int damageRoll(){
            boolean str = buff(FireMagicDied.StrengthEmpower.class)!=null;
            return Math.round(super.damageRoll()*(str? 1.5f:1f));
        }
    }

    public static class ColdGuradC extends SRPDICLRPRO {
        {
            state = HUNTING;
            this.HT = 40;
            this.HP = 40;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
        }
        @Override
        public int attackProc(Char enemy, int damage){
            if(Random.Int(10)==0) {
                Buff.affect(enemy, Degrade.class, 2f);
            }
            return super.attackProc(enemy, damage);
        }
        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 10, 15 );
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
                    m.damage(30, this);
                }
            }
        }
    }

}
