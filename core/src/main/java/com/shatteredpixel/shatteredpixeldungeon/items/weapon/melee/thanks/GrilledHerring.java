package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.thanks;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class GrilledHerring extends MeleeWeapon {

    private int eatFish = 3;
    public static final String AC_EATFISH	= "eatFish";

    {
        tier = 4;
        image = ItemSpriteSheet.GRILLED_FISH;
        hitSound = Assets.Sounds.HIT_STRONG;
        hitSoundPitch = 0.9f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if(eatFish<=0){
            Buff.affect( defender, Bleeding.class ).set((damage/2f));
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public int image() {
        if (eatFish <= 0) {
            super.image = ItemSpriteSheet.GRILLED_NOTE;
            tier = 3;
        } else {
            super.image = ItemSpriteSheet.GRILLED_FISH;
            tier = 4;
        }
        return image;
    }

    @Override
    public Item upgrade() {
        eatFish++;
        return super.upgrade();
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(isIdentified() && !cursed && eatFish>0){
            actions.add(AC_EATFISH);
        }

        return actions;
    }
    public static final float TIME_TO_EAT	= 3f;
    protected float eatingTime(){
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
                || Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)){
            return TIME_TO_EAT - 2;
        } else {
            return TIME_TO_EAT;
        }
    }
    public float energy = Hunger.HUNGRY;
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(AC_EATFISH)) {
            Buff.affect(hero, Hunger.class).satisfy(50f);
            eatFish--;
            GLog.i( Messages.get(this, "eat_msg") );

            hero.sprite.operate( hero.pos );
            hero.busy();
            SpellSprite.show( hero, SpellSprite.FOOD );
            Sample.INSTANCE.play( Assets.Sounds.EAT );

            hero.spend( eatingTime() );

            Talent.onFoodEaten(hero, energy, this);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        eatFish = bundle.getInt("EATFISH");
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("EATFISH", eatFish);
    }

    @Override
    public int min(int lvl) {
        return eatFish <=0 ? 3 + lvl : 4 + lvl;
    }

    @Override
    public int max(int lvl) {
        return eatFish <=0 ? 15 + lvl*3 : 25 + lvl*5;
    }

    @Override
    public String statsInfo(){
        if (eatFish<=0){
            return Messages.get(this, "stats_desc");
        } else {
            return Messages.get(this, "typical_stats_desc", eatFish);
        }
    }

}
