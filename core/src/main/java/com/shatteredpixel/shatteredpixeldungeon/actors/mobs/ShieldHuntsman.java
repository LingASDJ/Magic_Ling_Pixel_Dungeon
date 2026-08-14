//
// Decompiled by Jadx - 787ms
//
package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.custom.messages.M;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.BallisticaFloat;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.GME;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.MissileSpriteCustom;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfShielding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.EndingBlade;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShieldHuntsmanSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ShieldHuntsman extends Mob {

    public ShieldHuntsman() {
        this.spriteClass = ShieldHuntsmanSprite.class;
        this.HT = HP = 72;
        this.defenseSkill = 15;
        this.EXP = 19;
        this.state = this.SLEEPING;
        this.loot = new PotionOfShielding();
        this.lootChance = 0.1667f;
        baseSpeed = 0.8f;
        maxLvl = 25;
    }

    @Override
    public float lootChance(){
        return super.lootChance() * ((2f - Dungeon.LimitedDrops.HUNR_HP.count) / 2f);
    }

    protected int hitsToDisarm = 0;

    @Override
    public int attackProc(Char enemy, int damage){
        if (Random.Int(0, 10) > 7) {
            this.sprite.showStatus(16711680, Messages.get(this,"attack_msg_"+Random.IntRange(1, 7)));
        }
        if (Random.Int(10)== 1) {
            //如果结果大于7 那么触发下面的行动
            Buff.prolong(enemy, Paralysis.class, Random.Float(1.0f, 2.0f));
            enemy.sprite.emitter().burst(Speck.factory(2), 12);
            Buff.affect(enemy, Burning.class ).reignite( enemy, 8f );
            GLog.n(M.L(this, "tlk"));
        }
        if (enemy == Dungeon.hero) {

            Hero hero = Dungeon.hero;
            KindOfWeapon weapon = hero.belongings.weapon;
            Armor armor = hero.belongings.armor;
            KindofMisc misc = hero.belongings.misc;

            switch (Random.Int(3)){
                case 0:
                    if (weapon != null) {
                        if(!(weapon instanceof EndingBlade)){
                            if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(5, 7);

                            if (--hitsToDisarm == 0) {
                                hero.belongings.weapon = null;
                                Dungeon.quickslot.convertToPlaceholder(weapon);
                                Item.updateQuickslot();
                                GLog.n(M.L(this, "disarm", weapon.name()));

                                BallisticaFloat ba = new BallisticaFloat(hero.pos, GME.angle(pos, hero.pos) + Random.Float(-22.5f, 22.5f), 6, Ballistica.PROJECTILE);
                                ((MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class)).
                                        reset(hero.sprite,
                                                ba.collisionPosI,
                                                weapon, 0.6f, 1.25f,
                                                new Callback() {
                                                    @Override
                                                    public void call() {
                                                        Dungeon.level.drop(weapon, ba.collisionPosI).sprite.drop();
                                                    }
                                                });
                            }
                        }

                    }
                    break;
                    case 1:
                    if(armor != null){
                        if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(5, 7);

                        if (--hitsToDisarm == 0) {
                            hero.belongings.armor = null;
                            Dungeon.quickslot.convertToPlaceholder(armor);
                            Item.updateQuickslot();
                            GLog.n(M.L(this, "disarm", armor.name()));

                            BallisticaFloat ba = new BallisticaFloat(hero.pos, GME.angle(pos, hero.pos) + Random.Float(-22.5f, 22.5f), 6, Ballistica.PROJECTILE);
                            ((MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class)).
                                    reset(hero.sprite,
                                            ba.collisionPosI,
                                            armor, 0.6f, 1.25f,
                                            new Callback() {
                                                @Override
                                                public void call() {
                                                    Dungeon.level.drop(armor, ba.collisionPosI).sprite.drop();
                                                }
                                            });
                        }
                    }
                    break;
                    case 2:
                    if (misc != null) {
                        if (hitsToDisarm == 0) hitsToDisarm = Random.NormalIntRange(5, 7);

                        if (--hitsToDisarm == 0) {
                            hero.belongings.misc = null;
                            Dungeon.quickslot.convertToPlaceholder(misc);
                            Item.updateQuickslot();
                            GLog.n(M.L(this, "disarm", misc.name()));

                            BallisticaFloat ba = new BallisticaFloat(hero.pos, GME.angle(pos, hero.pos) + Random.Float(-22.5f, 22.5f), 6, Ballistica.PROJECTILE);
                            ((MissileSpriteCustom) hero.sprite.parent.recycle(MissileSpriteCustom.class)).
                                    reset(hero.sprite,
                                            ba.collisionPosI,
                                            misc, 0.6f, 1.25f,
                                            new Callback() {
                                                @Override
                                                public void call() {
                                                    Dungeon.level.drop(misc, ba.collisionPosI).sprite.drop();
                                                }
                                            });
                        }
                    }
                    break;
            }

        }
        return super.attackProc(enemy, damage);
    }

    private static String FOCUS_COOLDOWN = "focus_cooldown";

    @Override
    public void storeInBundle(Bundle b){
        super.storeInBundle(b);
        b.put("hitToDisarm", hitsToDisarm);
        b.put( FOCUS_COOLDOWN, focusCooldown );
    }
    @Override
    public void restoreFromBundle(Bundle b){
        super.restoreFromBundle(b);
        hitsToDisarm = b.getInt("hitToDisarm");
        focusCooldown = b.getInt( FOCUS_COOLDOWN );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 12, 25 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 50;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6);
    }

    protected float focusCooldown = 0;

    @Override
    protected boolean act() {
        boolean result = super.act();
        if (buff(Focus.class) == null && state == HUNTING && focusCooldown <= 0) {
            Buff.affect( this,Focus.class );
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
            if (sprite != null && sprite.visible) {
                Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
            }
            focusCooldown = Random.NormalFloat( 6, 7 );
            return Messages.get(this, "parried");
        }
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
            icon.hardlight(0.25f, 1.5f, 1f);
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }
    }
}
