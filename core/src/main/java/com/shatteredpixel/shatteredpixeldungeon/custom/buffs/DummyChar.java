package com.shatteredpixel.shatteredpixeldungeon.custom.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;

//It's called char, but it serves for buffs, not chars.

//Currently DummyChar is only used to hold "level buffs".
//Level buffs are buffs that not affected by chars.
//This kind of buff should be attached to a "static" char that is sealed in level, not existing chars.
//Or it would be problematic if the holder dies, or leaves the level.

//might be better to maintain a dummyChar array. Use id to distinguish between dcs.
public final class DummyChar extends Char {

    {
        alignment = Alignment.NEUTRAL;
    }

    @Override
    protected boolean act() {
        spend(TICK);
        if(buffs().isEmpty()){
            killDummyChar();
        }
        return true;
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public boolean canInteract(Char c) {
        return false;
    }

    //return the first DummyChar,
    public static DummyChar findDummyChar(){
        for(Char ch: Actor.chars()){
            if(ch instanceof DummyChar){
                return (DummyChar) ch;
            }
        }
        return null;
    }

    //return existing dummyChar, or create a new dummyChar
    public static DummyChar getDC(){
        for(Char ch: Actor.chars()){
            if(ch instanceof DummyChar){
                return (DummyChar) ch;
            }
        }
        DummyChar dc = new DummyChar();
        Actor.add(dc);
        return dc;
    }

    //remove existing dummyChar
    public static boolean killDummyChar() {
        DummyChar dc = findDummyChar();
        if (dc == null) {
            return false;
        }
        Actor.remove(dc);
        return true;
    }

}
