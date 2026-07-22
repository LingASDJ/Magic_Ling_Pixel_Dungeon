package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroWarlockSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NecroWarlock extends Mob {

    {
        HP = HT = 60;
        defenseSkill = 18;
        EXP = 13;
        maxLvl = 21;

        spriteClass = NecroWarlockSprite.class;

        properties.add(Property.NECRO);
        properties.add(Property.TUMULUS);
        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(15, 30);
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 4);
    }

    // ========== 掉落机制 ==========

    private static final float BASE_LOOT_CHANCE = 0.5f;
    private static final float MIN_LOOT_CHANCE = 0.125f;
    private float currentLootChance = BASE_LOOT_CHANCE;

    @Override
    public void rollToDropLoot() {
        if (Dungeon.hero.lvl > maxLvl + 2) return;

        if (Random.Float() < currentLootChance) {
            Item loot = createPotionLoot();
            if (loot != null) {
                Dungeon.level.drop(loot, pos).sprite.drop();
                currentLootChance = Math.max(currentLootChance / 2f, MIN_LOOT_CHANCE);
            }
        }

        super.rollToDropLoot();
    }

    private Item createPotionLoot() {
        Potion potion;
        do {
            potion = (Potion) Generator.randomUsingDefaults(Generator.Category.POTION);
        } while (potion instanceof PotionOfHealing
                || potion instanceof PotionOfStrength
                || potion instanceof PotionOfExperience);
        return potion;
    }

    // ========== 恶意秘术 ==========

    private static final int MALICE_EXTEND_MIN = 4;
    private static final int MALICE_EXTEND_MAX = 8;

    // FlavourBuff 子类：可以用 Buff.affect(target, Class, float)
    @SuppressWarnings("unchecked")
    private static final Class<? extends FlavourBuff>[] FLAVOUR_DEBUFFS = new Class[]{
            Slow.class,
            Paralysis.class,
            Weakness.class,
            Vulnerable.class,
            Cripple.class,
            Terror.class,
            Vertigo.class,
            Degrade.class,
            Frost.class
    };

    // 非 FlavourBuff：需要单独实例化
    // Burning, Poison

    @Override
    public int attackProc(Char enemy, int damage) {
        if (enemy == null || !enemy.isAlive()) return super.attackProc(enemy, damage);
        applyMaliceSecret(enemy);
        return super.attackProc(enemy, damage);
    }

    private void applyMaliceSecret(Char target) {
        boolean hasNegativeBuff = false;
        float extendTurns = Random.NormalIntRange(MALICE_EXTEND_MIN, MALICE_EXTEND_MAX);

        // 遍历目标所有buff，延长负面状态
        for (Buff b : target.buffs()) {
            if (isNegativeBuff(b)) {
                hasNegativeBuff = true;
                prolongBuff(target, b, extendTurns);
            }
        }

        if (hasNegativeBuff) {
            if (Dungeon.level.heroFOV[target.pos]) {
                target.sprite.emitter().burst(ShadowParticle.CURSE, 3);
                Sample.INSTANCE.play(Assets.Sounds.DEGRADE);
            }
        } else {
            // 无负面状态：随机添加4种
            applyRandomDebuffs(target, extendTurns);

            if (Dungeon.level.heroFOV[target.pos]) {
                target.sprite.emitter().burst(ShadowParticle.CURSE, 5);
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
            }
        }
    }

    /**
     * 随机施加4种负面状态
     */
    private void applyRandomDebuffs(Char target, float duration) {
        ArrayList<Runnable> available = new ArrayList<>();

        // FlavourBuff 类型
        for (Class<? extends FlavourBuff> buffClass : FLAVOUR_DEBUFFS) {
            if (target.buff(buffClass) == null) {
                available.add(() -> Buff.affect(target, buffClass, duration));
            }
        }

        // Burning：非 FlavourBuff
        if (target.buff(Burning.class) == null) {
            available.add(() -> Buff.affect(target, Burning.class).reignite(target, duration));
        }

        // Poison：非 FlavourBuff
        if (target.buff(Poison.class) == null) {
            available.add(() -> Buff.affect(target, Poison.class).set(duration));
        }

        int count = Math.min(4, available.size());
        Random.shuffle(available);

        for (int i = 0; i < count; i++) {
            available.get(i).run();
        }
    }

    /**
     * 延长指定buff的持续时间
     */
    private void prolongBuff(Char target, Buff buff, float duration) {
        if (buff instanceof Poison) {
            ((Poison) buff).set(duration);
        } else if (buff instanceof Burning) {
            ((Burning) buff).reignite(target, duration);
        } else if (buff instanceof FlavourBuff) {
            @SuppressWarnings("unchecked")
            Class<? extends FlavourBuff> buffClass = (Class<? extends FlavourBuff>) buff.getClass();
            Buff.prolong(target, buffClass, duration);
        }
    }

    private boolean isNegativeBuff(Buff buff) {
        return buff.type == Buff.buffType.NEGATIVE
                || buff instanceof Poison
                || buff instanceof Burning
                || buff instanceof Slow
                || buff instanceof Paralysis
                || buff instanceof Weakness
                || buff instanceof Vulnerable
                || buff instanceof Cripple
                || buff instanceof Terror
                || buff instanceof Vertigo
                || buff instanceof Degrade
                || buff instanceof Frost;
    }

    // ========== 恶骨机制 ==========

    public static class WickBoneDealy extends FlavourBuff {
        @Override
        public void detach() {
            super.detach();
            Buff.detach(target, WickedBone.class);
        }
    }

    public static class WickedBone extends Buff {
        {
            type = buffType.POSITIVE;
        }

        @Override
        public boolean attachTo(Char target) {
            if (target.buff(WickedBone.class) != null) {
                return false;
            }
            return super.attachTo(target);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", target.name());
        }

        @Override
        public int icon() {
            return BuffIndicator.CORRUPT;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x282A36);
        }

        @Override
        public void detach() {
            if (target instanceof Mob && !target.isAlive()) {
                explode((Mob) target);
            }
            super.detach();
        }

        private void explode(Mob source) {
            int damage = Math.round(source.damageRoll() * 0.5f);

            for (int i : PathFinder.NEIGHBOURS9) {
                int cell = source.pos + i;
                Char ch = Actor.findChar(cell);
                if (ch != null) {
                    ch.damage(damage, WickedBone.class, DamageType.REAL);
                    if (Dungeon.level.heroFOV[cell]) {
                        CellEmitter.center(cell).burst(Speck.factory(Speck.BONE), 2);
                    }
                }
            }

            if (Dungeon.level.heroFOV[source.pos]) {
                CellEmitter.center(source.pos).burst(Speck.factory(Speck.BONE), 4);
                Sample.INSTANCE.play(Assets.Sounds.BONES);
            }
        }
    }

    @Override
    protected boolean act() {
        applyWickedBone();
        return super.act();
    }

    private void applyWickedBone() {
        Mob closest = null;
        int closestDist = Integer.MAX_VALUE;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob == this) continue;
            if (mob.alignment == Alignment.ALLY) continue;
            if (!propertiesContainsNecro(mob)) continue;
            if (mob.buff(WickedBone.class) != null) continue;
            int dist = Dungeon.level.distance(pos, mob.pos);
            if (dist < closestDist) {
                closestDist = dist;
                closest = mob;
            }
        }

        if (closest != null) {
            Buff.affect(closest, WickedBone.class);
            Buff.affect(closest, WickBoneDealy.class, 100f);
            if (Dungeon.level.heroFOV[closest.pos]) {
                closest.sprite.emitter().burst(ShadowParticle.UP, 3);
                Sample.INSTANCE.play(Assets.Sounds.CURSED);
            }
        }
    }

    private boolean propertiesContainsNecro(Mob mob) {
        return mob.properties.contains(Property.NECRO)
                || mob.properties.contains(Property.UNDEAD);
    }
}