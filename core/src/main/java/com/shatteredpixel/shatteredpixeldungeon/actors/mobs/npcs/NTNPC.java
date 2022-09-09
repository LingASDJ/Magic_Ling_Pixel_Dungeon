package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class NTNPC extends NPC {

    {

        properties.add(Property.IMMOVABLE);
    }

    protected ArrayList<String> chat = new ArrayList<>();
    protected ArrayList<String> endChat = new ArrayList<>();

    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 1000;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo( pos, Dungeon.hero.pos );
        if(Statistics.amuletObtained && !endChat.isEmpty()){
            WndQuest.chating(this,endChat);
        }else {
            WndQuest.chating(this, chat);
        }
        return true;
    }
}
