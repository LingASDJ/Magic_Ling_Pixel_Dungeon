package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;

public class Nxhy extends Shopkeeper {

    {
        spriteClass = NxhySprite.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }
    private boolean seenBefore = false;
    @Override
    protected boolean act() {

        if (Dungeon.level.heroFOV[pos]){
            Notes.add(Notes.Landmark.SHOP);
        }
        throwItem();
        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            if (Dungeon.hero.buff(AscensionChallenge.class) != null) {
                yell(Messages.get(this, "talk_ascent", Messages.titleCase(Dungeon.hero.name())));
            }
            seenBefore = true;
        }
        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

}

