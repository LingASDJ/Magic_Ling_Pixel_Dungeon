package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulPlusSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GhoulPlus extends Mob {

    private int permanentHPBonus = 0;
    private int permanentAttackBonus = 0;
    private int permanentDefenseBonus = 0;

    {
        spriteClass = GhoulPlusSprite.class;

        HP = HT = 100;
        defenseSkill = 10;

        EXP = 20;
        maxLvl = 20;

        loot = Gold.class;
        lootChance = 1f;

        WANDERING = new Wandering();

        properties.add(Property.UNDEAD);
    }

    public void gainBuffFromGhoulDeath() {
        HP += 50;
        if (HP > HT) {
            HP = HT;
        }
        permanentHPBonus += 30;
        permanentAttackBonus += 10;
        permanentDefenseBonus += 5;
        HT = 100 + permanentHPBonus;
    }

    @Override
    public void damage(int damage, Object src, DamageType type){
        int nearbyGhouls = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Ghoul) {
                int distance = Dungeon.level.distance(pos, mob.pos);
                if (distance <= 2) {
                    nearbyGhouls++;
                }
            }
        }

        // 计算减伤（每只尸群15%减伤）
        float damageReduction = 1.0f - (nearbyGhouls * 0.15f);
        damageReduction = Math.max(0.1f, damageReduction); // 至少保留10%伤害

        int reducedDamage = Math.round(damage * damageReduction);
        super.damage(reducedDamage, src, type);
    }

    @Override
    public int damageRoll() {
        int baseDamage = Random.NormalIntRange(15, 20);

        int nearbyGhouls = 0;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Ghoul) {
                int distance = Dungeon.level.distance(pos, mob.pos);
                if (distance <= 2) {
                    nearbyGhouls++;
                }
            }
        }

        float damageBonus = 1.0f + (nearbyGhouls * 0.15f);

        return Math.round(baseDamage * damageBonus + permanentAttackBonus);
    }

    @Override
    public Item createLoot(){
        if (Dungeon.hero.lvl > maxLvl + 2){
            Gold gold = new Gold();
            gold.quantity(gold.random_4X());
        }
        return super.createLoot();
    }

    @Override
    public int attackSkill( Char target ) {
        return 25;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 3) + permanentDefenseBonus;
    }

    public class Wandering extends Mob.Wandering {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (enemyInFOV) {
                enemySeen = true;
                alerted = true;
                state = HUNTING;
                target = enemy.pos;
            } else {
                enemySeen = false;

                int oldPos = pos;
                int minDistance = 5;
                int nearestPos = -1;

                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof Ghoul) { // 确保不是自己
                        int distance = Dungeon.level.distance(pos, mob.pos);
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestPos = mob.pos;
                        }
                    }
                }
                
                if (nearestPos == -1) {
                    return super.act(false, justAlerted);
                } else {
                    target = nearestPos;

                    if (getCloser(target)) {
                        spend(1 / speed());
                        return moveSprite(oldPos, pos);
                    } else {
                        // 如果无法移动到目标位置（例如被阻挡），停留一回合
                        spend(TICK);
                    }
                }
            }

            return true;
        }
    }

    private static final String PERMANENT_HP_BONUS = "permanent_hp_bonus";
    private static final String PERMANENT_ATTACK_BONUS = "permanent_attack_bonus";
    private static final String PERMANENT_DEFENSE_BONUS = "permanent_defense_bonus";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(PERMANENT_HP_BONUS, permanentHPBonus);
        bundle.put(PERMANENT_ATTACK_BONUS, permanentAttackBonus);
        bundle.put(PERMANENT_DEFENSE_BONUS, permanentDefenseBonus);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        permanentHPBonus = bundle.getInt(PERMANENT_HP_BONUS);
        permanentAttackBonus = bundle.getInt(PERMANENT_ATTACK_BONUS);
        permanentDefenseBonus = bundle.getInt(PERMANENT_DEFENSE_BONUS);
        HT = 100 + permanentHPBonus;
    }

}
