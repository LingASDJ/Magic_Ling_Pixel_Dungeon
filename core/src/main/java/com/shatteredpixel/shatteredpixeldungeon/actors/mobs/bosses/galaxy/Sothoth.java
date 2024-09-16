package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RoseShiled;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.TargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SothothSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Sothoth extends Boss {

    private ArrayList<Integer> targetedCells = new ArrayList<>();

    private float abilityCooldown;
    private static final int MIN_ABILITY_CD = 10;
    private static final int MAX_ABILITY_CD = 15;

    private int turnCount = 0;

    {
        spriteClass = SothothSprite.class;

        HP = HT = 500;
        defenseSkill = 2;

        EXP = 0;

        maxLvl = 99;

        state = WANDERING;

        properties.add(Property.BOSS);
    }

    {
        //免疫全部气体
        immunities.add(Blob.class);

        immunities.add(Terror.class);
        immunities.add(Vertigo.class);
        immunities.add(Burning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(FrostBurning.class);
        immunities.add(Freezing.class);
        immunities.add(Frost.class);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 24 );
    }

    private void zap() {
        spend( 1f );

        if (hit( this, enemy, true )) {
            int dmg = attackProc(enemy,damageRoll());
            dmg = defenseProc(enemy,dmg);
            dmg -= enemy.drRoll();
            enemy.damage(dmg, new Stone());
            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "frost_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos )) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {

                return true;
            }
        }
    }

    @Override
    protected boolean canAttack(Char enemy) {
        // 获取敌人和目标位置之间的距离
        int distanceToTarget = Dungeon.level.distance(pos, target);

        // 如果目标位置距离大于6，返回true
        if (distanceToTarget > 6 || distanceToTarget <= 1) {
            return true;
        }

        // 其他情况下，检查攻击是否能够击中敌人
        if (distanceToTarget > 2) {
            return false;
        }

        // 使用Ballistica类来检查攻击路径
        Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
        return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
    }

    public void onZapComplete() {
        zap();
        next();
    }


    private static Class<?extends Weapon.Enchantment>[] SothothrandomEnchants = new Class[]{
            Blazing.class,
            Blocking.class,
            Chilling.class,
            Kinetic.class,
            Corrupting.class,
            Elastic.class,
            Lucky.class,
            Shocking.class,
    };

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        damage = Reflection.newInstance(Random.oneOf(SothothrandomEnchants)).
                proc( new LockSword(),this, enemy, damage );

        int distanceToTarget = Dungeon.level.distance(pos, target);


        if (distanceToTarget < 2) {
            int oppositeAdjacent = enemy.pos + (enemy.pos - pos);
            Ballistica trajectory = new Ballistica(enemy.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
            WandOfBlastWave.throwChar(enemy, trajectory, 12, false, false, this);
        }

        return damage;
    }


    @Override
    public int attackSkill( Char target ) {
        return 100;
    }

    @Override
    protected boolean act() {

        laserattack();

        //敌方灵视
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob.buff(MindVision.class) == null && mob.alignment == Alignment.ENEMY){
                Buff.affect(mob, MindVision.class, 50f);
            }
        }

        //全局灵视 + 全局虚弱
        Buff.affect(hero, Weakness.class, 10);
        Buff.affect(hero, MindVision.class, 10);

        if(hero.buff(LockedFloor.class) != null){
            notice();
        }

        return super.act();
    }

    public static int[] RandomPos = new int[]{
            1152,1263,1053,1336,1263,1177,1187
    };

    public void die(Object obj)
    {
        super.die(obj);
        obj = new SothothEyeDied();
        ((SothothEyeDied) obj).pos = pos;
        GameScene.add(((Mob) (obj)));
        Actor.addDelayed(new Pushing(((Char) (obj)), pos, ((SothothEyeDied) (obj)).pos), -1F);
        yell( Messages.get(this, "defeated") );
        Statistics.TrueYogNoDied = true;

        Buff.prolong(((SothothEyeDied) obj), RoseShiled.class, 10f);

        Set<Integer> selectedPositions = new HashSet<>();
        int maxPositions = Math.min(RandomPos.length, 4);

        while (selectedPositions.size() < maxPositions) {
            int index = Random.Int(RandomPos.length);
            selectedPositions.add(RandomPos[index]);
        }

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Music.INSTANCE.fadeOut(5f, new Callback() {
                    @Override
                    public void call() {
                        Music.INSTANCE.play(Assets.YOGGOD, true);
                    }
                });
            }
        });

        for (int pos : selectedPositions) {
            ServantAvgomon csp = new ServantAvgomon();
            csp.pos = pos;
            GameScene.add(csp);
        }

    }


    @Override
    public void damage(int dmg, Object src) {
        int count = 0;

        //生命值低于250；随机召唤4根塔维尔之触，所有仆从获得全局激素涌动效果
        if(HP<=250 && first){
            Set<Integer> selectedPositions = new HashSet<>();
            int maxPositions = Math.min(RandomPos.length, 4);

            while (selectedPositions.size() < maxPositions) {
                int index = Random.Int(RandomPos.length);
                selectedPositions.add(RandomPos[index]);
            }

            for (int pos : selectedPositions) {
                SothothLasher csp = new SothothLasher();
                csp.pos = pos;
                GameScene.add(csp);
            }

            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (mob instanceof SothothLasher
                        || mob instanceof ServantAvgomon) {
                    Buff.affect(mob, Adrenaline.class,50f);
                }
            }
            HP = 250;
            first = false;
        }

        // 统计所有 Servant-Avgomon 的数量
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof ServantAvgomon) {
                count += (int) mob.spawningWeight();
            }
        }

        //亚弗戈蒙之仆
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int n : PathFinder.NEIGHBOURS8) {
            if (Dungeon.level.passable[pos+n] && Actor.findChar( pos+n ) == null) {
                candidates.add( pos+n );
            }
        }

        if(Random.Float()<=0.15f && count<6){
            ServantAvgomon spawn = new ServantAvgomon();
            spawn.pos = Random.element( candidates );
            spawn.state = spawn.HUNTING;
            GameScene.add( spawn, 1 );
            Dungeon.level.occupyCell(spawn);
        }

        // 计算总的伤害减免百分比
        // 每个亚弗戈蒙之仆减少 10% 的伤害，总减免上限为 99%
        float reduction = Math.min(count * 0.10f, 0.99f);

        float reducedDamage = dmg * (1 - reduction);

        float scaleFactor = AscensionChallenge.statModifier(this);
        int scaledDmg = Math.round(reducedDamage / scaleFactor);

        dmg = (int) (scaledDmg * scaleFactor);
        super.damage(dmg, src);
    }

    private void laserattack(){
        boolean terrainAffected = false;
        HashSet<Char> affected = new HashSet<>();
        //delay fire on a rooted hero

        if (!hero.rooted) {
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                //shoot beams

                sprite.parent.add(new Beam.DeathRay(sprite.center(), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
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
                ch.damage(Random.NormalIntRange(16, 32), new Eye.DeathGaze());
                ((SothothSprite) sprite).setLaser();
                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == hero) {
                    GLog.n(Messages.get(Char.class, "kill", name()));
                }
            }
            targetedCells.clear();
        }

        if (abilityCooldown <= 0  && alignment == Alignment.ENEMY) {
            int beams = 1;
            GLog.w(Messages.get(this, "message"));
            HashSet<Integer> affectedCells = new HashSet<>();
            for (int i = 0; i < beams; i++) {
                int targetPos;

                targetPos = hero.pos;

                targetedCells.add(targetPos);
                Ballistica b = new Ballistica(pos, targetPos, Ballistica.WONT_STOP);
                affectedCells.addAll(b.path);
            }

            //remove one beam if multiple shots would cause every cell next to the hero to be targeted
            boolean allAdjTargeted = true;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (!affectedCells.contains(hero.pos + i) && Dungeon.level.passable[hero.pos + i]) {
                    allAdjTargeted = false;
                    break;
                }
            }
            if (allAdjTargeted) {
                targetedCells.remove(targetedCells.size() - 1);
            }
            for (int i : targetedCells) {
                Ballistica b = new Ballistica(pos, i, Ballistica.WONT_STOP);
                for (int p : b.path) {
                    sprite.parent.add(new TargetedCell(p, 0xFF0000));
                    affectedCells.add(p);
                }
            }

            // 在14回合时发出警告
            if (turnCount == 14) {
                for (int i : affectedCells) {
                    sprite.parent.add(new TargetedCell(i, 0xFF0000));
                }
            }
            else
                // don't want to overly punish players with slow move or attack speed
                spend(GameMath.gate(TICK, hero.cooldown(), 3 * TICK));
            hero.interrupt();

            abilityCooldown += Random.NormalFloat(MIN_ABILITY_CD, MAX_ABILITY_CD);

        }  {
            spend(TICK);
            if (abilityCooldown > 0) abilityCooldown--;
        }

        turnCount++; // 回合数自增


    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Camera.main.shake(1f,3f);
            GameScene.bossReady();
        }
    }

    public boolean first = true;
    private static final String FIRST = "first";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

}
