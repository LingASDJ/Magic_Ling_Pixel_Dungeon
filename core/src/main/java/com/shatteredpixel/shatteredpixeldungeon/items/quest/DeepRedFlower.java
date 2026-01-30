package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Hiro;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.HiroFlowerLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class DeepRedFlower extends Item {
    private static final String AC_ACTIVE = "active";

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
            if((Dungeon.level instanceof HiroFlowerLevel)) {
                for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                    if (mob instanceof Hiro) {
                        Hiro hiro = (Hiro) mob;
                        if(Dungeon.level.distance(hiro.pos, hero.pos) <= 1 && !hiro.flower){
                            hiro.flower = true;
                            detach(hero.belongings.backpack);
                            GLog.p(Messages.get(this,"used"));
                        } else {
                            GLog.w(Messages.get(this,"not_used"));
                        }
                    }
                }
            } else {
                GLog.w(Messages.get(this,"not_used"));
            }
        }
    }

//    @Override
//    public ItemSprite.Glowing glowing() {
//        return new ItemSprite.Glowing(Window.GDX_COLOR, 3f);
//    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

}
