package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class MagneticCrown extends Artifact {
    {
        image = ItemSpriteSheet.MAGNETIC_CROWN;
        levelCap = 10;
        exp = 0;
        charge = 3;
        chargeCap = 3;
        defaultAction = AC_ACTIVATE;
    }

    public static final String AC_ACTIVATE = "ACTIVATE";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (isEquipped(hero) && charge > 0 && !cursed) {
            actions.add(AC_ACTIVATE);
        }
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_ACTIVATE)) {
            if (!isEquipped(hero)) {
                GLog.w(Messages.get(this, "no_equip"));
                return;
            }
            if (cursed) {
                GLog.w(Messages.get(this, "cursed"));
                return;
            }
            if (charge <= 0) {
                GLog.w(Messages.get(this, "no_charge"));
                return;
            }
            GameScene.selectCell(new DragSelector());
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new Recharge();
    }

    private float chargeAccumulator = 0f;

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;

        if (charge < chargeCap) {
            chargeAccumulator += 0.3f;
            while (chargeAccumulator >= 1f) {
                charge++;
                chargeAccumulator -= 1f;
            }

            if (charge > chargeCap) {
                charge = chargeCap;
                chargeAccumulator = 0f;
            }

            updateQuickslot();
        }
    }


    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        if (isEquipped(Dungeon.hero)) {
            if (cursed) {
                desc += "\n\n" + Messages.get(this, "desc_cursed");
            } else {
                desc += "\n\n" + Messages.get(this, "desc_range", (int)(3 + level * 0.5f));
            }
        }
        return desc;
    }


    @Override
    public String name() {
        return Messages.get(this, "name");
    }

    @Override
    public String status() {
        if (cursed || !isEquipped(Dungeon.hero)) {
            return null;
        }
        return Messages.format("%d/%d", charge, chargeCap);
    }

    public class DragSelector extends CellSelector.Listener {
        @Override
        public void onSelect(Integer target) {
            if (target == null) return;

            int targetPos = target;
            ArrayList<Char> validTargets = new ArrayList<>();
            ArrayList<Char> enemyTargets = new ArrayList<>();  // 分离敌人目标
            float minDist = Float.MAX_VALUE;
            Char nearest = null;

            for (Char ch : Actor.chars()) {
                if (Dungeon.hero.fieldOfView[ch.pos]) {
                    if (ch.properties().contains(Char.Property.IMMOVABLE) ||
                            ch.properties().contains(Char.Property.NPC)) {
                        continue;
                    }

                    float dist = Dungeon.level.distance(targetPos, ch.pos);
                    if (dist <= 3 + level * 0.5f) {
                        validTargets.add(ch);
                        // 如果是敌人，加入敌人列表
                        if (ch.alignment != Char.Alignment.ALLY) {
                            enemyTargets.add(ch);
                        }
                    }
                }
            }

            if (validTargets.isEmpty()) {
                GLog.w(Messages.get(MagneticCrown.this, "no_target"));
                return;
            }

            // 优先从敌人列表中选择目标
            ArrayList<Char> priorityTargets = enemyTargets.isEmpty() ? validTargets : enemyTargets;

            for (Char ch : priorityTargets) {
                Ballistica trajectory = new Ballistica(ch.pos, targetPos, Ballistica.PROJECTILE);
                float dist = Dungeon.level.distance(targetPos, ch.pos);

                if (trajectory.collisionPos == targetPos ||
                        (dist < minDist && !Dungeon.level.solid[trajectory.collisionPos])) {
                    minDist = dist;
                    nearest = ch;
                } else if (dist == minDist) {
                    if (ch.alignment != Char.Alignment.ALLY &&
                            nearest.alignment == Char.Alignment.ALLY) {
                        nearest = ch;
                    }
                }
            }

            // 如果没有找到理想目标，从优先目标列表中选择最近的
            if (nearest == null) {
                nearest = priorityTargets.get(0);
                for (Char ch : priorityTargets) {
                    float dist = Dungeon.level.distance(targetPos, ch.pos);
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = ch;
                    } else if (dist == minDist) {
                        if (ch.alignment != Char.Alignment.ALLY &&
                                nearest.alignment == Char.Alignment.ALLY) {
                            nearest = ch;
                        }
                    }
                }
            }

            charge--;
            updateQuickslot();

            // 计算实际拖拽位置
            Ballistica trajectory = new Ballistica(nearest.pos, targetPos, Ballistica.PROJECTILE);
            int newPos = trajectory.collisionPos;

            // 如果目标位置被占据，尝试找到最近的可用位置
            if (Dungeon.level.solid[newPos] || Actor.findChar(newPos) != null) {
                int bestPos = nearest.pos;
                float bestDist = Float.MAX_VALUE;

                for (int i : PathFinder.NEIGHBOURS8) {
                    int checkPos = newPos + i;
                    if (Dungeon.level.passable[checkPos] && Actor.findChar(checkPos) == null) {
                        float dist = Dungeon.level.distance(targetPos, checkPos);
                        if (dist < bestDist) {
                            bestDist = dist;
                            bestPos = checkPos;
                        }
                    }
                }

                newPos = bestPos;
            }

            if (newPos != nearest.pos) {
                Actor.addDelayed(new Pushing(nearest, nearest.pos, newPos), -1);
                nearest.pos = newPos;
                Dungeon.level.occupyCell(nearest);
                Dungeon.hero.spendAndNext(1f);
            }

            try {
                Trap t = Dungeon.level.traps.get(target).reveal();
                if (newPos == t.pos && !t.mcOnlyUpgrade) {
                    t.mcOnlyUpgrade = true;
                    onTrapTriggered();
                }
            } catch (NullPointerException ignored) {}

            CellEmitter.get(nearest.pos).burst(ShadowParticle.UP, 5);
        }

        @Override
        public String prompt() {
            return Messages.get(MagneticCrown.this, "prompt");
        }
    }


    public class Recharge extends ArtifactBuff {
        private int turnsToCharge = Math.max(1, 50 - level());

        @Override
        public boolean act() {
            if (charge < chargeCap) {
                turnsToCharge--;
                if (turnsToCharge <= 0) {
                    charge++;
                    turnsToCharge = Math.max(1, 50 - level());
                    updateQuickslot();
                }
            }
            spend(TICK);
            return true;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", turnsToCharge);
        }
    }

    @Override
    public Item upgrade() {
        super.upgrade();
        chargeCap++;
        if (chargeCap > 10) chargeCap = 10;
        charge = chargeCap;
        return this;
    }

    public void onTrapTriggered() {
        if (!isEquipped(Dungeon.hero) || cursed) return;

        exp += 10;
        int expNeeded = (50 + 7 * level());

        while (exp >= expNeeded && level() < levelCap) {
            exp -= expNeeded;
            upgrade();
            GLog.p(Messages.get(this, "levelup"));
            expNeeded = (50 + 7 * level());
        }

        updateQuickslot();
    }

    private static final String CHARGE = "charge";
    private static final String CHARGECAP = "chargeCap";
    private static final String EXP = "exp";

    private static final String CHARGEACCUMULATOR = "chargeAccumulator";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(CHARGECAP, chargeCap);
        bundle.put(EXP, exp);
        bundle.put(CHARGEACCUMULATOR, chargeAccumulator);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
        chargeCap = bundle.getInt(CHARGECAP);
        exp = bundle.getInt(EXP);
        chargeAccumulator = bundle.getFloat(CHARGEACCUMULATOR);
    }
}
