package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfMaster;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class BackgroundBeamCounter extends Buff {
    public float left = 9999999f;

    private int dx=-1;
    private int dy=-1;
    private int vx=0;
    private int vy=0;

    //negative x, y means never appear.
    public void setDensity(int x, int y, int vx, int vy){
        this.dx = x;
        this.dy = y;
        this.vx = vx;
        this.vy = vy;
    }

    protected boolean shouldExist(Ballistica beam){
        boolean shouldExist = false;
        for (int p : beam.path) {if(Dungeon.level.solid[p]) {shouldExist = true; break;}}
        return shouldExist;
    }

    protected void beamHit(Ballistica beam){

        for (int p : beam.path) {
            if (Dungeon.level.flamable[p]) {
                Dungeon.level.destroy( p );
                GameScene.updateMap( p );
            }
            Char ch = findChar(p);
            if (ch != null) {
                if (ch.alignment != Char.Alignment.ENEMY) {
                    Buff.affect(ch, Blindness.class, 5f);
                    Buff.detach(ch, MindVision.class);
                    ch.damage(Random.IntRange(10, 15), DwarfMaster.class);
                    if (ch == Dungeon.hero && !ch.isAlive()) {
                        Dungeon.fail(getClass());
                    }
                }
            }
        }
    }

    @Override
    public boolean act() {
        if(!target.isAlive()) detach();

        beamX();
        beamY();

        spend(TICK);
        left -= TICK;
        return true;
    }

    protected void beamX(){
        if(dx<=0) return;
        int w = Dungeon.level.width();
        int x = Dungeon.level.width() - 2 ;
        int y = Dungeon.level.height() - 2;

        for(int i=0; i<x; ++i) {
            if ((int) (left*vx) % dx == i % dx) {
                Ballistica beam = new Ballistica(i + 1 + w, i + 1 + (y - 1) * w, Ballistica.WONT_STOP);
                if(shouldExist(beam)) {
                    target.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(beam.sourcePos), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
                    beamHit(beam);
                }
            }
        }
    }

    protected void beamY(){
        if(dy<=0) return;
        int w = Dungeon.level.width();
        int x = Dungeon.level.width() - 2 ;
        int y = Dungeon.level.height() - 2;
        for(int i=0; i<y; ++i) {
            if ((int) (left * vy) % dy == i % dy) {
                Ballistica beam = new Ballistica(w * (i + 1) + 1, w * (i + 2) - 1, Ballistica.WONT_STOP);
                if (shouldExist(beam)) {
                    target.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(beam.sourcePos), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
                    beamHit(beam);
                }
            }
        }
    }

    private static final String LEFT = "efc_left";
    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );
        bundle.put("xHole", dx);
        bundle.put("yHole", dy);
        bundle.put("xSpeed", vx);
        bundle.put("ySpeed", vy);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        left = bundle.getFloat( LEFT );
        dx = bundle.getInt("xHole");
        dy = bundle.getInt("yHole");
        vx = bundle.getInt("xSpeed");
        vy = bundle.getInt("ySpeed");
    }

}

