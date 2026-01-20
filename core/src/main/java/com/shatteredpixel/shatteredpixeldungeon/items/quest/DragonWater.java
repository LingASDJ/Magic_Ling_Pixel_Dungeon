package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.VialOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class DragonWater extends Item {

    {
        image = ItemSpriteSheet.Dragon_Lei;

        stackable = true;
        dropsDownHeap = true;
    }

    @Override
    public boolean doPickUp(Hero hero, int pos) {

        Waterskin flask = hero.belongings.getItem( Waterskin.class );
        Catalog.setSeen(getClass());

        if (flask != null && !flask.isFull()){

            flask.collectDragon( this );
            GameScene.pickUp( this, pos );

        } else {

            int terr = Dungeon.level.map[pos];
            if (!consumeDew(hero, terr == Terrain.ENTRANCE || terr == Terrain.ENTRANCE_SP
                    || terr == Terrain.EXIT || terr == Terrain.UNLOCKED_EXIT)){
                return false;
            } else {
                Catalog.countUse(getClass());
            }

        }

        Sample.INSTANCE.play( Assets.Sounds.DEWDROP );
        hero.spendAndNext( TIME_TO_PICK_UP );

        return true;
    }

    public static boolean consumeDew(Hero hero, boolean force){
        //20 drops for a full heal
        int quantity = 20;
        int heal = Math.round( hero.HT * 0.05f * quantity );

        int effect = Math.min( hero.HT - hero.HP, heal );
        int shield = 0;
        if (hero.hasTalent(Talent.SHIELDING_DEW)){
            shield = heal - effect;
            int maxShield = Math.round(hero.HT *0.2f*hero.pointsInTalent(Talent.SHIELDING_DEW));
            int curShield = 0;
            if (hero.buff(Barrier.class) != null) curShield = hero.buff(Barrier.class).shielding();
            shield = Math.min(shield, maxShield-curShield);
        }
        if (effect > 0 || shield > 0) {

            if (effect > 0 && quantity > 1 && VialOfBlood.delayBurstHealing()){
                Healing healing = Buff.affect(hero, Healing.class);
                healing.setHeal(effect, 0, VialOfBlood.maxHealPerTurn());
                healing.applyVialEffect();
            } else {
                hero.HP += effect;
                if (effect > 0){
                    hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(effect), FloatingText.HEALING);
                }
            }

            if (shield > 0) {
                Buff.affect(hero, Barrier.class).incShield(shield);
                hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(shield), FloatingText.SHIELDING );
            }

        } else if (!force) {
            GLog.i( Messages.get(Dewdrop.class, "already_full") );
            return false;
        }

        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    //max of one dew in a stack

    @Override
    public Item merge( Item other ){
        if (isSimilar( other )){
            quantity = 1;
            other.quantity = 0;
        }
        return this;
    }

    @Override
    public Item quantity(int value) {
        quantity = Math.min( value, 1);
        return this;
    }

}

