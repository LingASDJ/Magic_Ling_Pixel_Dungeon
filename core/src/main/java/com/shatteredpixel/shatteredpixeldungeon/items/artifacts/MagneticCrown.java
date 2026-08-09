package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
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
        if (hero.buff(MagicImmune.class) != null) return;
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

    @Override
    public boolean doEquip(Hero hero) {
        Buff.affect(hero, TrapWatch.class);
        return super.doEquip(hero);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        Buff.detach(hero, TrapWatch.class);
        return super.doUnequip(hero, collect, single);
    }

    public static class TrapWatch extends Buff {

        public Artifact getArtifact = null;

        public Artifact getEquippedArtifact(){
            if (getArtifact != null && getArtifact.isEquipped(Dungeon.hero)) return getArtifact;
            if (Dungeon.hero != null){
                Artifact art = Dungeon.hero.belongings.artifact();
                Item miscItem = Dungeon.hero.belongings.misc();
                if (art instanceof MagneticCrown) {
                    getArtifact = art;
                } else if (miscItem instanceof MagneticCrown) {
                    getArtifact = (Artifact) miscItem;
                } else {
                    getArtifact = null;
                }
            }
            return getArtifact;
        }

        @Override
        public boolean act() {
            spend(TICK);
            return true;
        }
//
        public void onTrapTrigger(int pos) {
            Hero hero = Dungeon.hero;
            if (!hero.fieldOfView[pos]) return;

            Artifact artifact = getEquippedArtifact();
            if(artifact != null){
                if(artifact instanceof MagneticCrown){
                    if (artifact.isEquipped(hero) && !artifact.cursed) {
                        ((MagneticCrown) artifact).onTrapTriggered();
                    }
                }
            }

        }
    }

    public class DragSelector extends CellSelector.Listener {
        private int range = (int)(3 + level * 0.5f);
        private Char targetChar;

        @Override
        public void onSelect(Integer target) {
            if (target == null) return;

            if (!Dungeon.hero.fieldOfView[target]) {
                GLog.w(Messages.get(MagneticCrown.this, "no_vision"));
                return;
            }

            int targetPos = target;
            float minDist = Float.MAX_VALUE;
            Char nearest = null;

            boolean heroRangeHasEnemy = false;
            for (Char ch : Actor.chars()) {
                if (Dungeon.hero.fieldOfView[ch.pos]
                        && ch.alignment == Char.Alignment.ENEMY
                        && !ch.properties().contains(Char.Property.IMMOVABLE)) {
                    if (Dungeon.level.distance(Dungeon.hero.pos, ch.pos) <= range) {
                        heroRangeHasEnemy = true;
                        break;
                    }
                }
            }

            if (heroRangeHasEnemy) {
                for (Char ch : Actor.chars()) {
                    if (!Dungeon.hero.fieldOfView[ch.pos]) continue;
                    if (ch.alignment != Char.Alignment.ENEMY) continue;
                    if (ch.properties().contains(Char.Property.IMMOVABLE)) continue;

                    float dist = Dungeon.level.distance(targetPos, ch.pos);
                    if (dist <= range) {
                        Ballistica trajectory = new Ballistica(ch.pos, targetPos, Ballistica.PROJECTILE);
                        if (trajectory.collisionPos == targetPos || !Dungeon.level.solid[trajectory.collisionPos]) {
                            if (dist < minDist) {
                                minDist = dist;
                                nearest = ch;
                            }
                        }
                    }
                }
                if (nearest == null) {
                    GLog.w(Messages.get(MagneticCrown.this, "no_enemy_in_range"));
                    return;
                }
            } else {
                for (Char ch : Actor.chars()) {
                    if (!Dungeon.hero.fieldOfView[ch.pos]) continue;
                    if (ch.properties().contains(Char.Property.IMMOVABLE) || ch.properties().contains(Char.Property.NPC)) continue;

                    float dist = Dungeon.level.distance(targetPos, ch.pos);
                    if (dist <= range) {
                        Ballistica trajectory = new Ballistica(ch.pos, targetPos, Ballistica.PROJECTILE);
                        if (trajectory.collisionPos == targetPos || !Dungeon.level.solid[trajectory.collisionPos]) {
                            if (dist < minDist) {
                                minDist = dist;
                                nearest = ch;
                            } else if (dist == minDist) {
                                if (getPriority(ch) > getPriority(nearest)) {
                                    nearest = ch;
                                }
                            }
                        }
                    }
                }
                if (nearest == null) {
                    GLog.w(Messages.get(MagneticCrown.this, "no_target"));
                    return;
                }
            }

            charge--;
            updateQuickslot();
            Talent.onArtifactUsed(Dungeon.hero);

            Ballistica trajectory = new Ballistica(nearest.pos, targetPos, Ballistica.PROJECTILE);
            int newPos = trajectory.collisionPos;

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
                Actor.add(new Pushing(nearest, nearest.pos, newPos));
                nearest.pos = newPos;
                Dungeon.level.occupyCell(nearest);
                if(!(nearest instanceof Hero)){
                    Buff.affect(nearest,Roots.class,1f);
                }
                Dungeon.hero.spendAndNext(1f);
            }

            CellEmitter.get(nearest.pos).burst(ShadowParticle.UP, 5);
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        }

        @Override
        public String prompt() {
            range = (int)(3 + level * 0.5f);
            targetChar = Dungeon.hero;
            showRange(Dungeon.hero.pos, range, Window.ORAGNECOLOR);

            java.util.Set<Char> charSet = Actor.chars();
            for (Char ch : charSet) {
                if (ch == null) continue;
                if (Dungeon.hero.fieldOfView[ch.pos]
                        && ch.alignment == Char.Alignment.ENEMY
                        && !ch.properties().contains(Char.Property.IMMOVABLE)
                        && Dungeon.level.distance(Dungeon.hero.pos, ch.pos) <= range) {
                    showRange(ch.pos, range, 0xff0000);
                }
            }
            return Messages.get(MagneticCrown.this, "prompt");
        }

        private void showRange(int center, int range, int color) {
            for (int i = 0; i < Dungeon.level.length(); i++) {
                if (Dungeon.level.distance(center, i) == range) {
                    Game.scene().addToFront(new ColorTargetedCell(i, color));
                }
            }
        }

        private int getPriority(Char ch) {
            if (ch.alignment == Char.Alignment.ENEMY) return 3;
            else if (ch == Dungeon.hero) return 2;
            else if (ch.alignment == Char.Alignment.ALLY) return 1;
            else return 0;
        }
    }

    public class Recharge extends ArtifactBuff {
        private float partialCharge = 0f;

        @Override
        public boolean act() {
            if (charge < chargeCap && !cursed) {
                if (Regeneration.regenOn() || target.buff(MagicImmune.class) == null) {
                    float chargeToGain = 1f / (50f - level());
                    chargeToGain *= RingOfEnergy.artifactChargeMultiplier(target);
                    partialCharge += chargeToGain;

                    while (partialCharge >= 1f) {
                        charge++;
                        partialCharge -= 1f;
                        if (charge >= chargeCap) {
                            partialCharge = 0;
                            break;
                        }
                    }
                }
            }
            updateQuickslot();
            spend(TICK);
            return true;
        }
    }

    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;

        if (charge < chargeCap) {
            if (!isEquipped(target)) amount *= 0.75f;
            partialCharge += 0.25f*amount;
            while (partialCharge >= 1f) {
                charge++;
                partialCharge--;
            }
            if (charge >= chargeCap){
                partialCharge = 0;
                charge = chargeCap;
            }
            updateQuickslot();
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
        int expNeeded = 50 + 7 * level();
        while (exp >= expNeeded && level() < levelCap) {
            exp -= expNeeded;
            upgrade();
            GLog.p(Messages.get(this, "levelup"));
            expNeeded = 50 + 7 * level();
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