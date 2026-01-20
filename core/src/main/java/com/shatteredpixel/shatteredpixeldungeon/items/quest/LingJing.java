package com.shatteredpixel.shatteredpixeldungeon.items.quest;

import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class LingJing extends Item {

    {
        image = ItemSpriteSheet.LINGPEA;

        stackable = true;
        unique = true;
    }
    @Override
    public ArrayList<String> actions(Hero hero) {
        if(!SPDSettings.KillDragon()) {
            return new ArrayList<>(); //yup, no dropping this one
        } else {
            return super.actions(hero);
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
}

