package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.FiveYearsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.FiveYearsTwoPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetYogFiveSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class YogSTSNewYears extends FiveYearsNPC {

    {
        spriteClass = YetYogFiveSprite.class;
        plot1 = new FiveYearsTwoPlot.YogSTSFiveYearsPlot();
        plot2 = new FiveYearsTwoPlot.YogSTSBRFiveYearsPlot();
        plot3 = new FiveYearsTwoPlot.YogSTSARFiveYearsPlot();
        plot4 = new FiveYearsTwoPlot.YogSTSEndFiveYearsPlot();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, c.pos );
        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1,false)));
            first = false;
        } else if(secnod){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                                           sprite(),
                                           Messages.titleCase(name()),
                                           Messages.get(YogSTSNewYears.class, "messages2"),
                                           Messages.get(YogSTSNewYears.class, "knows"),
                                           Messages.get(YogSTSNewYears.class, "money"),
                                           Messages.get(YogSTSNewYears.class, "wealth"),
                                           Messages.get(YogSTSNewYears.class, "all")
                                   ) {
                                       @Override
                                       protected void onSelect(int index) {
                                           if (index==0){
                                               Dungeon.level.drop(new ScrollOfDivination().identify(),hero.pos);
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
                                               secnod = false;
                                           } else if (index==1){
                                               Buff.affect(hero, Bless.class, 820f);
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
                                               secnod = false;
                                           } else if (index==2){
                                               Dungeon.level.drop(new Gold(315),hero.pos);
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
                                               secnod = false;
                                           } else if (index==3){
                                               hero.damage(hero.HP/2,this,DamageType.REAL);
                                               GameScene.flash(Window.GDX_COLOR);
                                               Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot3,false)));
                                               secnod = false;
                                           }
                                       }
                                   }
                    );
                }});
        } else {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot4,false)));
        }
        return true;
    }

}
