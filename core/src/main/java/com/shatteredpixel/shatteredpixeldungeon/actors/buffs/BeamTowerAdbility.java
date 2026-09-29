package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class BeamTowerAdbility extends Buff {

    public int towerPos;
    private int stateLoop = 1;

    //发射：在整条即将燃烧的路径上铺满火焰（Tengu 式 FireAbility，火焰一回合后消失）
    private void fireRange(int[] tiles, int projectileProps){
        for (int i = 0; i < tiles.length; ++i) {
            Ballistica b = new Ballistica(towerPos, towerPos + tiles[i], projectileProps);
            for (int j : b.path) {
                if (j == towerPos) continue;
                GameScene.add(Blob.seed(j, 2, TowerFireBlob.class));
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }

    //预警：在整条即将发射的光束路径上铺满彩色标记（覆盖全部预警范围）
    private void warnRange(int[] tiles, int projectileProps, int color){
        for (int i = 0; i < tiles.length; ++i) {
            Ballistica b = new Ballistica(towerPos, towerPos + tiles[i], projectileProps);
            for (int j : b.path) {
                if (j == towerPos) continue;
                target.sprite.parent.add(new ColorTargetedCell(j, color));
            }
        }
    }

    @Override
    public boolean act() {

       if(!level.locked){
           detach();
       }

/*
            if(target instanceof Boss && target.HP <= target.HT * 2 /10) {
                if(stateLoop%3==0){
                    stateLoop++;
                }else if(stateLoop%3 == 1){
                    FloatingText.show(p.x, p.y, "*", 0xFF77FF);
                    stateLoop++;
                }else{
                    int[] tile = PathFinder.NEIGHBOURS8;
                    for(int i=0;i<8;++i){
                        Ballistica b = new Ballistica(towerPos, towerPos + tile[i], Ballistica.STOP_SOLID);
                        target.sprite.parent.add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(b.sourcePos), DungeonTilemap.raisedTileCenterToWorld(b.collisionPos)));
                        beamProc(b);
                    }
                }
            }
            else{
 */     PointF p = DungeonTilemap.raisedTileCenterToWorld(towerPos);
        if (stateLoop == 1) {
            //预警①：十字光束
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w, -w, 1, -1}, Ballistica.STOP_SOLID, 0x5580FF);
        } else if (stateLoop == 2) {
            //发射①：仅发射刚预警的十字火墙
            stateLoop++;
            int w = Dungeon.level.width();
            fireRange(new int[]{w, -w, 1, -1}, Ballistica.STOP_SOLID);
        } else if (stateLoop == 3) {
            //预警②：斜向光束
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w + 1, w - 1, -w + 1, -w - 1}, Ballistica.STOP_SOLID, 0xFF8055);
        } else if (stateLoop == 4) {
            //发射②：仅发射刚预警的斜向火墙
            stateLoop++;
            int w = Dungeon.level.width();
            fireRange(new int[]{w + 1, w - 1, -w + 1, -w - 1}, Ballistica.STOP_SOLID);
        } else if (stateLoop == 5) {
            //预警③：斜扫光束
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w, w - 5, -w + 5, -w}, Ballistica.IGNORE_SOFT_SOLID, 0x00FFFF);
        } else if (stateLoop == 6) {
            //发射③：仅发射刚预警的斜扫火墙
            stateLoop++;
            int w = Dungeon.level.width();
            fireRange(new int[]{w, w - 5, -w + 5, -w}, Ballistica.IGNORE_SOFT_SOLID);
        } else if (stateLoop == 7) {
            //预警④：最终 8 向齐射（一次性整体预警）
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w + 1, w - 1, -w + 1, -w - 1, w, -w, 1, -1}, Ballistica.STOP_SOLID, 0x00FFFF);
        } else if (stateLoop == 8) {
            //发射④：仅发射刚预警的最终 8 向火墙
            stateLoop = 1;
            int w = Dungeon.level.width();
            fireRange(new int[]{w + 1, w - 1, -w + 1, -w - 1, w, -w, 1, -1}, Ballistica.STOP_SOLID);
        } else {
            stateLoop = 1;
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
        bundle.put(SHOCKER_POS, towerPos);
        bundle.put(SHOCKING_ORDINALS, stateLoop);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        towerPos = bundle.getInt(SHOCKER_POS);
        stateLoop = bundle.getInt(SHOCKING_ORDINALS);
    }

    //===== Tengu 式火焰：每格的火只持续一回合，随后燃烧角色、点燃地面并消失 ===== 
    public static class TowerFireBlob extends Blob {

        {
            actPriority = BUFF_PRIO - 1;
            alwaysVisible = true;
        }

        @Override
        public void use(BlobEmitter emitter) {
            super.use(emitter);
            emitter.pour(FlameParticle.FACTORY, 0.03f);
        }

        @Override
        protected void evolve() {

            boolean observe = false;
            boolean burned = false;

            int cell;
            for (int i = area.left; i < area.right; i++){
                for (int j = area.top; j < area.bottom; j++){
                    cell = i + j* Dungeon.level.width();
                    off[cell] = (int)GameMath.gate(0, cur[cell] - 1, 1);

                    if (off[cell] > 0) {
                        volume += off[cell];
                    }

                    if (cur[cell] > 0 && off[cell] == 0){

                        Char ch = Actor.findChar( cell );
                        if (ch != null && !ch.isImmune(Fire.class)) {
                            Buff.affect( ch, Burning.class ).reignite( ch );
                        }
                        if (ch == Dungeon.hero){
                            Statistics.bossScores[3] -= 100;
                        }

                        if (Dungeon.level.flamable[cell]){
                            Dungeon.level.destroy( cell );

                            observe = true;
                            GameScene.updateMap( cell );
                        }

                        burned = true;
                        CellEmitter.get(cell).start(FlameParticle.FACTORY, 0.03f, 10);
                    }
                }
            }

            if (observe) {
                Dungeon.observe();
            }
            if (burned){
                Sample.INSTANCE.play(Assets.Sounds.BURNING);
            }
        }
    }
}
