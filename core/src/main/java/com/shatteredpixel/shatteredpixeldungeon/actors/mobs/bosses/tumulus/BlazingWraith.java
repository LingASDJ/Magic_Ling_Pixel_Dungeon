package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlazingWraithSprite;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

/**
 * 炽烈亡魂：被白魔女二次擢升的高等存在，已摆脱死灵桎梏。
 *
 * <p>生命为白鹭的 18%；不会普通攻击；每 5 回合向玩家方向发射一道延迟 1 回合的激光，
 * 命中后致盲 3 回合并造成玩家最大生命值 16% 的真实伤害。
 * 防御 0~8，精准 29，闪避 27，属性：无。</p>
 */
public class BlazingWraith extends Mob {

    private static final int LASER_INTERVAL = 5;

    private int pendingLaser = -1;

    private static final String PENDING_LASER = "pending_laser";

    public BlazingWraith() {
        spriteClass = BlazingWraithSprite.class;

        HP = HT = Math.max(1, Math.round(Egret.MAX_HP * 0.18f));
        defenseSkill = 27;
        baseSpeed = 1f;
        EXP = 0;
        maxLvl = 30;
        lootChance = 0f;

        Buff.affect(this, LaserCooldown.class, LaserCooldown.DURATION);
    }

    @Override
    public int attackSkill(Char target) {
        return 29;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 8);
    }

    @Override
    public boolean canAttack(Char enemy) {
        // 不会普通攻击
        return false;
    }

    @Override
    public boolean attack(Char enemy) {
        return false;
    }

    @Override
    protected boolean act() {
        if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
            fieldOfView = new boolean[Dungeon.level.length()];
        }
        Dungeon.level.updateFieldOfView(this, fieldOfView);

        if (pendingLaser >= 0) {
            fireLaser(pendingLaser);
            pendingLaser = -1;
            spend(TICK);
            return true;
        }

        if (buff(LaserCooldown.class) == null) {
            Char enemy = chooseEnemy();
            if (enemy != null && enemy.isAlive() && enemy.pos >= 0
                    && enemy.pos < fieldOfView.length && fieldOfView[enemy.pos]) {
                pendingLaser = enemy.pos;
                Buff.affect(this, LaserCooldown.class, LaserCooldown.DURATION);
                primeLaser(enemy.pos);
                spend(TICK);
                return true;
            }
        }

        return super.act();
    }

    private void primeLaser(int target) {
        if (sprite != null) {
            sprite.turnTo(pos, target);
            sprite.flash();
        }

        Ballistica beam = new Ballistica(pos, target, Ballistica.STOP_SOLID);
        int end = beam.collisionPos == null ? target : beam.collisionPos;

        if (sprite != null && sprite.parent != null) {
            for (int cell : beam.subPath(0, beam.dist)) {
                if (cell == pos) {
                    continue;
                }
                sprite.parent.addToBack(new ColorTargetedCell(cell, 0xD81B1B));
            }
        }
        if (Dungeon.level.heroFOV[end]) {
            CellEmitter.get(end).burst(Speck.factory(Speck.LIGHT), 6);
            Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        }
    }

    private void fireLaser(int target) {
        if (target < 0 || target >= Dungeon.level.length()) {
            return;
        }

        // 穿透单位，只被墙体阻挡
        Ballistica beam = new Ballistica(pos, target, Ballistica.STOP_SOLID);
        int end = beam.collisionPos == null ? target : beam.collisionPos;

        if (sprite != null && sprite.parent != null
                && (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[end])) {
            sprite.parent.add(new Beam.DeathRay(
                    sprite.center(),
                    DungeonTilemap.raisedTileCenterToWorld(end)));
        }

        Hero hero = Dungeon.hero;
        if (hero != null && hero.isAlive()) {
            boolean hit = hero.pos == end || beam.subPath(1, beam.dist).contains(hero.pos);
            if (hit) {
                Buff.affect(hero, Blindness.class, 3f);
                hero.damage(Math.max(1, Math.round(hero.HT * 0.16f)), this, DamageType.REAL);
                if (hero.sprite != null) {
                    hero.sprite.flash();
                }
            }
        }

        if (Dungeon.level.heroFOV[end]) {
            CellEmitter.get(end).burst(Speck.factory(Speck.LIGHT), 12);
        }
    }

    @Override
    public void rollToDropLoot() {
        // 召唤物不掉落
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PENDING_LASER, pendingLaser);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        pendingLaser = bundle.contains(PENDING_LASER) ? bundle.getInt(PENDING_LASER) : -1;
    }

    /** 激光冷却：只计时，不显示 icon。 */
    public static class LaserCooldown extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 5f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0xFFAA00);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
