package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HolyBallBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HolyBallGreenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HolyBallRedSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

/**
 * 三圣祝福之礼生成的圣光球。
 *
 * <ul>
 *     <li>红球：需要被 1 次近战攻击打破。</li>
 *     <li>绿球：需要被 1 次投掷攻击打破。</li>
 *     <li>蓝球：需要被 1 次法杖攻击打破。</li>
 * </ul>
 *
 * <p>球不可移动、不会行动、也不参与 AI；被正确方式命中时立即碎裂并通知白鹭。
 * 碎裂不会走 {@code die()}，因此不会触发白鹭的死亡被动。</p>
 */
public class HolyBall extends Mob {

    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;

    private int colorType = RED;
    private Egret owner;
    private int ownerId = -1;
    private boolean broken = false;

    private static final String COLOR = "color";
    private static final String OWNER = "owner";

    public HolyBall() {
        spriteClass = HolyBallRedSprite.class;
        HP = HT = 1;
        defenseSkill = 0;
        baseSpeed = 0f;
        EXP = 0;
        maxLvl = 30;
        lootChance = 0f;
        properties.add(Property.IMMOVABLE);
        state = HUNTING;
    }

    public HolyBall init(int colorType, Egret owner) {
        this.colorType = colorType;
        this.spriteClass = spriteClassFor(colorType);
        this.owner = owner;
        this.ownerId = owner == null ? -1 : owner.id();
        return this;
    }

    private static Class<? extends CharSprite> spriteClassFor(int colorType) {
        switch (colorType) {
            case GREEN:
                return HolyBallGreenSprite.class;
            case BLUE:
                return HolyBallBlueSprite.class;
            case RED:
            default:
                return HolyBallRedSprite.class;
        }
    }

    public int colorType() {
        return colorType;
    }

    public String colorName() {
        switch (colorType) {
            case GREEN:
                return Messages.get(this, "green");
            case BLUE:
                return Messages.get(this, "blue");
            case RED:
            default:
                return Messages.get(this, "red");
        }
    }

    @Override
    protected boolean act() {
        if (owner == null && ownerId >= 0) {
            Actor a = Actor.findById(ownerId);
            if (a instanceof Egret) {
                owner = (Egret) a;
            }
        }
        if (broken || owner == null || !owner.isAlive()) {
           die(true);
            return true;
        }
        spend(TICK);
        return true;
    }

    @Override
    public void damage(int dmg, Object src, DamageType type) {
        if (broken) {
            return;
        }
        if (src == null) {
            src = this;
        }

        boolean correct;
        if (src instanceof Wand) {
            correct = colorType == BLUE;
        } else if (Dungeon.hero != null
                && Dungeon.hero.belongings.thrownWeapon instanceof MissileWeapon) {
            correct = colorType == GREEN;
        } else {
            correct = colorType == RED
                    && (src instanceof Hero
                    || src instanceof MeleeWeapon
                    || (Dungeon.hero != null
                    && Dungeon.hero.belongings.attackingWeapon() instanceof MeleeWeapon));
        }

        if (correct) {
            die(true);
        }
    }

    @Override
    public boolean isInvulnerable(Class effect) {
        return false;
    }

    @Override
    public String name() {
        return Messages.get(this, "name") + " · " + colorName();
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", colorName());
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COLOR, colorType);
        bundle.put(OWNER, owner == null ? ownerId : owner.id());
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        colorType = bundle.getInt(COLOR);
        spriteClass = spriteClassFor(colorType);
        if (bundle.contains(OWNER)) {
            ownerId = bundle.getInt(OWNER);
        }
        if (ownerId >= 0) {
            Actor a = Actor.findById(ownerId);
            if (a instanceof Egret) {
                owner = (Egret) a;
            }
        }
    }

    @Override
    public void die(Object cause) {
       super.die(cause);
       if (broken) {
           return;
       }
       broken = true;
       int color = colorType;
       Egret o = owner;
       if (o != null) {
           o.onHolyBallBroken(color);
       }
       Sample.INSTANCE.play(Assets.Sounds.SHATTER);
       if (sprite != null) {
           sprite.removeHealthIndicator();
           CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 10);
           sprite.die();
       }
    }

    @Override
    public void rollToDropLoot() {
        // no loot
    }
}
