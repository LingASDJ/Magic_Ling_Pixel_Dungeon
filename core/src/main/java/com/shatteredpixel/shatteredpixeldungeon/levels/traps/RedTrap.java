package com.shatteredpixel.shatteredpixeldungeon.levels.traps;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.watabou.noosa.audio.Sample;

public class RedTrap extends Trap {

    {
        color = TEAL;
        shape = DOTS;
    }

    @Override
    public void activate() {

        CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        Sample.INSTANCE.play( Assets.Sounds.TELEPORT );

        Char ch = Actor.findChar( pos);
        if (ch != null && !ch.flying) {
            if (ch instanceof Hero) {
                ScrollOfTeleportation.teleportChar((Hero) ch);
            } else {
                int count = 20;
                int pos;
                do {
                    pos = Dungeon.level.randomRespawnCell( ch );
                    if (count-- <= 0) {
                        break;
                    }
                } while (pos == -1 || Dungeon.level.secret[pos]);

                    ch.pos = pos;
                    if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING) {
                        ((Mob) ch).state = ((Mob) ch).WANDERING;
                    }
                    ch.sprite.place(ch.pos);
                    ch.sprite.visible = Dungeon.level.heroFOV[pos];

            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);

        if (heap != null){
            int cell = Dungeon.level.randomRespawnCell( null );

            Item item = heap.pickUp();

            if (cell != -1) {
                Heap dropped = Dungeon.level.drop( item, cell );
                dropped.type = heap.type;
                dropped.sprite.view( dropped );
            }
        }
    }
}

