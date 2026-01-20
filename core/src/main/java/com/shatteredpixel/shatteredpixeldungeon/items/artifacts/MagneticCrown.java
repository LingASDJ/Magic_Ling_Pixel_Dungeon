package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
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
        private int range = (int)(3 + level * 0.5f);

        @Override
        public void onSelect(Integer target) {
            if (target == null) return;

            int targetPos = target;
            float minDist = Float.MAX_VALUE;
            Char nearest = null;

            // 首先检查英雄附近是否有敌人
            boolean hasNearbyEnemy = false;
            for (Char ch : Actor.chars()) {
                if (Dungeon.hero.fieldOfView[ch.pos] && ch.alignment == Char.Alignment.ENEMY) {
                    if (Dungeon.level.distance(Dungeon.hero.pos, ch.pos) <= range) {
                        hasNearbyEnemy = true;
                        break;
                    }
                }
            }

            for (Char ch : Actor.chars()) {
                if (Dungeon.hero.fieldOfView[ch.pos]) {
                    if (ch.properties().contains(Char.Property.IMMOVABLE) ||
                            ch.properties().contains(Char.Property.NPC)) {
                        continue;
                    }

                    float dist = Dungeon.level.distance(targetPos, ch.pos);
                    if (dist <= range) {
                        Ballistica trajectory = new Ballistica(ch.pos, targetPos, Ballistica.PROJECTILE);

                        // 检查轨迹是否有效
                        if (trajectory.collisionPos == targetPos || !Dungeon.level.solid[trajectory.collisionPos]) {
                            // 如果有附近敌人，只考虑敌人
                            if (hasNearbyEnemy && ch.alignment != Char.Alignment.ENEMY) {
                                continue;
                            }

                            // 如果距离更近，直接更新
                            if (dist < minDist) {
                                minDist = dist;
                                nearest = ch;
                            }
                            // 如果距离相等，则按优先级选择
                            else if (dist == minDist) {
                                // 定义优先级：敌人 > 玩家 > 友方
                                int currentPriority = getPriority(ch);
                                int nearestPriority = getPriority(nearest);

                                if (currentPriority > nearestPriority) {
                                    nearest = ch;
                                }
                            }
                        }
                    }
                }
            }

            if (nearest == null) {
                GLog.w(Messages.get(MagneticCrown.this, "no_target"));
                return;
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
                showRange(nearest.pos, range, nearest);
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
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        }

        @Override
        public String prompt() {
            // 在显示提示时更新范围
            range = (int)(3 + level * 0.5f);
            showRange(Dungeon.hero.pos, range, Dungeon.hero);
            return Messages.get(MagneticCrown.this, "prompt");
        }

        private void showRange(int center, int range, Char ch) {
            for (int i = 0; i < Dungeon.level.length(); i++) {
                // 只显示最外围的范围
                if (Dungeon.level.distance(center, i) == range) {
                    Game.scene().addToFront(new ColorTargetedCell(i, ch != Dungeon.hero ? 0xff0000 : Window.ORAGNECOLOR));
                }
            }
        }

        // 辅助方法：获取目标的优先级
        private int getPriority(Char ch) {
            if (ch.alignment == Char.Alignment.ENEMY) {
                return 3;  // 敌人优先级最高
            } else if (ch == Dungeon.hero) {
                return 2;  // 玩家次之
            } else if (ch.alignment == Char.Alignment.ALLY) {
                return 1;  // 友方优先级最低
            }
            return 0;
        }
    }

    public class Recharge extends ArtifactBuff {
        private float partialCharge = 0f;

        @Override
        public boolean act() {
            if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
                float chargeToGain = 1f / (50f - level());

                chargeToGain *= RingOfEnergy.artifactChargeMultiplier(target);

                partialCharge += chargeToGain;

                while (partialCharge >= 1f) {
                    if (charge < chargeCap) {
                        charge++;
                        partialCharge -= 1f;
                    } else {
                        partialCharge = 0f;
                        break;
                    }
                }
            } else {
                partialCharge = 0f;
            }

            updateQuickslot();
            spend(TICK);
            return true;
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

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGE, charge);
        bundle.put(CHARGECAP, chargeCap);
        bundle.put(EXP, exp);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        charge = bundle.getInt(CHARGE);
        chargeCap = bundle.getInt(CHARGECAP);
        exp = bundle.getInt(EXP);
    }
}