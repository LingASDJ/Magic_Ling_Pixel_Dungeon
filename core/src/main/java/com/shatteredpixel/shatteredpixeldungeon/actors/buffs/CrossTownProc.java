package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.CrossDiedTower;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class CrossTownProc extends Buff {

    public int towerPosLing;
    private int stateLoop = 0;

    private void beamProc(Ballistica b) {
        for (int j : b.path) {
            if (j == b.sourcePos) continue;
            Char ch = findChar(j);
            if (ch != null) {
                if (ch.alignment == Char.Alignment.ENEMY) {
                    ch.damage(Random.IntRange(5, 19), CrossDiedTower.class);
                    Buff.affect(ch, Cripple.class, 2f);
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
            }
        }
    }

    @Override
    public boolean act() {
        PointF p = DungeonTilemap.raisedTileCenterToWorld(towerPosLing);
        if (stateLoop == 1) {
            stateLoop++;
            FloatingText.show(p.x, p.y, "+", 0x5580FF);
        } else if (stateLoop == 2) {
            int w = Dungeon.level.width();
            int[] tile = {w, -w, 1, -1};
            for (int i = 0; i < 4; ++i) {
                Ballistica b = new Ballistica(towerPosLing, towerPosLing + tile[i], Ballistica.STOP_SOLID);
                target.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                beamProc(b);
            }
            stateLoop++;
        } else if (stateLoop == 4) {
            stateLoop++;
            FloatingText.show(p.x, p.y, "x", 0xFF8055);
        } else if (stateLoop == 5) {
            int w = Dungeon.level.width();
            int[] tile = {w + 1, w - 1, -w + 1, -w - 1};
            for (int i = 0; i < 4; ++i) {
                Ballistica b = new Ballistica(towerPosLing, towerPosLing + tile[i], Ballistica.STOP_SOLID);
                target.sprite.parent.add(new Beam.LightRay(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                beamProc(b);
            }
            stateLoop++;
        } else if (stateLoop == 6) {
            stateLoop++;
            FloatingText.show(p.x, p.y, "S", 0x00ffff);
        } else if (stateLoop == 7) {
            int w = Dungeon.level.width();
            int[] tile = {w, w - 5, -w + 5, -w};
            for (int i = 0; i < 4; ++i) {
                Ballistica b = new Ballistica(towerPosLing, towerPosLing + tile[i], Ballistica.IGNORE_SOFT_SOLID);
                target.sprite.parent.add(new Beam.DeathRayS(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos),
                        DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                beamProc(b);
            }
            stateLoop++;
        } else if (stateLoop == 10) {
            stateLoop++;
            FloatingText.show(p.x, p.y, "※", 0x00ffff);
        } else if (stateLoop == 11) {
            int w = Dungeon.level.width();
            int[] tile = {w + 1, w - 1, -w + 1, -w - 1};
            for (int i = 0; i < 4; ++i) {
                Ballistica b = new Ballistica(towerPosLing, towerPosLing + tile[i], Ballistica.STOP_SOLID);
                target.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                beamProc(b);
            }
            if (stateLoop == 11) {
                int wx = Dungeon.level.width();
                int[] tilex = {w, -wx, 1, -1};
                for (int i = 0; i < 4; ++i) {
                    Ballistica b = new Ballistica(towerPosLing, towerPosLing + tilex[i], Ballistica.STOP_SOLID);
                    target.sprite.parent.add(new Beam.DeathRayS(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos),
                            DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                    beamProc(b);
                }
            }
            stateLoop = 0;
        } else if (stateLoop == 0 || stateLoop == 3|| stateLoop == 8|| stateLoop == 9) {
            stateLoop++;
        } else {
            stateLoop = 0;
        }
        // }

        spend(TICK);
        return true;
    }

    private static final String SHOCKER_POS = "shocker_pos";
    private static final String SHOCKING_ORDINALS = "shocking_ordinals";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SHOCKER_POS, towerPosLing);
        bundle.put(SHOCKING_ORDINALS, stateLoop);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        towerPosLing = bundle.getInt(SHOCKER_POS);
        stateLoop = bundle.getInt(SHOCKING_ORDINALS);
    }
}

