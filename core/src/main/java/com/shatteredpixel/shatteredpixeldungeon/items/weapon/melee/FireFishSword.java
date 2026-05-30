package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.spdtomlpd.TragicCode;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class FireFishSword extends MeleeWeapon{

    {
        image = ItemSpriteSheet.FIREFISHSWORD;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1f;
        tier = 6;
        ACC = 2.90f; //20% boost to accuracy
        DLY = 1.6f; //2x speed
        cursed = true;
        enchant(Enchantment.randomCurse());
    }

    public static Weapon resetling(IceFishSword ingredient ) {
        FireFishSword result = new FireFishSword();
        result.quantity = ingredient.quantity();
        result.identify();
        result.level = ingredient.level;
        if(ingredient.customName != null){
            result.customName = ingredient.customName;
        }
        GLog.n(Messages.get( FireFishSword.class, "resetling"),result.name());
        return result;
    }

    @Override
    public int min(int lvl) {
        return 2 + lvl * 3;
    }

    @Override
    public int max(int lvl) {
        return 6 + lvl * 7;
    }


    public void bolt(Integer target, final Mob mob){
        if (target != null) {

            final Ballistica shot = new Ballistica( Dungeon.hero.pos, target, Ballistica.PROJECTILE);

            fx(shot, new Callback() {
                public void call() {
                    onHit(shot, mob);
                }
            });
        }
    }

    protected void onHit(Ballistica bolt, Mob mob) {

        //presses all tiles in the AOE first

        if (mob != null){

            if (mob.isAlive() && bolt.path.size() > bolt.dist+1) {
                Buff.prolong( mob, Hex.class, Hex.DURATION );
                Buff.affect(mob, Burning.class).reignite(mob, 8f);
            }
        }

    }

    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( Dungeon.hero.sprite.parent, MagicMissile.FORCE, Dungeon.hero.sprite, bolt.collisionPos,
                callback);
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
        int dmgBoost = augment.damageFactor(7 + buffedLvl());
        TragicCode.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? (7 + buffedLvl()) : 7;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(min()+dmgBoost), augment.damageFactor(max()+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+dmgBoost, max(0)+dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 7 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

}

