package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.tumulus;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NecroGuardSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class NecroGuard extends Mob {

    {
        spriteClass = NecroGuardSprite.class;
        properties.add(Property.TUMULUS);
        properties.add(Property.NECRO);
        HP = HT = 60;
        defenseSkill = 20;
        baseSpeed = 1f;
        EXP = 8;
        maxLvl = 15;
    }

    private boolean isLegionSpawn = false;
    private boolean legionSpawned = false;

    private static final String LEGION_SPAWN = "legion_spawn";
    private static final String LEGION_SPAWNED = "legion_spawned";

    public void resetLegion(){
        isLegionSpawn = false;
        legionSpawned = false;
        spawnLegionAlly();
    }

    private int getGuardBonus() {
        int bonus = 0;
        int range = 2;
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob == this) continue;
            if (!(mob instanceof NecroArcher) && !(mob instanceof NecroGuard)) continue;
            int dist = Dungeon.level.distance(pos, mob.pos);
            if (dist <= range) {
                bonus += Random.NormalIntRange(1, 4);
                if (mob instanceof NecroGuard) {
                    bonus += Random.NormalIntRange(1, 4);
                }
            }
        }
        return bonus;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange(16, 25);
    }

    @Override
    public int attackSkill(Char target) {
        return 30;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 6) + getGuardBonus();
    }

    private void spawnLegionAlly() {
        ArrayList<Integer> candidates = new ArrayList<>();
        int[] dirs = PathFinder.NEIGHBOURS8;
        for (int d : dirs) {
            int p = pos + d;
            if (Dungeon.level.passable[p]
                    && Actor.findChar(p) == null
                    && p != Dungeon.hero.pos) {
                candidates.add(p);
            }
        }
        if (candidates.isEmpty()) return;

        int spawnPos = Random.element(candidates);
        Mob ally;
        if (Random.Boolean()) {
            ally = new NecroArcher();
        } else {
            ally = new NecroGuard();
        }
        if (ally instanceof NecroArcher) {
            ((NecroArcher) ally).setLegionSpawn(true);
        } else {
            ((NecroGuard) ally).setLegionSpawn(true);
        }

        ally.pos = spawnPos;

        GameScene.add(ally);
        Dungeon.level.occupyCell(ally);
    }

    public void setLegionSpawn(boolean val) {
        isLegionSpawn = val;
    }

    @Override
    protected boolean act() {
        if (!legionSpawned && !isLegionSpawn) {
            spawnLegionAlly();
            legionSpawned = true;
        }
        return super.act();
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEGION_SPAWN, isLegionSpawn);
        bundle.put(LEGION_SPAWNED, legionSpawned);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        isLegionSpawn = bundle.getBoolean(LEGION_SPAWN);
        legionSpawned = bundle.getBoolean(LEGION_SPAWNED);
    }
}