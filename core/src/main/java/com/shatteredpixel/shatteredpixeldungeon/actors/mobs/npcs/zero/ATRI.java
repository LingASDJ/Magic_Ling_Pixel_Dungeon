package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.ATRIPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RedCrab;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ATRISprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class ATRI extends NTNPC {

    {
        spriteClass = ATRISprite.class;
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

        sprite.turnTo(pos, Dungeon.hero.pos);

        ATRIPlot plot = new ATRIPlot();
        ATRIPlot.Second B_plot = new ATRIPlot.Second();
        ATRIPlot.EndLess C_plot = new ATRIPlot.EndLess();
        Food food = Dungeon.hero.belongings.getItem(Food.class);
        if(first){
            if (Dungeon.level.adjacent(pos, hero.pos)){
                int oppositeAdjacent = hero.pos + (hero.pos - pos);
                Ballistica trajectory = new Ballistica(hero.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(hero, trajectory, 100, true, true, getClass());
                Dungeon.hero.interrupt();
                GameScene.flash(Window.GDX_COLOR);
                //hero.damage(12,ATRI.class);
                if (enemy == Dungeon.hero && !enemy.isAlive()) {
                    Dungeon.fail( getClass() );
                    GLog.n( Messages.get(ATRI.class, "ondeath") );
                }
            }
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;

        } else if(secnod && food != null) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(B_plot,false));
                }
            });
            secnod = false;
            GameScene.scene.add(new Delayer(3f){
                @Override
                protected void onComplete() {
                    ArrayList<Food> food = hero.belongings.getAllItems(Food.class);
                    for (Food w : food.toArray(new Food[0])){
                        w.detach(hero.belongings.backpack);
                    }
                    Dungeon.level.drop( new RedCrab(), hero.pos );
                }
            });
        } else if(!secnod) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(C_plot,false));
                }
            });
        }

        return true;
    }
}