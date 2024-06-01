package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class WorstStormCloud extends Blob{

    private int[] affectedCell= new int[]{};

    @Override
    public boolean act() {
        boolean result = super.act();
        if(volume<=0) {
            for(int i=0;i<affectedCell.length;i++){
                Level.set(affectedCell[i], Terrain.EMBERS);
                GameScene.updateMap(affectedCell[i]);
            }
            affectedCell= new int[]{};
        }
        return result;
    }

    @Override
    protected void evolve() {
        Fire fire = (Fire) Dungeon.level.blobs.get(Fire.class);
        int cell;

        for (int i = area.left-2; i <= area.right; i++) {
            for (int j = area.top - 2; j <= area.bottom; j++) {
                cell = i + j * Dungeon.level.width();
                if (cur[cell] > 0) {
                    Dungeon.level.setCellToWater(true, cell);
                    if (fire != null) {
                        fire.clear(cell);
                    }

                    if (!getIndex(cell)) {
                        int length = affectedCell.length;
                        affectedCell = Arrays.copyOf(affectedCell, length == 0 ? 1 : length + 1);
                        affectedCell[length] = cell;
                    }

                    off[cell] = cur[cell] - 1;
                    volume += off[cell];

                    //fiery enemies take damage as if they are in toxic gas
                    Char ch = Actor.findChar(cell);
                    if (ch != null
                            && !ch.isImmune(getClass())
                            && Char.hasProp(ch, Char.Property.FIERY)) {
                        ch.damage(1 + Dungeon.scalingDepth() / 5, this);
                    }
                } else {
                    off[cell] = 0;
                }
            }
        }
    }

    private boolean getIndex(int value){
        if(affectedCell.length==0)
            return false;

        for(int i=0;i<affectedCell.length;i++){
            if(affectedCell[i]==value)
                return true;
        }
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("affectedCell",affectedCell);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        affectedCell = bundle.getIntArray("affectedCell");
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.pour( Speck.factory( Speck.STORM ), 0.4f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }
}
