package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;


import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.CryStalPosition2;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.FALSEPosition;
import static com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel.TRUEPosition;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BeamTowerAdbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HellBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicGirlDebuff.MagicGirlSayTimeLast;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShopLimitLock;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BlackHost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ColdGurad;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDICLRPRO;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Skeleton;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.SmallLeafHardDungeon;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaReal;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.timing.VirtualActor;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.BeamCustom;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ScanningBeam;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.ShopBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FireMagicGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
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

    private static final float TIME_TO_ZAP = 4f;

    {
        HP = HT = Statistics.bossRushMode && !Statistics.amuletObtained ? 270 * (Dungeon.depth/5) : (Statistics.amuletObtained || Statistics.RandMode && Dungeon.depth == 20) ? 2024 : 270 * (Statistics.deepestFloor/5);
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

    //莲娜愤怒姿态时的特殊技能判定：true 时解锁「召唤系 + 喷火」怒之技
    public boolean VeryAngry = true;

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

    //===== 怒之技：召唤系（旧版废弃方法复活）=====
    private static final int MIN_COOLDOWN = 7;
    private static final int MAX_COOLDOWN = 11;

    private int lastAbility = 0;
    private static final int NONE = 0;
    private static final int LINK = 1;
    private static final int TELE = 2;
    private static final int ENRAGE = 3;
    private static final int DEATHRATTLE = 4;
    private static final int SACRIFICE = 5;
    private static final int SUMMON = 6;
    private static final int FIREBREATH = 7;
    private static final int HONGLIAN = 8;   //红莲真火（仅第二阶段）
    private static final int SKYFIRE = 9;    //天火（仅第三阶段）

    //场上至多存在的召唤物数量
    private static final int MAX_SUMMONS = 6;

    private static final float[] chanceMap = {0f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f};

    //===== 喷火蓄力（预警 + 施法动画）=====
    private int fireBreathCharge = 0;               //>0 表示正在蓄力，倒计时结束后才释放怒焰
    private ArrayList<Integer> fireBreathCells;     //蓄力时锁定的即将被点燃的格子
    private int fireBreathTarget = -1;              //蓄力时锁定的目标格（用于释放时的弹道动画）

    //===== 暴怒姿态三阶段：鬼磷精英 / 红莲真火 / 天火 ===== 
    private int redLotusCharge = 0;                                       //红莲真火蓄力回合数（>0 表示蓄力中）
    private int redLotusCooldown = 0;                                     //红莲真火冷却回合数（40-80，冷却期内不再触发）
    private ArrayList<Integer> pendingCorePositions = null;               //读档暂存的核心位置（首回合重挂引用）
    private ArrayList<TurbidFlameCore> redLotusCores = new ArrayList<>(); //红莲真火的两个浊焰核心
    private int skyFireCharge = 0;                                        //天火蓄力回合数（>0 表示蓄力中）
    private ArrayList<Integer> skyFireCells;                              //天火三角警告区格子
    private float skyFireAngle = 0f;                                      //天火方向


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

        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FROST,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
    }

    //读取召唤物（莲娜的烈焰守卫）
    private HashSet<Mob> getSubjects(){
        HashSet<Mob> subjects = new HashSet<>();
        for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])){
            if (m.alignment == alignment && (m instanceof ColdGurad || m instanceof SRPDICLRPRO || m instanceof SRPDHBLR || m instanceof ColdGuradB)){
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

    //献祭所有召唤物：每个召唤物在爆炸时对周围敌人造成物理伤害
    private void sacrificeSubject(){
        for (Mob m : getSubjects()){
            for (int i : PathFinder.NEIGHBOURS8){
                CellEmitter.center(i+m.pos).burst(Speck.factory(Speck.BONE), 3);
                Char ch = Actor.findChar(i+m.pos);
                if (ch != null){
                    if (ch.alignment != Alignment.ENEMY){
                        ch.damage(Random.IntRange(25, 36), m, DamageType.PHYSICAL);
                        if (ch == Dungeon.hero && !ch.isAlive()){
                            Dungeon.fail(getClass());
                        }
                    }
                }
            }
            CellEmitter.center(m.pos).burst(Speck.factory(Speck.BONE), 6);
            m.die(this);
            Dungeon.level.mobs.remove(m);
        }
        new Flare(6, 32).color(0xFF22FF, false).show(sprite, 1.5f);
        yell(Messages.get(this, "sacrifice"));
    }

    private void rollForAbility(){
        for (int tries = 0; tries < 10; tries++){
            lastAbility = Random.chances(chanceMap);
            if (isAbilityAllowed(lastAbility)) {
                chanceMap[lastAbility] /= 4f;
                if(chanceMap[lastAbility] < 0.0001f) resetChanceMap();
                return;
            }
        }
        lastAbility = NONE;
    }

    //怒之技按阶段限定：红莲真火仅第二阶段且需度过冷却、天火仅第三阶段
    private boolean isAbilityAllowed(int ability){
        if (ability == HONGLIAN) return phase == 2 && redLotusCooldown <= 0;
        if (ability == SKYFIRE) return phase == 3;
        return true;
    }
    private void resetChanceMap(){
        for(int i=1;i<chanceMap.length;++i){
            chanceMap[i]=100f;
        }
        chanceMap[0]=0f;
    }

    //在自身周围寻找一个可召唤的空位
    private int findSpawnPos(){
        int w = Dungeon.level.width();
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int y = -3; y <= 3; y++){
            for (int x = -3; x <= 3; x++){
                int c = pos + y*w + x;
                if (!Dungeon.level.insideMap(c)) continue;
                if (Dungeon.level.solid[c]) continue;
                if (Actor.findChar(c) != null) continue;
                candidates.add(c);
            }
        }
        if (candidates.isEmpty()) return -1;
        return candidates.get(Random.Int(candidates.size()));
    }

    //真正召唤一只烈焰守卫（旧版 SUMMON 空壳的实体化）
    private boolean summonSubject(){
        if (getSubjects().size() >= MAX_SUMMONS) return false;
        //ColdGurad（雪凛守卫）数量上限 3，达到上限后只召唤 SRPDHBLR（ColdGuradC）
        int guardCount = 0;
        for (Mob m : getSubjects()){
            if (m instanceof ColdGurad) guardCount++;
        }
        Class<? extends Mob> type;
        if (guardCount >= 3){
            type = ColdGuradC.class;
        } else {
            type = (Random.Int(2) == 0) ? ColdGuradB.class : ColdGuradC.class;
        }
        int spawnPos = findSpawnPos();
        if (spawnPos == -1) return false;
        Mob m = Reflection.newInstance(type);
        m.pos = spawnPos;
        m.state = m.HUNTING;
        Dungeon.level.mobs.add(m);
        GameScene.add(m);
        Dungeon.level.occupyCell(m);
        CellEmitter.get(spawnPos).burst(Speck.factory(Speck.RED_LIGHT), 10);
        return true;
    }

    //喷火法术·起手：锁定锥形区域并预警，播放施法动画，进入蓄力
    private void castFireBreath(){
        if (enemy == null) return;

        fireBreathTarget = enemy.pos;
        final Ballistica bolt = new Ballistica(pos, fireBreathTarget,
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
        ConeAOE cone = new ConeAOE(bolt, 6, 45,
                Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);

        fireBreathCells = new ArrayList<>();
        for (int cell : cone.cells){
            if (cell == pos) continue;
            fireBreathCells.add(cell);
            //预警：在即将被点燃的格子上显示高亮标记
            sprite.parent.add(new ColorTargetedCell(cell, 0xFF4422));
        }

        //施法动画：朝向目标播放蓄力施法动作（不发射弹道、不结算伤害）
        ((FireMagicGirlSprite) sprite).cast(fireBreathTarget);

        fireBreathCharge = 2;
    }

    //喷火法术·释放：对预警锁定的格子真正喷出怒焰
    private void releaseFireBreath(){
        if (fireBreathCells == null) return;

        for (int cell : fireBreathCells){
            if (!Dungeon.level.insideMap(cell)) continue;
            if (Dungeon.level.map[cell] == Terrain.DOOR){
                Level.set(cell, Terrain.OPEN_DOOR);
                GameScene.updateMap(cell);
            }

            GameScene.add(Blob.seed(cell, 12, HalomethaneFire.class));

            Char ch = Actor.findChar(cell);
            if (ch != null && ch.alignment == Alignment.ENEMY){
                ch.damage(Random.NormalIntRange(12, 22), this);
                if (ch.isAlive()){
                    Buff.affect(ch, Burning.class).reignite(ch, 8f);
                }
            }
        }

        //喷出锥形火弹视觉（朝预警时锁定的方向）
        if (fireBreathTarget != -1){
            final Ballistica bolt = new Ballistica(pos, fireBreathTarget,
                    Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
            ConeAOE cone = new ConeAOE(bolt, 6, 45,
                    Ballistica.STOP_TARGET | Ballistica.STOP_SOLID | Ballistica.IGNORE_SOFT_SOLID);
            for (Ballistica ray : cone.outerRays){
                ((MagicMissile) sprite.parent.recycle(MagicMissile.class)).reset(
                        MagicMissile.FIRE_CONE,
                        sprite,
                        ray.path.get(ray.dist),
                        null
                );
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        Sample.INSTANCE.play(Assets.Sounds.BLAST);

        yell(Messages.get(this, "firebreath_" + Random.IntRange(1, 2)));

        fireBreathCells = null;
        fireBreathTarget = -1;
    }

    @Override
    public boolean act() {

        //安全兜底：莲娜已死（如临死反扑自杀）时立即结束回合，避免已移除实体继续执行技能
        if (!isAlive()){
            spend(TICK);
            return true;
        }

        if(VeryAngry){
            if (Dungeon.level.map[pos] == Terrain.WATER){
                Level.set( pos, Terrain.EMPTY);
                GameScene.updateMap( pos );
                CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 10 );
            }

            //1.67 evaporated tiles on average
            int evaporatedTiles = Random.chances(new float[]{0, 1, 2});

            for (int i = 0; i < evaporatedTiles; i++) {
                int cell = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
                if (Dungeon.level.map[cell] == Terrain.WATER){
                    Level.set( cell, Terrain.EMPTY);
                    GameScene.updateMap( cell );
                    CellEmitter.get( cell ).burst( Speck.factory( Speck.STEAM ), 10 );
                }
            }

            for (int i : PathFinder.NEIGHBOURS9) {
                int vol = Fire.volumeAt(pos+i, HalomethaneFire.class);
                if (vol < 4 && !Dungeon.level.water[pos + i] && !Dungeon.level.solid[pos + i]){
                    GameScene.add( Blob.seed( pos + i, 4 - vol, HalomethaneFire.class ) );
                }
            }
        }

        if(allDead){
            immunities.add(Burning.class);
            immunities.add(HalomethaneBurning.class);
            immunities.add(FrostBurning.class);
        }

        //红莲真火冷却递减（冷却期内不再触发红莲业火）
        if (redLotusCooldown > 0){
            redLotusCooldown--;
        }

        //读档后重挂浊焰核心引用（核心实体由 Level.mobs 恢复流程自动重建，这里只找回来）
        if (pendingCorePositions != null){
            ArrayList<Integer> pending = pendingCorePositions;
            pendingCorePositions = null;
            redLotusCores.clear();
            for (int cp : pending){
                for (Mob m : Dungeon.level.mobs){
                    if (m instanceof TurbidFlameCore && m.pos == cp && m.isAlive()){
                        redLotusCores.add((TurbidFlameCore) m);
                        break;
                    }
                }
            }
            //若读档时核心已全部消亡，蓄力直接化解
            if (redLotusCores.isEmpty() && redLotusCharge > 0){
                redLotusCharge = 0;
                if (sprite != null){
                    sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "redlotus_save"));
                    yell(Messages.get(this, "redlotus_save"));
                }
            }
        }

        //===== 喷火蓄力中：倒计时结束才真正喷出怒焰 =====
        if (fireBreathCharge > 0){
            fireBreathCharge--;
            if (fireBreathCharge <= 0){
                releaseFireBreath();
            }
            spend(TICK);
            return true;
        }

        //===== 红莲真火蓄力中：起手已全屏预警一次，蓄力期间只显示回合倒计时 =====
        if (redLotusCharge > 0){
            redLotusCharge--;

            //起手已全屏警告，这里只显示剩余回合倒计时
            if (redLotusCharge > 0){
                sprite.showStatus(0xFF8800, Messages.get(this, "redlotus_count", redLotusCharge));
            }

            boolean allDestroyed = true;
            for (TurbidFlameCore core : redLotusCores.toArray(new TurbidFlameCore[0])){
                if (core.isAlive()){
                    allDestroyed = false;
                }
            }

            if (allDestroyed){
                //保命成功：红莲真火被化解
                redLotusCharge = 0;
                redLotusCores.clear();
                //化解也算一次完整使用，进入长冷却
                redLotusCooldown = Random.NormalIntRange(40, 80);
                sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "redlotus_save"));
                yell(Messages.get(this, "redlotus_save"));
            } else if (redLotusCharge <= 0){
                releaseRedLotus();
            }

            spend(TICK);
            return true;
        }

        //===== 天火蓄力中：三角警告区预警，2回合后真实AOE =====
        if (skyFireCharge > 0){
            skyFireCharge--;
            warnSkyFire();
            if (skyFireCharge <= 0){
                releaseSkyFire();
            }
            spend(TICK);
            return true;
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

                if(Statistics.attackIFGirl && !VeryAngry) {
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
            //莲娜已在此处死亡（临死反扑自杀），立即结束回合，绝不能再执行后续怒之技/召唤/蓄力逻辑
            return true;
        }

        //===== 怒之技：莲娜愤怒姿态（VeryAngry）时才能使用的特殊技能 =====
        if (VeryAngry && phase >= 0){
            if (paralysed > 0){
                spend(TICK);
                return true;
            }

            if (abilityCooldown <= 0){
                rollForAbility();

                if (lastAbility == LINK && lifeLinkSubject()){
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == TELE && teleportSubject()) {
                    lastAbility = TELE;
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == ENRAGE){
                    Buff.affect(this, Adrenaline.class, 12f);
                    Buff.affect(this, ChampionEnemy.Halo.class);
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == DEATHRATTLE){
                    yell(Messages.get(this, "death_rattle"));
                    summonSubject();
                    summonSubject();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == SACRIFICE){
                    sacrificeSubject();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == SUMMON){
                    if (summonSubject()){
                        yell(Messages.get(this, "more_summon"));
                    }
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == FIREBREATH){
                    castFireBreath();
                    abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    spend(TICK);
                    return true;
                } else if (lastAbility == HONGLIAN){
                    if (castRedLotus()){
                        abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    }
                    spend(TICK);
                    return true;
                } else if (lastAbility == SKYFIRE){
                    if (castSkyFire()){
                        abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
                    }
                    spend(TICK);
                    return true;
                }
            } else {
                abilityCooldown--;
            }
        }

        return super.act();
    }
    //===== 暴怒姿态·第二阶段：红莲真火（全屏预警，摧毁浊焰核心保命）===== 
    private static final int RED_LOTUS_CHARGE_TOTAL = 10;   //全屏预警持续回合数

    //强制莲娜闪现到王座(612)：常规传送失败时改用邻近空位强制闪现
    private void flashToThrone(){
        int throne = 612;
        if (ScrollOfTeleportation.teleportToLocation(this, throne)){
            return;
        }
        int fallback = -1;
        int w = Dungeon.level.width();
        for (int i : PathFinder.NEIGHBOURS8){
            int c = throne + i;
            if (Dungeon.level.insideMap(c) && !Dungeon.level.solid[c] && !Dungeon.level.pit[c] && Actor.findChar(c) == null){
                fallback = c;
                break;
            }
        }
        if (fallback == -1) fallback = throne;
        ScrollOfTeleportation.appear(this, fallback);
        Dungeon.level.occupyCell(this);
    }

    //红莲真火·起手：生成两个浊焰核心 + 全屏预警
    private boolean castRedLotus(){
        if (enemy == null) return false;

        //释放红莲业火时，莲娜必定闪现到王座(612)处，浊焰核心也将在其周围生成
        flashToThrone();

        ArrayList<Integer> corePos = findCorePositions();
        if (corePos.size() < 2) return false;

        redLotusCores.clear();
        //只生成两个浊焰核心（摧毁两个结晶即可化解）
        for (int i = 0; i < Math.min(2, corePos.size()); i++){
            int p = corePos.get(i);
            TurbidFlameCore core = new TurbidFlameCore();
            core.pos = p;
            Dungeon.level.mobs.add(core);
            GameScene.add(core);
            Dungeon.level.occupyCell(core);
            redLotusCores.add(core);
        }

        redLotusCharge = RED_LOTUS_CHARGE_TOTAL;
        warnFullScreen();
        Camera.main.shake(3f, 0.6f);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        ((FireMagicGirlSprite) sprite).cast(enemy.pos);
        yell(Messages.get(this, "redlotus_" + Random.IntRange(1, 2)));
        return true;
    }

    //为浊焰核心寻找两个空位（优先贴身八格，不足则用召唤空位补足）
    private ArrayList<Integer> findCorePositions(){
        ArrayList<Integer> spots = new ArrayList<>();
        for (int i : PathFinder.NEIGHBOURS8){
            if (Actor.findChar(pos + i) == null && !Dungeon.level.solid[pos + i] && !Dungeon.level.pit[pos + i]){
                spots.add(pos + i);
            }
        }
        while (spots.size() < 2){
            int p = findSpawnPos();
            if (p == -1) break;
            if (!spots.contains(p)) spots.add(p);
        }
        return spots;
    }

    //全屏预警：对所有可见格子铺红色警告标记
    private void warnFullScreen(){
        for (int c = 0; c < Dungeon.level.length(); c++){
            if (Dungeon.level.heroFOV[c]){
                sprite.parent.add(new ColorTargetedCell(c, 0xFF0000));
            }
        }
    }

    //红莲真火·释放：未摧毁全部核心 → 血量削至当前 1/5；若当前血量已低于 1/5 则直接秒杀
    private void releaseRedLotus(){

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Camera.main.shake(4f, 1f);

        Char h = Dungeon.hero;
        if (h != null && h.isAlive()){
            if (h.HP <= h.HT / 5){
                h.damage(h.HP + 1, this, DamageType.REAL);
                h.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "redlotus_kill"));
                if (h == Dungeon.hero && !h.isAlive()) Dungeon.fail(getClass());
            } else {
                h.HP = h.HP / 5;
                if (h.HP <= 0) h.HP = 1;
                h.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "redlotus_hit"));
                h.sprite.burst(0xFF0000, 10);
            }
        }

        for (TurbidFlameCore core : redLotusCores.toArray(new TurbidFlameCore[0])){
            if (core.isAlive()){
                core.die(this);
            }
        }
        redLotusCores.clear();
        redLotusCharge = 0;
        //释放完成，进入长冷却（40-80 回合）
        redLotusCooldown = Random.NormalIntRange(40, 80);
    }

    //浊焰核心被摧毁
    public void onCoreDestroyed(TurbidFlameCore core){
        redLotusCores.remove(core);
        if (sprite != null) sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "core_down"));
    }

    //红莲真火是否正在蓄力（供水晶塔判断是否暂停火墙）
    public boolean isRedLotusCharging(){
        return redLotusCharge > 0;
    }

    //天火是否正在蓄力（供水晶塔判断是否跳过8向齐射）
    public boolean isSkyFireCharging(){
        return skyFireCharge > 0;
    }

    //===== 暴怒姿态·第三阶段：天火（三角警告区，2回合后真实AOE）===== 
    private static final int SKY_FIRE_CHARGE_TOTAL = 2;   //三角区预警回合数

    //天火·起手：以莲娜为顶点朝英雄方向生成三角警告区
    private boolean castSkyFire(){
        if (enemy == null) return false;

        int w = Dungeon.level.width();
        float dx = enemy.pos % w - pos % w;
        float dy = enemy.pos / w - pos / w;
        skyFireAngle = (float)Math.atan2(dy, dx);

        skyFireCells = triangleCells(pos, skyFireAngle, 12, 6);
        if (skyFireCells.isEmpty()) return false;

        skyFireCharge = SKY_FIRE_CHARGE_TOTAL;
        warnSkyFire();
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
        ((FireMagicGirlSprite) sprite).cast(enemy.pos);
        yell(Messages.get(this, "skyfire_" + Random.IntRange(1, 2)));
        return true;
    }

    //三角形警告区格子：顶点 apex、方向 angle、长度 length、底边半宽 halfBase
    private ArrayList<Integer> triangleCells(int apex, float angle, int length, int halfBase){
        ArrayList<Integer> cells = new ArrayList<>();
        HashSet<Integer> seen = new HashSet<>();
        int w = Dungeon.level.width();
        float dirX = (float)Math.cos(angle);
        float dirY = (float)Math.sin(angle);
        float px = -dirY;
        float py = dirX;
        int ax = apex % w;
        int ay = apex / w;
        for (int d = 1; d <= length; d++){
            float hw = halfBase * (float)d / length;
            int cx = Math.round(ax + dirX * d);
            int cy = Math.round(ay + dirY * d);
            int half = (int)Math.ceil(hw);
            for (int s = -half; s <= half; s++){
                int x = cx + Math.round(px * s);
                int y = cy + Math.round(py * s);
                int c = y * w + x;
                if (!Dungeon.level.insideMap(c)) continue;
                if (seen.add(c)) cells.add(c);
            }
        }
        return cells;
    }

    //天火预警：三角警告区铺金色警告标记
    private void warnSkyFire(){
        if (skyFireCells == null) return;
        for (int c : skyFireCells){
            sprite.parent.add(new ColorTargetedCell(c, 0xFFAA00));
        }
    }

    //天火·释放：2回合后，对三角区内所有非敌方角色造成真实伤害并附磷火燃烧
    private void releaseSkyFire(){
        if (skyFireCells == null) return;

        Sample.INSTANCE.play(Assets.Sounds.BLAST);
        Camera.main.shake(3f, 0.6f);

        for (int c : skyFireCells){
            GameScene.add(Blob.seed(c, 3, HalomethaneFire.class));
            Char ch = Actor.findChar(c);
            if (ch != null && ch.alignment != Alignment.ENEMY){
                ch.damage(Random.IntRange(20, 35), this, DamageType.REAL);
                Buff.affect(ch, HalomethaneBurning.class).reignite(ch, 8f);
                ch.sprite.burst(0xFFAA00, 8);
                if (ch == Dungeon.hero && !ch.isAlive()){
                    Dungeon.fail(getClass());
                }
            }
        }
        skyFireCells = null;
        skyFireCharge = 0;
    }

    private static final String PHASE = "phase";
    private static final String ABILITY_CD = "ability_cd";
    private static final String SUMMON_CD = "summon_cd";
    private static final String TARGETED_CELLS = "targeted_cells";

    private static final String ALL_DEAD = "all_dead";
    private static final String VERY_ANGRY = "very_angry";
    private static final String FIRE_BREATH_CHARGE = "firebreath_charge";
    private static final String FIRE_BREATH_CELLS = "firebreath_cells";
    private static final String FIRE_BREATH_TARGET = "firebreath_target";
    private static final String RED_LOTUS_CHARGE = "red_lotus_charge";
    private static final String RED_LOTUS_COOLDOWN = "red_lotus_cooldown";
    private static final String RED_LOTUS_CORES = "red_lotus_cores";
    private static final String SKY_FIRE_CHARGE = "sky_fire_charge";
    private static final String SKY_FIRE_CELLS = "sky_fire_cells";
    private static final String SKY_FIRE_ANGLE = "sky_fire_angle";

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
        bundle.put(VERY_ANGRY, VeryAngry);

        bundle.put(FIRE_BREATH_CHARGE, fireBreathCharge);
        if (fireBreathCells != null){
            int[] cells = new int[fireBreathCells.size()];
            for (int i = 0; i < fireBreathCells.size(); i++){
                cells[i] = fireBreathCells.get(i);
            }
            bundle.put(FIRE_BREATH_CELLS, cells);
        }
        bundle.put(FIRE_BREATH_TARGET, fireBreathTarget);

        bundle.put(RED_LOTUS_CHARGE, redLotusCharge);
        bundle.put(RED_LOTUS_COOLDOWN, redLotusCooldown);
        int[] coreArr = new int[redLotusCores.size()];
        for (int i = 0; i < redLotusCores.size(); i++){
            coreArr[i] = redLotusCores.get(i).pos;
        }
        bundle.put(RED_LOTUS_CORES, coreArr);

        bundle.put(SKY_FIRE_CHARGE, skyFireCharge);
        if (skyFireCells != null){
            int[] skyArr = new int[skyFireCells.size()];
            for (int i = 0; i < skyFireCells.size(); i++){
                skyArr[i] = skyFireCells.get(i);
            }
            bundle.put(SKY_FIRE_CELLS, skyArr);
        }
        bundle.put(SKY_FIRE_ANGLE, skyFireAngle);
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
        VeryAngry = bundle.getBoolean(VERY_ANGRY);

        fireBreathCharge = bundle.getInt(FIRE_BREATH_CHARGE);
        int[] cells = bundle.getIntArray(FIRE_BREATH_CELLS);
        if (cells != null){
            fireBreathCells = new ArrayList<>();
            for (int i : cells) fireBreathCells.add(i);
        }
        fireBreathTarget = bundle.getInt(FIRE_BREATH_TARGET);

        redLotusCharge = bundle.getInt(RED_LOTUS_CHARGE);
        redLotusCooldown = bundle.getInt(RED_LOTUS_COOLDOWN);
        redLotusCores = new ArrayList<>();
        //核心实体由 Level.mobs 的 bundle 恢复流程自动重建，这里只暂存位置，首回合再重挂引用
        int[] coreArr = bundle.getIntArray(RED_LOTUS_CORES);
        if (coreArr != null){
            pendingCorePositions = new ArrayList<>();
            for (int p : coreArr) pendingCorePositions.add(p);
        }

        skyFireCharge = bundle.getInt(SKY_FIRE_CHARGE);
        int[] skyArr = bundle.getIntArray(SKY_FIRE_CELLS);
        if (skyArr != null){
            skyFireCells = new ArrayList<>();
            for (int p : skyArr) skyFireCells.add(p);
        }
        skyFireAngle = bundle.getFloat(SKY_FIRE_ANGLE);
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

        if(Statistics.RandMode && Dungeon.depth == 20){
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
            if (mob instanceof SRPDICLRPRO ||mob instanceof Skeleton||mob instanceof DM100|| mob instanceof BlackHost|| mob instanceof Warlock|| mob instanceof Monk|| mob instanceof CrystalDiedTower|| mob instanceof CrystalLingTower|| mob instanceof TurbidFlameCore) {
                mob.die( cause );
            }
        }

        GameScene.bossSlain();
        Buff.detach(hero, MagicGirlSayTimeLast.class);

        Badges.KILL_FIREGIRL();

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

    //===== 怒之技：召唤系小怪（旧版废弃方法复活）=====
    public static class ColdGuradB extends ColdGurad {
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
        @Override
        public void damage(int dmg, Object src, DamageType type) {
            super.damage(dmg, src, type);
            LockedFloor lock = hero.buff(LockedFloor.class);
            if (lock != null){
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
                else                                                    lock.addTime(dmg*1.5f);
            }
        }
    }

    public static class ColdGuradC extends Thief {

        public static class ColdGuradCSprite extends ThiefSprite {

            public ColdGuradCSprite(){
                super();
                tint(1, 1, 0, 0.2f);
            }

            @Override
            public void resetColor() {
                super.resetColor();
                tint(1, 1, 0, 0.2f);
            }
        }

        {
            state = HUNTING;
            this.HT = 20;
            this.HP = 20;
            immunities.add(Corruption.class);
            resistances.add(Amok.class);
            lootChance=0f;
            maxLvl = -8848;
            spriteClass = ColdGuradCSprite.class;
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
            super.damage(dmg, src, type);
            LockedFloor lock = hero.buff(LockedFloor.class);
            if (lock != null){
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
                else                                                    lock.addTime(dmg*1.5f);
            }
        }

        @Override
        public int attackProc(Char enemy, int damage){
            if(Random.Int(10)==0) {
                Buff.affect(enemy, Weakness.class, 2f);
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
                    m.damage(30, this, DamageType.REAL);
                }
            }
        }
    }

}
