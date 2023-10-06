package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.BGMPlayer.playBGM;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
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
            playBGM(Assets.SHOP, true);
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
    public int defenseSkill( Char enemy ) {
        return 0;
    }

    @Override
    public void add( Buff buff ) {
        flee();
    }


    public void flee() {
        destroy();
        Notes.remove(Notes.Landmark.SHOP);
        CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
        hero.sprite.burst(15597568, 9);
        sprite.killAndErase();
    }



    @Override
    public void destroy() {
        Actor.remove( this );
        HP = 0;
        for (Heap heap: Dungeon.level.heaps.valueList()) {
            if (heap.type == Heap.Type.FOR_SALE) {
                if (ShatteredPixelDungeon.scene() instanceof GameScene) {
                    CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY, 4);
                }
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

