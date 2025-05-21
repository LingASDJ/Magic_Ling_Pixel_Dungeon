package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scorpio;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BloodParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class TowerMachine extends Boss {

    private int attackCooldown = 0;
    private int summonedMobs = 1;

    {
        initProperty();
        initBaseStatus(50, 75, 33, 0, 400, 0, 0);
        initStatus(120);
        first = true;
        spriteClass = TowerMachineSprite.class;

        viewDistance = 100;
        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);
    }

    @Override
    public boolean act() {
        ArrayList<Integer> positions = new ArrayList<>();
        if (buff(SummonColdDown.class) == null && state != SLEEPING && summonedMobs <= 5) {
            Mob testActor = getSummonTimeMobs();
            testActor.state = testActor.HUNTING;
            GameScene.add(testActor);
            /****************/
            positions.add(162);
            positions.add(110);
            positions.add(37);
            positions.add(114);
            /****************/
            Random.shuffle(positions);
            ScrollOfTeleportation.appear(testActor,positions.get(0));
            Buff.affect(this, TowerTime.SummonColdDown.class, HP <= HT / 2 ? 15f : 25f);
            summonedMobs++;
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        dmg -= dmg * (summonedMobs*5) / 100;
        super.damage(dmg, src);
    }

    private Mob getSummonTimeMobs() {
        List<Class<? extends Mob>> mobTypes = new ArrayList<>();
        mobTypes.add(Eye.class);
        mobTypes.add(ShieldHuntsman.class);
        mobTypes.add(Scorpio.class);
        Random.shuffle(mobTypes);
        Class<? extends Mob> selectedMobType = mobTypes.get(0);
        Mob mob = null;
        try {
            mob = selectedMobType.getDeclaredConstructor().newInstance();
        } catch (Exception ignored) {}
        return mob;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 65);
    }

    private boolean targeting = false;
    private boolean shot = true;
    private int cellToFire = 0;

    private static final String ATTACK_COOLDOWN = "attack_cooldown";
    private static final String TARGETING = "targeting";
    private static final String SHOT = "shot";
    private static final String CELL_TO_FIRE = "cell_to_fire";
    private static final String SUMMONED_MOBS = "summoned_mobs";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ATTACK_COOLDOWN, attackCooldown);
        bundle.put(TARGETING, targeting);
        bundle.put(SHOT, shot);
        bundle.put(CELL_TO_FIRE, cellToFire);
        bundle.put(SUMMONED_MOBS, summonedMobs);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        attackCooldown = bundle.getInt(ATTACK_COOLDOWN);
        targeting = bundle.getBoolean(TARGETING);
        shot = bundle.getBoolean(SHOT);
        cellToFire = bundle.getInt(CELL_TO_FIRE);
        summonedMobs = bundle.getInt(SUMMONED_MOBS);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return Dungeon.level.distance(pos, target) > 1 || HP < HT / 2;
    }

    @Override
    public int attackSkill( Char target ) {
        return 26;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(4, 8);
    }

    public void onZapComplete(int cell) {
        zap(cell);
        next();
    }

    private void zap(int cell) {

        boolean LastHP = HP <= HT/2;
        
        spend(1f);
        Invisibility.dispel(this);
        int dmg = damageRoll();

        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);

        if(LastHP){
            if(Dungeon.hero != null){
                if(Dungeon.hero.pos == cell){
                    Dungeon.hero.damage(dmg*2,new Eye.DeathGaze());
                }
            }
            for(int c: PathFinder.NEIGHBOURS49){
                CellEmitter.get(cell+c).burst(PurpleParticle.MISSILE, 15);
                if(Dungeon.hero != null){
                    if(Dungeon.hero.pos == cell + c){
                        Dungeon.hero.damage(dmg*2,new Eye.DeathGaze());
                    }
                }
            }
        } else {
            for(int c: PathFinder.NEIGHBOURS13_4){
                CellEmitter.get(cell+c).burst(BloodParticle.BURST, 20);
                Mob mob = Dungeon.level.findMob(cell+c);
                if(mob != null && mob.getClass() != TowerMachine.class){
                    mob.damage(dmg,new Bomb());
                }
                if(Dungeon.hero != null){
                    if(Dungeon.hero.pos == cell){
                        Dungeon.hero.damage(dmg,new Bomb());
                    }
                }
            }
        }



        if (!enemy.isAlive() && enemy == Dungeon.hero) {
            Dungeon.fail(getClass());
            GLog.n(Messages.get(this, "bomb_party_kill"));
        }
    }

    // 修改doAttack方法
    protected boolean doAttack(Char enemy) {
        boolean LastHP = HP <= HT/2;
        if (Dungeon.level.adjacent(pos, enemy.pos)) {
            attackCooldown = 0;
            shot = true;
            targeting = false;
            return super.doAttack(enemy);
        } else if (shot) {
            // 进入瞄准阶段
            targeting = true;
            shot = false;
            cellToFire = enemy.pos;
            attackCooldown = LastHP ? 3 : 8;
            // 显示瞄准效果
            sprite.parent.add(new ColorTargetedCell(cellToFire,LastHP  ? Window.DeepPK_COLOR : Window.TITLE_COLOR));
            if (LastHP) {
                for (int c : PathFinder.NEIGHBOURS49) {
                    sprite.parent.add(new ColorTargetedCell(cellToFire + c, Window.DeepPK_COLOR));
                }
            } else {
                for (int c : PathFinder.NEIGHBOURS13) {
                    sprite.parent.add(new ColorTargetedCell(cellToFire + c,Window.TITLE_COLOR));
                }
            }
            ((TowerMachineSprite)sprite).targeting(cellToFire);
            spend(TICK);
            return true;
        } else if (attackCooldown > 0) {
            // 冷却倒计时
            spend(TICK);
            attackCooldown--;
            return true;
        } else {
            // 冷却结束执行攻击
            shot = true;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(cellToFire);
            } else {
                zap(cellToFire);
            }
            targeting = false;
            return true;
        }
    }

    public static class StarCanon extends Item {
        {
            image = ItemSpriteSheet.PROJECTILES_STAR;
        }
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    private class Waiting extends Mob.Wandering{

        @Override
        protected boolean noticeEnemy() {
            spend(TICK);
            return super.noticeEnemy();
        }
    }

    public static class SummonColdDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION	= 10f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }

}

