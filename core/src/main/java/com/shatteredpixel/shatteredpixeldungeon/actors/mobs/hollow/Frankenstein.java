package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElementalBuff.BaseBuff.ScaryBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FrankensteinSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Frankenstein extends Mob {

    private static int FIRST_DEATH_REVIVE_CHANCE = 50; // 50% chance for first death
    private static int SECOND_DEATH_REVIVE_CHANCE = 25; // 25% chance for second death
    private int deathCount = 0; // Counter for how many times the creature has died
    public boolean MustDied;

    public int RestCooldown;

    {
        spriteClass = FrankensteinSprite.class;
        baseSpeed = 1.2f;
        HP = HT = 90;
        EXP = 15;
        defenseSkill = 14;
        maxLvl = 34;
        properties.add(Char.Property.HOLLOW);
    }

    @Override
    public boolean act() {
        if(deathCount == 2 && RestCooldown != -10){
            RestCooldown--;
            sprite.showStatus(CharSprite.NEGATIVE, String.valueOf(RestCooldown));
            if(RestCooldown == 0){
                AnkhResetLive();
                RestCooldown = -10;
            }
        } else if(deathCount == 1 && RestCooldown != -10){
            RestCooldown--;
            sprite.showStatus(CharSprite.NEGATIVE, String.valueOf(RestCooldown));
            if(RestCooldown == 0){
                AnkhResetLive();
                RestCooldown = -10;
            }
        }

        return super.act();
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return state == PASSIVE;
    }

    @Override
    public boolean isAlive() {
        if (super.isAlive()) {
            return true; // If still alive according to the parent class
        }

        // Check resurrection chances
        if (deathCount == 0 && RestCooldown == 0) {
            if (Random.Int(100) <= FIRST_DEATH_REVIVE_CHANCE) {
                RestCooldown = 5;
                deathCount++;
                for (Buff b : buffs()) {
                   b.detach();
                }
                state = PASSIVE;
                return true;
            }
        } else if (deathCount == 1 && RestCooldown == 0) {
            if (Random.Int(100) <= SECOND_DEATH_REVIVE_CHANCE) {
                RestCooldown = 5;
                deathCount++;
                for (Buff b : buffs()) {
                    b.detach();
                }
                state = PASSIVE;
                return true;
            }
        }

        MustDied = true;
        return false;
    }

    public void AnkhResetLive() {
        HP = HT; // Reset HP to maximum
        if (Dungeon.level.heroFOV[pos]) {
            SpellSprite.show( this, SpellSprite.ANKH);
            new Flare( 5, 32 ).color(Window.CYELLOW, true ).show( sprite, 2f );
        }
        if(deathCount>0){
            maxLvl = -1;
        }
        state = HUNTING;
        spend(TICK); // Time spent on revival
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if(enemy!=null && enemy == hero) {
            for (Buff buff : hero.buffs()) {
                if (buff instanceof ScaryBuff) {
                    ((ScaryBuff) buff).damgeScary(10);
                } else {
                    Buff.affect(enemy, ScaryBuff.class).set((100), 5);
                }
            }
        }
        return damage; // Return adjusted damage
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(30, 45);
    }

    @Override
    public int attackSkill(Char target) {
        return 35; // Fixed attack skill
    }

    @Override
    public int drRoll() {
        return 0; // No damage reduction
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("FIRST_DEATH_REVIVE_CHANCE", FIRST_DEATH_REVIVE_CHANCE);
        bundle.put("SECOND_DEATH_REVIVE_CHANCE", SECOND_DEATH_REVIVE_CHANCE);
        bundle.put("deathCount", deathCount);
        bundle.put("MustDied", MustDied);
        bundle.put("CD",RestCooldown);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        FIRST_DEATH_REVIVE_CHANCE = bundle.getInt("FIRST_DEATH_REVIVE_CHANCE");
        SECOND_DEATH_REVIVE_CHANCE = bundle.getInt("SECOND_DEATH_REVIVE_CHANCE");
        deathCount = bundle.getInt("deathCount");
        MustDied = bundle.getBoolean("MustDied");
        RestCooldown = bundle.getInt("CD");
    }

}
