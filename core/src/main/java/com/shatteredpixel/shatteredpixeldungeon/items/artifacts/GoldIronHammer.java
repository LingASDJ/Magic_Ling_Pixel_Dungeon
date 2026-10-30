package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import static com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune.isMagicImmuned;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.buffs.ZeroDefense;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GoldIronHammer extends Artifact {

    public static final String AC_BLAST = "BLAST";
    public static final String AC_WEAPON_PLUS = "WEAPON_PLUS";

    private static final int MAX_CHARGE_CAP = 10;

    {
        image = ItemSpriteSheet.Gold_Iron;

        levelCap = 10;

        updateChargeCap(true);
        usesTargeting = true;
    }

    private void updateChargeCap(boolean refill) {
        chargeCap = Math.min(MAX_CHARGE_CAP, level() / 2 + 1);

        if (refill) {
            charge = chargeCap;
            partialCharge = 0f;
        } else {
            charge = Math.min(charge, chargeCap);
            if (charge >= chargeCap) {
                partialCharge = 0f;
            }
        }
    }

    @Override
    public Item upgrade() {
        super.upgrade();

        updateChargeCap(true);

        return this;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new GoldIronHammer.BlastRecharger();
    }

    @Override
    public String defaultAction() {
        return AC_BLAST;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);

        if (isMagicImmuned(hero)) {
            return actions;
        }

        if (isEquipped(hero)) {
            actions.add(AC_BLAST);
            if (charge >= 4) {
                actions.add(AC_WEAPON_PLUS);
            }
        }

        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (AC_BLAST.equals(action)) {
            GameScene.selectCell(blaster);
        }
    }

    /**
     * 神器充能，每回合 0.1f * amount 充能。
     */
    @Override
    public void charge(Hero target, float amount) {
        if (cursed || isMagicImmuned(target) || amount <= 0f) {
            return;
        }

        if (charge >= chargeCap) {
            return;
        }

        partialCharge += 0.1f * amount;

        while (partialCharge >= 1f && charge < chargeCap) {
            partialCharge -= 1f;
            charge++;
        }

        if (charge >= chargeCap) {
            charge = chargeCap;
            partialCharge = 0f;
        }

        updateQuickslot();
    }

    public class BlastRecharger extends ArtifactBuff {

        @Override
        public boolean act() {
            if (charge < chargeCap && !cursed && !isMagicImmuned(target)) {

                double loseCharge = chargeCap - charge;

                float denominator = Math.max(
                        1f,
                        80f - (float) (loseCharge * loseCharge)
                );

                float chargeGain = 1f / denominator;
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);

                if (chargeGain > 0f) {
                    partialCharge += chargeGain;

                    while (partialCharge >= 1f && charge < chargeCap) {
                        partialCharge -= 1f;
                        charge++;
                    }

                    if (charge >= chargeCap) {
                        charge = chargeCap;
                        partialCharge = 0f;
                    }
                }
            }

            updateQuickslot();
            spend(TICK);
            return true;
        }
    }

    protected CellSelector.Listener blaster = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (target == null) {
                return;
            }

            final int targetPos = target;
            final Char enemy = Actor.findChar(targetPos);

            if (Dungeon.level.avoid[targetPos]
                    || !Dungeon.level.passable[targetPos]
                    || (enemy != null && enemy.alignment != Char.Alignment.ENEMY)) {
                GLog.w(Messages.get(GoldIronHammer.class, "cant_reach"));
                return;
            }

            int RCH = 4 + (level() >= 4 ? 2 : 0);

            if (Dungeon.level.distance(curUser.pos, targetPos) > RCH) {
                GLog.w(Messages.get(GoldIronHammer.class, "cant_long_distance", RCH));
                return;
            }

            if (charge < 1) {
                GLog.w(Messages.get(GoldIronHammer.class, "cant_no_charge"));
                return;
            }

            if (enemy == null
                    || enemy == curUser
                    || curUser.isCharmedBy(enemy)
                    || !Dungeon.level.heroFOV[targetPos]) {
                GLog.w(Messages.get(GoldIronHammer.class, "ability_no_target"));
                return;
            }

            QuickSlotButton.target(enemy);

            int baseDMG = Random.NormalIntRange(
                    10 + buffedLvl(),
                    (10 + buffedLvl()) * 2
            );

            if (enemy instanceof Mob && enemy.alignment == Char.Alignment.ENEMY) {

                if (level() >= 4) {

                    for (int i : PathFinder.NEIGHBOURS9) {
                        int cell = targetPos + i;

                        if (cell < 0 || cell >= Dungeon.level.passable.length) {
                            continue;
                        }

                        Char ch = Actor.findChar(cell);

                        if (ch instanceof Mob && ch.alignment == Char.Alignment.ENEMY) {
                            int dmg = ch.pos == targetPos ? baseDMG : baseDMG / 2;

                            ch.damage(dmg, GoldIronHammer.this, Char.DamageType.PHYSICAL);
                            WandOfBlastWave.BlastWave.blast(ch.pos, 3, 0.5f);

                            if (ch.buff(ZeroDefense.class) == null) {
                                Buff.affect(ch, ZeroDefense.class, 1.5f * level());
                            }
                        }
                    }

                    Camera.main.shake(0.75f, 1f);

                } else if (level() >= 2) {

                    if (enemy.buff(ZeroDefense.class) == null) {
                        Buff.affect(enemy, ZeroDefense.class, 1.5f * level());
                    }

                } else {

                    enemy.damage(baseDMG, GoldIronHammer.this, Char.DamageType.PHYSICAL);
                    Camera.main.shake(0.75f, 1f);
                    WandOfBlastWave.BlastWave.blast(targetPos, 3, 0.5f);
                }

                charge--;
            }

            curUser.sprite.turnTo(curUser.pos, targetPos);
            curUser.sprite.zap(curUser.pos);
            curUser.spendAndNext(Actor.TICK);
            updateQuickslot();
        }

        @Override
        public String prompt() {
            return Messages.get(GoldIronHammer.this, "blast");
        }
    };
}