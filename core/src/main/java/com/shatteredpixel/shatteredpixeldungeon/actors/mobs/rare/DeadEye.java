package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.PurpleParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.KingBag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfDisintegration;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeadEyeSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DeadEye extends Mob {

    {
        spriteClass = DeadEyeSprite.class;

        HP = HT = 110;
        defenseSkill = 20;
        viewDistance = 8;

        EXP = 13;
        maxLvl = 26;

        flying = true;

        HUNTING = new Hunting();

        loot = new ScrollOfRetribution();
        lootChance = 1f;

        properties.add(Property.DEMONIC);
    }

    @Override
    protected boolean act() {
        if (beamCharged && state != HUNTING){
            beamCharged = false;
            sprite.idle();
        }
        if (beam == null && beamTarget != -1) {
            beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
            sprite.turnTo(pos, beamTarget);
        }
        if (beamCooldown > 0)
            beamCooldown--;
        return super.act();
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(22, 35);  // 攻击22-35
    }

    @Override
    public int attackSkill(Char target) {
        return 30;  // 命中30
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 10);  // 防御0-10
    }

    private Ballistica beam;
    private int beamTarget = -1;
    private int beamCooldown;
    public boolean beamCharged;

    @Override
    protected boolean canAttack(Char enemy) {
        // 增加视野范围判断
        if (beamCooldown == 0) {
            Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.STOP_SOLID);

            // 扩大视野范围判断
            if (enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos]
                    && (super.canAttack(enemy) || aim.subPath(1, aim.dist).contains(enemy.pos))){
                beam = aim;
                beamTarget = enemy.pos;
                return true;
            } else {
                return beamCharged;
            }
        } else {
            return super.canAttack(enemy);
        }
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if (beamCharged) {
            dmg /= 4;  // 蓄力期间免伤3/4
        }
        super.damage(dmg, src, type);
    }

    public void deathGaze() {
        if (!beamCharged || beamCooldown > 0 || beam == null)
            return;

        beamCharged = false;
        beamCooldown = Random.IntRange(4, 6);

        Invisibility.dispel(this);
        for (int pos : beam.subPath(1, beam.dist)) {
            if (Dungeon.level.flamable[pos]) {
                Dungeon.level.destroy(pos);
                GameScene.updateMap(pos);
            }

            Char ch = Actor.findChar(pos);
            if (ch == null) continue;

            if (hit(this, ch, true)) {
                int dmg = Random.NormalIntRange(30, 50);  // 死亡凝视30-50伤害
                dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
                ch.damage(dmg, new Eye.DeathGaze());

                // 如果是英雄，解离背包物品
                if (ch == Dungeon.hero) {
                    // 增加解离物品数量，根据难度调整
                    int itemsToDisintegrate = Random.IntRange(2, 3);
                    ArrayList<Item> toDisintegrate = new ArrayList<>();
                    ArrayList<String> destroyedItems = new ArrayList<>();

                    // 收集所有可解离的物品，包括背包和容器中的物品
                    collectDisintegratableItems(Dungeon.hero.belongings.backpack, toDisintegrate);

                    // 随机选择物品解离
                    for (int i = 0; i < itemsToDisintegrate && !toDisintegrate.isEmpty(); i++) {
                        Item item = Random.element(toDisintegrate);
                        item.detach(Dungeon.hero.belongings.backpack);
                        toDisintegrate.remove(item);
                        destroyedItems.add(item.name());  // 记录物品名称
                    }

                    // 通知玩家被摧毁的物品
                    if (!destroyedItems.isEmpty()) {
                        StringBuilder message = new StringBuilder(Messages.get(this, "disintegrate"));
                        message.append(" ");
                        for (int i = 0; i < destroyedItems.size(); i++) {
                            if (i > 0) {
                                message.append(i == destroyedItems.size() - 1 ?
                                        Messages.get(this, "and") : ", ");
                            }
                            message.append(destroyedItems.get(i));
                        }
                        GLog.w(message.toString());
                    }
                }

                if (Dungeon.level.heroFOV[pos]) {
                    ch.sprite.flash();
                    CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                }

                if (!ch.isAlive() && ch == Dungeon.hero) {
                    Badges.validateDeathFromEnemyMagic();
                    Dungeon.fail(this);
                    GLog.n(Messages.get(this, "deathgaze_kill"));
                }
            } else {
                ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
            }
        }

        beam = null;
        beamTarget = -1;
    }

    // 收集所有可解离的物品，包括背包和容器中的物品
    private void collectDisintegratableItems(Item container, ArrayList<Item> toDisintegrate) {
        if (container == null) return;

        // 如果是容器，递归收集其中的物品
        if (container instanceof Bag) {
            Bag bag = (Bag) container;
            for (Item item : bag.items) {
                collectDisintegratableItems(item, toDisintegrate);
            }
        } else {
            // 检查物品是否符合解离条件
            if (isDisintegratable(container)) {
                toDisintegrate.add(container);
            }
        }
    }

    // 检查物品是否符合解离条件
    private boolean isDisintegratable(Item item) {
        if (item == null || item.unique || item.isEquipped(Dungeon.hero))
            return false;
        return !(item instanceof Dewdrop) && !(item instanceof KingBag);
    }

    @Override
    protected boolean doAttack(Char enemy) {
        beam = new Ballistica(pos, beamTarget, Ballistica.STOP_SOLID);
        if (beamCooldown > 0 || (!beamCharged && !beam.subPath(1, beam.dist).contains(enemy.pos))) {
            return super.doAttack(enemy);
        } else if (!beamCharged) {
            ((DeadEyeSprite)sprite).charge(enemy.pos);
            spend(attackDelay() * 2f);  // 蓄力2回合
            beamCharged = true;
            return true;
        } else {
            spend(attackDelay());
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[beam.collisionPos]) {
                sprite.zap(beam.collisionPos);
                return false;
            } else {
                sprite.idle();
                deathGaze();
                return true;
            }
        }
    }

    //used so resistances can differentiate between melee and magical attacks
    public static class DeathGaze{}

    //generates an average of 1 dew, 0.25 seeds, and 0.25 stones
    @Override
    public Item createLoot() {
        Item loot;
        switch(Random.Int(4)){
            case 0: case 1: default:
                loot = new Dewdrop();
                int ofs;
                do {
                    ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
                } while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);
                if (Dungeon.level.heaps.get(pos+ofs) == null) {
                    Dungeon.level.drop(new Dewdrop(), pos + ofs).sprite.drop(pos);
                } else {
                    Dungeon.level.drop(new Dewdrop(), pos + ofs).sprite.drop(pos + ofs);
                }
                break;
            case 2:
                loot = Generator.randomUsingDefaults(Generator.Category.SEED);
                break;
            case 3:
                loot = Generator.randomUsingDefaults(Generator.Category.STONE);
                break;
        }
        return loot;
    }

    private static final String BEAM_TARGET     = "beamTarget";
    private static final String BEAM_COOLDOWN   = "beamCooldown";
    private static final String BEAM_CHARGED    = "beamCharged";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put( BEAM_TARGET, beamTarget);
        bundle.put( BEAM_COOLDOWN, beamCooldown );
        bundle.put( BEAM_CHARGED, beamCharged );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains(BEAM_TARGET))
            beamTarget = bundle.getInt(BEAM_TARGET);
        beamCooldown = bundle.getInt(BEAM_COOLDOWN);
        beamCharged = bundle.getBoolean(BEAM_CHARGED);
    }

    {
        resistances.add( WandOfDisintegration.class );
        resistances.add( Eye.DeathGaze.class );
        resistances.add( DisintegrationTrap.class );
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            //even if enemy isn't seen, attack them if the beam is charged
            if (beamCharged && enemy != null && canAttack(enemy)) {
                enemySeen = enemyInFOV;
                return doAttack(enemy);
            }
            return super.act(enemyInFOV, justAlerted);
        }
    }
}
