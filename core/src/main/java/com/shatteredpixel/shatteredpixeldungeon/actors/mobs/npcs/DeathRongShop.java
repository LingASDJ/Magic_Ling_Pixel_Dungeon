package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DeathRongSprite;

public class DeathRongShop extends Shopkeeper {

    {
        spriteClass = DeathRongSprite.class;
        properties.add(Property.BOSS);
        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {

        if (Dungeon.level.heroFOV[pos]){
            Notes.add(Notes.Landmark.SHOP);
        }
        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 0;
    }

}

