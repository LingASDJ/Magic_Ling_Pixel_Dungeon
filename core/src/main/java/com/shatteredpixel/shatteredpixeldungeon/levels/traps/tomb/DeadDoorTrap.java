package com.shatteredpixel.shatteredpixeldungeon.levels.traps.tomb;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tomb.DeadTowerRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DeadDoorTrap extends Trap {

    {
        color = TOMB;
        shape = WAVES;
        disarmedByActivation = false;
        avoidsHallways = true;
    }

    @Override
    public void activate() {

        int towerPos = -1;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof DeadTowerRoom.DeadTower) {
                towerPos = mob.pos;
                break;
            }
        }

        if (towerPos == -1) return;

        for (int i : PathFinder.NEIGHBOURS9) {
            int targetPos = pos + i;
            Char ch = Actor.findChar(targetPos);

            if (ch != null) {
                teleportToTower(ch, towerPos);
            }

            Heap heap = Dungeon.level.heaps.get(targetPos);
            if (heap != null && heap.type == Heap.Type.HEAP) {
                Item item = heap.pickUp();
                Dungeon.level.drop(item, towerPos);

                if (item instanceof Honeypot.ShatteredPot) {
                    ((Honeypot.ShatteredPot) item).movePot(targetPos, towerPos);
                }
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
    }

    /**
     * 将角色传送到死亡晶塔附近的安全位置
     */
    private void teleportToTower(Char ch, int towerPos) {
        // 在死亡晶塔周围找安全位置
        int[] neighbors = PathFinder.NEIGHBOURS8;
        int[] candidates = new int[neighbors.length];
        int count = 0;

        for (int n : neighbors) {
            int candidate = towerPos + n;
            if (Dungeon.level.insideMap(candidate)
                    && !Dungeon.level.solid[candidate]
                    && Actor.findChar(candidate) == null
                    && Dungeon.level.passable[candidate]) {
                candidates[count++] = candidate;
            }
        }

        int dest;
        if (count > 0) {
            dest = candidates[Random.Int(count)];
        } else {
            dest = towerPos;
        }

        ch.pos = dest;
        ScrollOfTeleportation.appear(ch, dest);
        if (ch == Dungeon.hero) {
            Dungeon.observe();
        }
    }
}