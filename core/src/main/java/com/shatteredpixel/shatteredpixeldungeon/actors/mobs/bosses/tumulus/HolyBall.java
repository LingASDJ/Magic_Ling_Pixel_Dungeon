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
 * <p>只有匹配的攻击方式才能击破：红球近战、绿球投掷、蓝球法杖。
 * 其他来源一律视为无敌，不会掉血、不会碎裂，也不会触发死亡被动。</p>
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
            // 施法结束/白鹭死亡时安静消失，不算被打破
            dissolve();
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

        if (!correct) {
            // 不匹配的来源不显示闪避，直接按无敌处理
            if (sprite != null) {
                sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
            }
            return;
        }

        shatter();
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

    /** 被正确方式击破：通知白鹭并移除。 */
    private void shatter() {
        if (broken) {
            return;
        }
        broken = true;

        if (sprite != null) {
            sprite.removeHealthIndicator();
            CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 10);
            sprite.die();
        }
        Sample.INSTANCE.play(Assets.Sounds.SHATTER);

        Egret o = owner;
        int color = colorType;
        Dungeon.level.mobs.remove(this);
        Actor.remove(this);

        if (o != null) {
            o.onHolyBallBroken(color);
        }
    }

    /** 安静消失：施法结束、白鹭死亡或清场时使用，不算击破。 */
    public void dissolve() {
        if (broken) {
            return;
        }
        broken = true;

        if (sprite != null) {
            sprite.removeHealthIndicator();
            sprite.killAndErase();
        }
        Dungeon.level.mobs.remove(this);
        Actor.remove(this);
    }

    @Override
    public void die(Object cause) {
        // 圣光球不是普通单位：任何死亡路径都只做安静消失，
        // 不触发死亡广播，也不计入三圣击破。
        dissolve();
    }

    @Override
    public void rollToDropLoot() {
        // no loot
    }
}
