package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.MintPlot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MintSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Mint extends NTNPC {

    {
        spriteClass = MintSprite.class;
        properties.add(Property.IMMOVABLE);
        properties.add(Property.UNKNOWN);
        maxLvl = -1;
    }

    @Override
    public String name() {
        if (first){
            return Messages.get(Mint.class, "n");
        } else {
            return super.name();
        }
    }

    @Override
    public String info(){



        if (first){
            return Messages.get(Mint.class, "d");
        } else {
            return super.info();
        }
    }

    private boolean first=true;
    private boolean secnod=true;
    private boolean rd=true;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);

        if (first) {
            ((MintSprite)sprite).wakeUp();
            GameScene.scene.add(new Delayer(1.75f){
                @Override
                protected void onComplete() {
                    ((MintSprite)sprite).idleS();
                }
            });
            first = false;
            sprite.showStatus(Window.CYELLOW, "!!!");
            Statistics.CatFirst = true;
        } else if(secnod){


            MintPlot plot = new MintPlot();
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            secnod = false;
        } else {
            yell(Messages.get(this,"x"));
        }
        return true;
    }



}
