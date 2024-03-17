package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.HollowKnightPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HollowKnightSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class HollowKnight extends NTNPC {
    private int died;

    private static final String DIED = "died";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DIED,died);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        died = bundle.getInt(DIED);
    }

    {
        spriteClass = HollowKnightSprite.class;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        HollowKnightPlot plot = new HollowKnightPlot();
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndDialog(plot,false));
            }
        });
        return true;
    }

    private String def_verb(){

        String ret;

        if(died<7)died++;

        //6次攻击后 立刻一个即死陷阱
        if(died>=6){
            hero.die(true);
            Dungeon.fail( getClass() );
            GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
        }

        switch(died){
            case 1:
                ret = Messages.get(this, "d1");
            break;
            case 2:
                ret = Messages.get(this, "d2");
            break;
            case 3:
                ret = Messages.get(this, "d3");
                break;
            case 4:
                ret = Messages.get(this, "d4");
                break;
            case 5:
                ret = Messages.get(this, "d5");
                break;
            default:
            case 6:
                ArrayList<Ankh> ankh = hero.belongings.getAllItems(Ankh.class);
                for (Ankh w : ankh.toArray(new Ankh[0])){
                    w.detachAll(hero.belongings.backpack);
                }
                ret = Messages.get(this, "d6");
                break;
        }
        return ret;
    }

    @Override
    public String defenseVerb() {
        return def_verb();
    }

}
