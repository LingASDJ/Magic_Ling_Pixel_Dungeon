package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import static com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel.chinaHoliday;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfSnapFreeze;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TragicCode;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.thanks.GrilledHerring;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class IceFishSword extends MeleeWeapon {


    {
        image = ItemSpriteSheet.ICEFISHSWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 6;
        ACC = 1f; //20% boost to accuracy
        DLY = 1f; //2x speed
        cursed = true;
        enchant(Enchantment.random());
    }

    public static Weapon cook(FireFishSword ingredient ) {
        IceFishSword result = new IceFishSword();
        /** 传递数量，链接等级 自动鉴定 */
        result.quantity = ingredient.quantity();
        result.level = ingredient.level;
        result.identify();
        //双形态武器测试 如果有自定义名字 需要传递名字
        if(ingredient.customName != null){
            result.customName = ingredient.customName;
        }
        GLog.b(Messages.get( IceFishSword.class, "cook",result.name()));
        return result;
    }

    @Override
    public Item upgrade() {
        return upgrade(false);
    }


    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{PotionOfSnapFreeze.class, GrilledHerring.class, MagicalInfusion.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 16+Dungeon.depth/2;

            output = IceFishSword.class;
            outQuantity = 1;
        }

    }

    public void bolt(Integer target, final Mob mob){
        if (target != null) {

            final Ballistica shot = new Ballistica( Dungeon.hero.pos, target, Ballistica.PROJECTILE);

            fx(shot, () -> onHit(shot, mob));
        }
    }

    protected void onHit(Ballistica bolt, Mob mob) {

        //presses all tiles in the AOE first

        if (mob != null){

            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Buff.prolong(mob, Chill.class, Chill.DURATION/2f);
                Buff.affect(mob, Bleeding.class).set((float) (4));
            }
        }

    }

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( Dungeon.hero.sprite.emitter(), MagicMissile.WARD, Dungeon.hero.sprite,
                bolt.collisionPos,
                callback);
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl * 3;
    }

    @Override
    public int max(int lvl) {
        return 6 + lvl * 7;
    }



    public int proc(Char attacker, Char defender, int damage) {
        if(attacker instanceof Hero && Random.Int(10)==3){
            for(Mob mob : ((Hero) attacker).visibleEnemiesList()){
                bolt(mob.pos, mob);
            }
        }

        return super.proc(attacker, defender, damage);
    }

    @Override
    public int STRReq(int lvl) {
        int req = Dungeon.depth/10+16;
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public int value() {
        return chinaHoliday == RegularLevel.ChinaHoliday.CJ ? quantity * 320 : quantity * 500;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(TragicCode.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        int dmgBoost = augment.damageFactor(6 + buffedLvl());
        TragicCode.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? (6 + buffedLvl()) : 6;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 6 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}
