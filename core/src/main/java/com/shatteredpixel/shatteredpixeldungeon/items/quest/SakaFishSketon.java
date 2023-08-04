package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class SakaFishSketon extends Item {

    {
        image = ItemSpriteSheet.FISHSKELETON;
        stackable = true;
    }

    private static final String AC_INTER_TP = "interlevel_tp";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if(Dungeon.depth < 0) {
            actions.add(AC_INTER_TP);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_INTER_TP)){
            if(Dungeon.hero.buff(LockedFloor.class) != null) {
                GLog.w(Messages.get(this,"cannot_send"));
                return;
            }
            InterlevelScene.mode = InterlevelScene.Mode.RETURN;
            Game.switchScene(InterlevelScene.class);
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

    @Override
    public int value() {
        return quantity * 1250;
    }
}

