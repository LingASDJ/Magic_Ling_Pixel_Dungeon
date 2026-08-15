package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RedSwarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Swarm;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.ShubNiggurath;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.rare.BloodsSwarm;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.EndingBlade;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class KillSwarm extends Item {

    private static final String AC_KILL = "kill";

    public int count;

    {
        image = ItemSpriteSheet.KILL_SWARM;
        stackable = true;
        defaultAction = AC_KILL;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_KILL);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_KILL)){
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
                if (	mob instanceof Swarm ||
                        mob instanceof RedSwarm ||
                        mob instanceof BloodsSwarm ||
                        mob instanceof ShubNiggurath) {
                    mob.die( true );
                } else {
                    if(!Statistics.RandMode){
                        Buff.detach(mob, EndingBlade.TrialModeBuff.class);
                    }
                }
            }
            count++;
            if(count >= 2){
                detach( hero.belongings.backpack );
            }
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String COUNT = "count";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNT,count);
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        count = bundle.getInt(COUNT);
    }
}
