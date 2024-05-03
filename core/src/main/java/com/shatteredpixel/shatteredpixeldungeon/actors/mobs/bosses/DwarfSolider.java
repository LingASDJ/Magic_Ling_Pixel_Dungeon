package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.BrokenArmorFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gauntlet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfSoliderSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DwarfSolider extends Mob {

    {
        spriteClass = DwarfSoliderSprite.class;

        HP = HT = 60;
        defenseSkill = 30;

        EXP = 11;
        maxLvl = -21;

        loot = new Food();
        lootChance = 0.083f;
        HUNTING = new Hunting();
        immunities.add(BrokenArmorFire.class);
        properties.add(Property.UNDEAD);
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 24, 36 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 120;
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*0.75f;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(3, 6);
    }

    protected float focusCooldown = 0;

    @Override
    public void damage(int dmg, Object src) {
        super.damage(dmg, src);
        LockedFloor lock = hero.buff(LockedFloor.class);
        if (lock != null){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg);
            else                                                    lock.addTime(dmg*1.5f);
        }
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, Messages.get(this,"attack_msg_"+Random.IntRange(1, 7)));
        }
        int damage2 = super.attackProc(enemy, damage);
        if (enemy == Dungeon.hero) {
            int hitsToDisarm = Random.Int(0, 100);
            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;
            if (weapon != null && !(weapon instanceof Gauntlet) && !weapon.cursed && hitsToDisarm > 60) {
                hero.belongings.weapon = null;
                Dungeon.quickslot.convertToPlaceholder(weapon);
                KindOfWeapon.updateQuickslot();
                Dungeon.level.drop(weapon, hero.pos).sprite.drop();
                GLog.w(Messages.get(this,"kicked"));
            }
        }

        if(buff(DwarfSolider.Focus.class) != null){
            Buff.detach(DwarfSolider.this,DwarfSolider.Focus.class);
        }

        return damage2;
    }

    public class Hunting extends Mob.Hunting {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if (!enemyInFOV) {
                if(buff(DwarfSolider.Focus.class) != null){
                    Buff.detach(DwarfSolider.this,DwarfSolider.Focus.class);
                }
            }
            return super.act( enemyInFOV, justAlerted );
        }

    }

    @Override
    protected boolean act() {
        boolean result = super.act();
        if (buff(Focus.class) == null && state == HUNTING && focusCooldown <= 0) {
            Buff.affect( this, Focus.class );
        }
        return result;
    }

    @Override
    protected void spend( float time ) {
        focusCooldown -= time;
        super.spend( time );
    }

    @Override
    public void move( int step, boolean travelling) {
        // moving reduces cooldown by an additional 0.67, giving a total reduction of 1.67f.
        // basically monks will become focused notably faster if you kite them.
        if (travelling) focusCooldown -= 0.67f;
        super.move( step, travelling);
    }

    @Override
    public int defenseSkill( Char enemy ) {
        if (buff(Focus.class) != null && paralysed == 0 && state != SLEEPING){
            return INFINITE_EVASION;
        }
        return super.defenseSkill( enemy );
    }

    @Override
    public String defenseVerb() {
        Focus f = buff(Focus.class);
        if (f == null) {
            return super.defenseVerb();
        } else {
            f.detach();
            this.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(DwarfSolider.class, "future"));
            Buff.affect(this, Barrier.class).setShield( 20 );
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
            }
            focusCooldown = Random.NormalFloat( 20, 30 );
            return Messages.get(this, "parried");
        }
    }

    private static String FOCUS_COOLDOWN = "focus_cooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( FOCUS_COOLDOWN, focusCooldown );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        focusCooldown = bundle.getInt( FOCUS_COOLDOWN );
    }

    public static class Focus extends Buff {

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0x9932CC);
        }
    }
}

