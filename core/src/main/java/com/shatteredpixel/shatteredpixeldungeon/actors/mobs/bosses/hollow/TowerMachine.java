package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Boss;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MagicFireParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.RainbowParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TowerMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class TowerMachine extends Boss {

    private int attackCooldown = 0;

    {
        initProperty();
        initBaseStatus(50, 75, 100, 0, 600, 0, 0);
        initStatus(120);
        noDropIceCoin = true;
        spriteClass = TowerMachineSprite.class;

        viewDistance = 100;
        state = WANDERING = new Waiting();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.BOSS);

        immunities.add(FrostBurning.class);
        immunities.add(HalomethaneBurning.class);
        immunities.add(Burning.class);
        immunities.add(Ooze.class);
        immunities.add(Terror.class);
        immunities.add(Hex.class);
        immunities.add(Vertigo.class);
        immunities.add(Blindness.class);
        immunities.add(TowerMachine.DeadAlive.class);
        immunities.add(Blob.class);
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if (src == this) {
            return;
        }
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null) {
            int multiple = 2;
            lock.addTime(dmg*multiple);
        }
        BossHealthBar.assignBoss(this);
        super.damage(dmg, src, type);
    }

    @Override
    public int damageRoll() {
        return 0;
    }

    private boolean targeting = false;
    private boolean shot = true;
    private int cellToFire = 0;

    private static final String ATTACK_COOLDOWN = "attack_cooldown";
    private static final String TARGETING = "targeting";
    private static final String SHOT = "shot";
    private static final String CELL_TO_FIRE = "cell_to_fire";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(ATTACK_COOLDOWN, attackCooldown);
        bundle.put(TARGETING, targeting);
        bundle.put(SHOT, shot);
        bundle.put(CELL_TO_FIRE, cellToFire);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        attackCooldown = bundle.getInt(ATTACK_COOLDOWN);
        targeting = bundle.getBoolean(TARGETING);
        shot = bundle.getBoolean(SHOT);
        cellToFire = bundle.getInt(CELL_TO_FIRE);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if( buff(MachineAttackCooledDown.class) != null){
            return false;
        }
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

    private boolean isZapping = false;

    public static class DeadBoat{};

    private void zap(int cell) {
        if (isZapping) return;
        boolean LastHP = HP <= HT / 2;

        spend(1f);
        Invisibility.dispel(this);
        int dmg = HP*2 <= HT ? 130 : 90;

        CellEmitter.get(cell).burst(SmokeParticle.FACTORY, 4);

        int[] area = LastHP ? PathFinder.NEIGHBOURS49 : PathFinder.NEIGHBOURS13_4;

        for (int c : area) {
            if (c == 0) continue;

            int targetCell = cell + c;

            CellEmitter.get(targetCell).burst(LastHP ? RainbowParticle.BURST : MagicFireParticle.FACTORY, 8);

            for (Mob mob : Dungeon.level.mobs) {
                if (mob.pos == targetCell && !(mob instanceof TowerMachine || mob instanceof Morphs || mob instanceof TowerMind.MindCore)) {
                    int mobDmg = (int) (mob.HT * 0.2f);
                    mob.damage(mobDmg, new DeadBoat());
                    mob.HP = mob.HT;
                    Buff.affect(mob, DeadAlive.class).set(5, 1);
                }
            }

            if (Dungeon.hero != null) {
                if (Dungeon.hero.pos == targetCell) {
                    Dungeon.hero.damage(dmg, new Bomb());
                }

                if (enemy != null && !enemy.isAlive() && enemy == Dungeon.hero) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "bomb_party_kill"));
                    break;
                }
            }
        }

        isZapping = false;
    }

    @Override
    public void die(Object cause) {
        super.die(cause);
         for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof TowerMind || mob instanceof TowerTime||mob instanceof TowerGods||mob instanceof TowerMachine) {
                Buff.affect(mob, TowerParalysis.class).set((21), 1);
            }
             if(mob instanceof Morphs){
                 ((Morphs) mob).phase+=0.25f;
             }
        };
    }

    public TowerParalysis towerParalysis = buff(TowerParalysis.class);

    protected boolean doAttack(Char enemy) {
        boolean LastHP = HP <= HT/2;
        if (Dungeon.level.adjacent(pos, enemy.pos) && towerParalysis == null ) {
            attackCooldown = 0;
            shot = true;
            targeting = false;
            return super.doAttack(enemy);
        } else if (shot && towerParalysis == null) {
            targeting = true;
            shot = false;
            cellToFire = enemy.pos;
            attackCooldown = 5;
            sprite.parent.add(new ColorTargetedCell(cellToFire,LastHP  ? Window.DeepPK_COLOR : Window.TITLE_COLOR));
            if (LastHP) {
                for (int c : PathFinder.NEIGHBOURS49) {
                    sprite.parent.add(new ColorTargetedCell(cellToFire + c, Window.DeepPK_COLOR));
                }
            } else {
                for (int c : PathFinder.NEIGHBOURS13_4) {
                    sprite.parent.add(new ColorTargetedCell(cellToFire + c,Window.TITLE_COLOR));
                }
            }
            ((TowerMachineSprite)sprite).targeting(cellToFire);
            spend(TICK);
            return true;

        } else if (attackCooldown > 0 && towerParalysis == null) {
            spend(TICK);
            attackCooldown--;
            return true;
        } else {
            shot = true;
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap(cellToFire);
            } else {
                onZapComplete(cellToFire);
            }
            Buff.affect(this,MachineAttackCooledDown.class, HP <= HT / 2 ?  12f : 18f);
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

    public static class MachineAttackCooledDown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 30f;

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

    }

    public static class DeadAlive extends Buff {
        public static int level = 0;
        private int interval = 1;

        {
            announced = true;
        }

        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend(interval);
                if (--level <= 0) {
                    detach();
                }

            }
            return true;
        }

        public void detach() {
          super.detach();
          if (target.isAlive()) target.die(true);
        }

        public int level() {
            return level;
        }

        public void set( int value, int time ) {
            //decide whether to override, preferring high value + low interval
            if (Math.sqrt(interval)*level <= Math.sqrt(time)*value) {
                level = value;
                interval = time;
                spend(time - cooldown() - 1);
            }
        }

        @Override
        public float iconFadePercent() {
            if (target instanceof Hero){
                float max = ((Hero) target).lvl;
                return Math.max(0, (max-level)/max);
            }
            return 0;
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level, dispTurns(visualcooldown()));
        }

        private static final String LEVEL	    = "level";
        private static final String INTERVAL    = "interval";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( INTERVAL, interval );
            bundle.put( LEVEL, level );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            interval = bundle.getInt( INTERVAL );
            level = bundle.getInt( LEVEL );
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xff0000);
        }

        @Override
        public int icon() {
            return BuffIndicator.TERROR;
        }


    }


}

