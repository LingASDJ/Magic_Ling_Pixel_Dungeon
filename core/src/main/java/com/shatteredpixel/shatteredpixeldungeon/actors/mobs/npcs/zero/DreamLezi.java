package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears.DreamLeziPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.extra.ScrollOfTeleTation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DreamSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DreamLezi extends FiveYearsNPC {

    {
        spriteClass = DreamSprite.class;
        plot1 = new DreamLeziPlot();
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

    private static String[] TXT_RANDOM = {Messages.get(DreamLezi.class,"roll1"),Messages.get(DreamLezi.class,"roll2"),};

    @Override
    public boolean interact(Char c) {
        if (c != hero) return true;

        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot1, false)));
            first = false;
        } else if(secnod){
           yell(Messages.get(this,"no_gold"));
           secnod = false;
        } else if(rd){
            Dungeon.gold -= 720;
            Dungeon.level.drop(new ScrollOfTeleTation(), hero.pos);
            rd = false;
        } else {
            yell(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }

        return true;
    }
}
