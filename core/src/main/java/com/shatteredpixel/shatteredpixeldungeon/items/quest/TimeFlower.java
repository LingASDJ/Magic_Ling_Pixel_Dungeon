package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.HiroFlowerLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TimeFlower extends Item {

    private static final String AC_ACTIVE = "active";

    public int used;

    public boolean powerFlower = false;

    {
        image = ItemSpriteSheet.FLOWERS;
        cursed = false;
        defaultAction = AC_ACTIVE;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_ACTIVE);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_ACTIVE)){
            if(!(Dungeon.level instanceof HiroFlowerLevel)){
                if(used < 3){
                    Buff.detach(hero, Slow.class);
                    Buff.affect(hero, Swiftthistle.TimeBubble.class).setLeft(powerFlower ? 10 * (7- (float) Dungeon.depth /5) : 6f);
                    GLog.p(Messages.get(this,"used"));
                }
                if(used >= 3){
                    detach(hero.belongings.backpack);
                    Dungeon.level.drop(new DeepRedFlower(), hero.pos).sprite.drop(hero.pos);
                }
                used++;
            } else {
                GLog.w(Messages.get(this,"not_used"));
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFCC );

    @Override
    public ItemSprite.Glowing glowing() {
        return powerFlower ? WHITE : null;
    }

    private static final String BLESSED = "blessed";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( BLESSED, powerFlower );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        powerFlower	= bundle.getBoolean( BLESSED );
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}
