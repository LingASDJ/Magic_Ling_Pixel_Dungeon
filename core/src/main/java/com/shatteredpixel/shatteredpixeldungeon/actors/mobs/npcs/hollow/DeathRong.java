package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BoatPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.BoatPlot_End;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeathRongSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class DeathRong extends NTNPC {

    {
        spriteClass = DeathRongSprite.class;
        properties.add(Property.IMMOVABLE);
        flying = true;
    }

    public boolean first=true;
    public boolean secnod=true;
    public boolean rd = true;

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
    protected boolean act() {

        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, Dungeon.hero.pos);
        BoatPlot plot = new BoatPlot();

        BoatPlot_End plot2 = new BoatPlot_End();

        if(first){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot,false)));
        } else if(Statistics.defalult_deaddog) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
        }
        Bestiary.setSeen(DeathRong.class);

        return true;
    }

    public static void tell(String text) {
        Game.runOnRenderThread(() -> GameScene.show(new WndQuest(new DeathRong(), text)));
    }

}

