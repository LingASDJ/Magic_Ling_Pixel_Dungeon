package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.lb;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.KindofMisc;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor.Glyph;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfCorrosion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon.Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

//前世的回想 低灯火死亡会导致金币和炼金能量和灯火全部丢失
//参考了MarshalldotEXE/Rivals-pixel-dungeond的部分源代码
public class BlackSoul extends Mob implements Callback {

    private static final float TIME_TO_ZAP	= 1f;
    public int gold;
    {
        spriteClass = ShadowSprite.class;
    }

    public MeleeWeapon weapon;
    public Armor armor;
    public KindofMisc misc1;
    public KindofMisc misc2;
    public Wand wand;
    public MissileWeapon missile;

    public BlackSoul() {
        super();

        int lvl = Math.min( 20, Dungeon.depth );

        flying = true;

        maxLvl = 45;

        //melee
        do {
            weapon = (MeleeWeapon)Generator.random(Generator.Category.WEAPON);
        } while (weapon.cursed);
        if (lvl >= 16) weapon.enchant(Enchantment.random());
        weapon.identify();

        //armor
        do {
            armor = (Armor)Generator.random(Generator.Category.ARMOR);
        } while (armor.cursed);
        if (lvl >= 8) armor.inscribe(Glyph.random());
        armor.identify();

        //misc1
        do {
            misc1 = (KindofMisc)Generator.random(Generator.Category.RING);
        } while (misc1.cursed);
        misc1.identify();

        //misc2
        do {
            misc2 = (KindofMisc)Generator.random(Generator.Category.RING);
        } while (misc2.cursed);
        misc2.identify();

        //wand
        do {
            wand = new WandOfCorrosion();
        } while (wand.cursed);
        wand.updateLevel();
        wand.curCharges = Math.min( 10, wand.maxCharges );
        wand.identify();

        //missile
        do {
            missile = (MissileWeapon)Generator.random(Generator.Category.MISSILE);
        } while (missile.cursed);

        properties.add(Property.ABYSS);

        HP = HT = (40 + 5*(hero.lvl-1) + hero.HTBoost)/2;
        defenseSkill = (int)(armor.evasionFactor( this, 7 + lvl ));

        EXP = lvl * 2;
        if (lvl >= 20) EXP = 100;
    }

    private static final String WEAPON	= "weapon";
    private static final String ARMOR	= "armor";
    private static final String MISC1	= "misc1";
    private static final String MISC2	= "misc2";
    private static final String WAND	= "wand";
    private static final String MISSILE	= "missile";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WEAPON, weapon );
        bundle.put( ARMOR, armor );
        bundle.put( MISC1, misc1 );
        bundle.put( MISC2, misc2 );
        bundle.put( WAND, wand );
        bundle.put( MISSILE, missile );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        weapon		= (MeleeWeapon)		bundle.get( WEAPON );
        armor		= (Armor)			bundle.get( ARMOR );
        misc1		= (KindofMisc)		bundle.get( MISC1 );
        misc2		= (KindofMisc)		bundle.get( MISC2 );
        wand		= (Wand)			bundle.get( WAND );
        missile		= (MissileWeapon)	bundle.get( MISSILE );
        BossHealthBar.assignBoss(this);
    }

    @Override
    public int damageRoll() {
        int dmg = 0;
        dmg += weapon.damageRoll( this );
        if (dmg < 0) dmg = 0;
        return dmg;
    }

    @Override
    public int drRoll() {
        int dr = 0;
        dr += Random.NormalIntRange( armor.DRMin(), armor.DRMax() );
        dr += Random.NormalIntRange( 0, weapon.defenseFactor( this ) );
        if (dr < 0) dr = 0;
        return dr;
    }

    @Override
    public int attackSkill( Char target ) {
        return (int)((12 + Dungeon.depth) * weapon.accuracyFactor( this,enemy ));
    }

    @Override
    public float attackDelay() {
        return super.attackDelay() * weapon.speedFactor( this );
    }

    @Override
    public float speed() {
        return armor.speedFactor( this, super.speed() );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack(enemy)
                || weapon.canReach(this, enemy.pos)
                || (new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos);
    }

    protected boolean doAttack( Char enemy ) {
        if (Dungeon.level.adjacent( pos, enemy.pos ) || weapon.canReach(this, enemy.pos)) {

            return super.doAttack( enemy );

        } else {
            boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];

            if (wand.curCharges > 0) {
                if (visible) {
                    sprite.zap( enemy.pos );
                } else {
                    zap();
                }
                wand.curCharges--;

            } else if (missile.quantity() > 0) {
                if (visible) {
                    sprite.toss( enemy.pos );
                } else {
                    toss();
                }
                missile.quantity--;
            }

            return !visible;
        }
    }

    private void zap() {
        spend( TIME_TO_ZAP );

        final Ballistica shot = new Ballistica( pos, enemy.pos, wand.collisionProperties(enemy.pos));

        wand.rivalOnZap( shot, this );
    }

    private void toss() {
        spend( TIME_TO_ZAP );

        if (hit( this, enemy, true )) {
            enemy.damage( this.missile.damageRoll(this), this.missile.getClass() );
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL, enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    public void onTossComplete() {
        toss();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        return weapon.proc( this, enemy, damage );
    }

    @Override
    public int defenseProc( Char enemy, int damage ) {
        damage = super.defenseProc( enemy, damage );
        return armor.proc( enemy, this, damage );
    }

    @Override
    public void damage( int dmg, Object src ) {
        super.damage( dmg, src );
        if (HP <= 0) {
            spend( TICK );
        }
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);
        
        if (gold > 0) {
            Dungeon.level.drop( new Gold(gold), pos ).sprite.drop();
        }
        Dungeon.level.drop( new LostBackpack(), pos).sprite.drop( pos );
        GameScene.bossSlain();

        yell(Messages.get(this, "ellipsis"));
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            yell(Messages.get(this, "question"));
        }
    }

    @Override
    public String info() {
        String desc = super.info();

        desc += Messages.get(this, "weapon", weapon.toString() );
        desc += Messages.get(this, "armor", armor.toString() );
        desc += Messages.get(this, "wand", wand.toString() );
        desc += Messages.get(this, "missile", missile.toString() );

        return desc;
    }

    {
        resistances.add( Grim.class );
        resistances.add( GrimTrap.class );
        resistances.add( ScrollOfRetribution.class );
        resistances.add( ScrollOfPsionicBlast.class );
        immunities.add( Amok.class );
        immunities.add( Corruption.class );
        immunities.add( Terror.class );
    }
}