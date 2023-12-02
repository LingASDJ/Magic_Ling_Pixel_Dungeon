package com.shatteredpixel.shatteredpixeldungeon.items.books;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionHero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.MainBooks;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.noosa.particles.Emitter;

import java.util.ArrayList;

public class Books extends MainBooks {

    {
        defaultAction = Read;
        stackable = true;
    }



    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }


    public String desc = Messages.get(this, "desc");
    public Integer icon = null;
    private static final String Read	= "Read";
    public Emitter emitter() { return null; }

    public ItemSprite.Glowing glowing() {
        return null;
    }
    @Override
    public int value() {
        return quantity * 50;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(Read);
        return actions;
    }

    @Override
    public void execute(final Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals( Read )) {
            for (Buff b : hero.buffs(ChampionHero.class)) {
                if (b == null) {
                    Statistics.readBooks++;
                    Badges.valiReadBooks();
                }
            }
        }
    }

}
