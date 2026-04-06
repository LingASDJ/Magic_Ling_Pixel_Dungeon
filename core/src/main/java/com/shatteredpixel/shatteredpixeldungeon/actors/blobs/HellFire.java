package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HellBurning;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.HellFlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public class HellFire extends Blob {
    private boolean isValidCell(int cell) {
        return cell >= 0 && cell < Dungeon.level.length();
    }
    @Override
    protected void evolve() {
        boolean[] flamable = Dungeon.level.flamable;
        int cell;
        int fire;

        Freezing freeze = (Freezing)Dungeon.level.blobs.get(Freezing.class);
        boolean observe = false;
        int width = Dungeon.level.width();
        int length = Dungeon.level.length();

        for (int i = area.left-1; i <= area.right; i++) {
            for (int j = area.top-1; j <= area.bottom; j++) {
                cell = i + j*width;

                // 验证当前cell是否有效
                if (!isValidCell(cell)) {
                    continue;
                }

                if (cur[cell] > 0) {
                    if (freeze != null && freeze.volume > 0 && freeze.cur[cell] > 0){
                        freeze.clear(cell);
                        off[cell] = cur[cell] = 0;
                        continue;
                    }

                    burn(cell);
                    fire = cur[cell] - 1;

                    if (fire <= 0 && flamable[cell]) {
                        Dungeon.level.destroy(cell);
                        observe = true;
                        GameScene.updateMap(cell);
                    }

                } else if (freeze == null || freeze.volume <= 0 || freeze.cur[cell] <= 0) {
                    if (flamable[cell]) {
                        boolean hasAdjacentFire = false;

                        // 检查左边界
                        if (cell % width != 0 && isValidCell(cell-1) && cur[cell-1] > 0) {
                            hasAdjacentFire = true;
                        }
                        // 检查右边界
                        else if (cell % width != width-1 && isValidCell(cell+1) && cur[cell+1] > 0) {
                            hasAdjacentFire = true;
                        }
                        // 检查上边界
                        else if (cell >= width && isValidCell(cell-width) && cur[cell-width] > 0) {
                            hasAdjacentFire = true;
                        }
                        // 检查下边界
                        else if (cell < length - width && isValidCell(cell+width) && cur[cell+width] > 0) {
                            hasAdjacentFire = true;
                        }

                        fire = hasAdjacentFire ? 4 : 0;
                        if (hasAdjacentFire) {
                            burn(cell);
                            area.union(i, j);
                        }
                    } else {
                        fire = 0;
                    }
                } else {
                    fire = 0;
                }

                volume += (off[cell] = fire);
            }
        }

        if (observe) {
            Dungeon.observe();
        }
    }

    //定义燃烧效果和渲染燃烧行动
    public static void burn( int pos ) {
        Char ch = Actor.findChar( pos );
        if (ch != null && !ch.isImmune(HellFire.class)) {
            Buff.affect( ch, HellBurning.class ).reignite( ch );
        }

        Heap heap = Dungeon.level.heaps.get( pos );
        if (heap != null) {
            heap.burn();
        }

        Plant plant = Dungeon.level.plants.get( pos );
        if (plant != null){
            plant.wither();
        }
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );
        emitter.pour( HellFlameParticle.FACTORY, 0.03f );
    }

    @Override
    public String tileDesc() {
        return Messages.get(this, "desc");
    }

}
