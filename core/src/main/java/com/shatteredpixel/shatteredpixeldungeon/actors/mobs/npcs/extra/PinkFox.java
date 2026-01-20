package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.gold.PinkFoxPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PinkFoxSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndGoldBurrety;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class PinkFox extends NTNPC {

    {
        spriteClass = PinkFoxSprite.class;
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
        PinkFoxPlot plot = new PinkFoxPlot();
        PinkFoxPlot.PinkFoxSPlot plotb = new PinkFoxPlot.PinkFoxSPlot();
        if (first && Dungeon.depth == 1) {
            Statistics.PinkFox = true;
            ((PinkFoxSprite)sprite).wakeUp();
            GameScene.scene.add(new Delayer(1f){
                @Override
                protected void onComplete() {
                    ((PinkFoxSprite)sprite).idleS();
                }
            });
            first = false;
            sprite.showStatus(Window.CYELLOW, "!!!");

            Statistics.goldRefogreCount += 2;
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });

            Item t1;
            t1 = Generator.random(Generator.Category.TRINKET);
            t1.identify();
            if (t1.doPickUp(Dungeon.hero)){
                GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", t1.name())) );
            } else {
                Dungeon.level.drop(t1, pos+1);
            }

            Item t2;
            t2 = Generator.random(Generator.Category.TRINKET);
            t2.identify();
            if (t2.doPickUp(Dungeon.hero)){
                GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", t1.name())) );
            } else {
                Dungeon.level.drop(t2,pos-1);
            }

            Item t3;
            t3 = Generator.random(Generator.Category.TRINKET);
            t3.identify();
            if (t3.doPickUp(Dungeon.hero)){
                GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", t3.name())) );
            } else {
                Dungeon.level.drop(t3,Dungeon.hero.pos);
            }

            first=false;
        } else if(Statistics.goldRefogreCount > 0) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndGoldBurrety(PinkFox.this,null));
                }
            });
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plotb,false));
                }
            });
        }
        return true;
    }
}
