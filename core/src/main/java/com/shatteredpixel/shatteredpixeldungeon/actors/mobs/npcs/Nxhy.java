package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
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
        if (!seenBefore && Dungeon.level.heroFOV[pos]) {
            yell(Messages.get(this, "greetings", Dungeon.hero.name()));
            seenBefore = true;
        }
        if (Dungeon.level.heroFOV[pos]){
            Notes.add(Notes.Landmark.SHOP);
        }
        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public void damage( int dmg, Object src ) {
        flee();
    }

    @Override
    public void add( Buff buff ) {
        flee();
    }


    public void flee() {
        destroy();
        Notes.remove(Notes.Landmark.SHOP);
        sprite.killAndErase();
        CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
    }

    @Override
    public void destroy() {
        super.destroy();
        for (Heap heap: Dungeon.level.heaps.valueList()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
                if (heap.size() == 1) {
                    heap.destroy();
                } else {
                    heap.items.remove(heap.size()-1);
                    heap.type = Heap.Type.HEAP;
                }
            }
        }
    }
}

