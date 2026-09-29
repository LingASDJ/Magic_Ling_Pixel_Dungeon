package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.MagicGirlDead;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.FireMagicDied;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.ColorTargetedCell;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;

public class BeamTowerAdbility extends Buff {

    public int towerPos;
    private int stateLoop = 1;

    //发射：在整条即将燃烧的路径上铺满火焰（形态固定：十字火/X字磷/米字霜，火焰只燃一回合）
    private void fireRange(int[] tiles, int projectileProps, int element){
        for (int i = 0; i < tiles.length; ++i) {
            Ballistica b = new Ballistica(towerPos, towerPos + tiles[i], projectileProps);
            for (int j : b.path) {
                if (j == towerPos) continue;
                TowerFireBlob blob = (TowerFireBlob) Blob.seed(j, 2, TowerFireBlob.class);
                blob.element = element;
                GameScene.add(blob);
            }
        }
        Sample.INSTANCE.play(Assets.Sounds.BURNING);
    }

    //天火技能是否正在蓄力（蓄力期间火墙不做8向齐射）
    private boolean skyFireCharging(){
        for (Mob m : level.mobs){
            if (m instanceof FireMagicDied && ((FireMagicDied) m).isSkyFireCharging()){
                return true;
            }
        }
        return false;
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

       //红莲真火蓄力期间，水晶塔暂停喷射火墙（玩家需专注摧毁浊焰核心保命）
       for (Mob m : level.mobs){
           if (m instanceof FireMagicDied && ((FireMagicDied) m).isRedLotusCharging()){
               spend(TICK);
               return true;
           }
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
            //预警①：十字光束 → 普通火焰
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w, -w, 1, -1}, Ballistica.STOP_SOLID, 0x5580FF);
        } else if (stateLoop == 2) {
            //发射①：仅发射刚预警的十字火墙（普通火）
            stateLoop++;
            int w = Dungeon.level.width();
            fireRange(new int[]{w, -w, 1, -1}, Ballistica.STOP_SOLID, 1);
        } else if (stateLoop == 3) {
            //预警②：X字光束 → 磷火
            stateLoop++;
            int w = Dungeon.level.width();
            warnRange(new int[]{w + 1, w - 1, -w + 1, -w - 1}, Ballistica.STOP_SOLID, 0xFF8055);
        } else if (stateLoop == 4) {
            //发射②：仅发射刚预警的X字火墙（磷火）
            stateLoop++;
            int w = Dungeon.level.width();
            fireRange(new int[]{w + 1, w - 1, -w + 1, -w - 1}, Ballistica.STOP_SOLID, 2);
        } else if (stateLoop == 5) {
            //预警③：米字光束（8向）→ 霜火；天火蓄力期间跳过，避免信息过载
            if (skyFireCharging()){
                stateLoop = 1;
            } else {
                stateLoop++;
                int w = Dungeon.level.width();
                warnRange(new int[]{w + 1, w - 1, -w + 1, -w - 1, w, -w, 1, -1}, Ballistica.STOP_SOLID, 0x00FFFF);
            }
        } else if (stateLoop == 6) {
            //发射③：仅发射刚预警的米字火墙（霜火）；天火蓄力期间同样跳过
            if (skyFireCharging()){
                stateLoop = 1;
            } else {
                stateLoop = 1;
                int w = Dungeon.level.width();
                fireRange(new int[]{w + 1, w - 1, -w + 1, -w - 1, w, -w, 1, -1}, Ballistica.STOP_SOLID, 0);
            }
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

    //===== 火墙火焰：每格只燃一回合，元素随机（霜焰/普通火/磷火）=====
    public static class TowerFireBlob extends Blob {

        public int element = 1;   //0=霜焰 1=普通火 2=磷火

        //按元素区分火焰颜色：霜火亮蓝、磷火亮绿（普通火用引擎自带橙红 FlameParticle）
        private static final Emitter.Factory FROST_FACTORY = ColoredFlameParticle.factory(0x4488FF);
        private static final Emitter.Factory HALO_FACTORY = ColoredFlameParticle.factory(0x33CC33);

        {
            actPriority = BUFF_PRIO - 1;
            alwaysVisible = true;
        }

        @Override
        public void use(BlobEmitter emitter) {
            super.use(emitter);
            if (element == 0){
                emitter.pour(FROST_FACTORY, 0.03f);   //霜火：亮蓝
            } else if (element == 1){
                emitter.pour(FlameParticle.FACTORY, 0.03f);   //普通火：橙红
            } else {
                emitter.pour(HALO_FACTORY, 0.03f);   //磷火：亮绿
            }
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
                            if(!(ch instanceof MagicGirlDead || ch instanceof FireMagicDied.ColdGuradB || ch instanceof FireMagicDied.ColdGuradC)){
                                if (element == 0){
                                    Buff.affect( ch, FrostBurning.class ).reignite( ch );
                                } else if (element == 1){
                                    Buff.affect( ch, Burning.class ).reignite( ch );
                                } else {
                                    Buff.affect( ch, HalomethaneBurning.class ).reignite( ch );
                                }
                            }
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
                        if (element == 0){
                            CellEmitter.get(cell).start(FROST_FACTORY, 0.03f, 10);
                        } else if (element == 1){
                            CellEmitter.get(cell).start(FlameParticle.FACTORY, 0.03f, 10);
                        } else {
                            CellEmitter.get(cell).start(HALO_FACTORY, 0.03f, 10);
                        }
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

        //通用彩色火焰粒子：按颜色区分霜火（亮蓝）/磷火（亮绿）
        public static class ColoredFlameParticle extends PixelParticle.Shrinking {

            public static Emitter.Factory factory(final int tint){
                return new Emitter.Factory() {
                    @Override
                    public void emit(Emitter emitter, int index, float x, float y) {
                        ColoredFlameParticle p = (ColoredFlameParticle)emitter.recycle(ColoredFlameParticle.class);
                        p.tint = tint;
                        p.color(tint);
                        p.reset(x, y);
                    }
                    @Override
                    public boolean lightMode() {
                        return true;
                    }
                };
            }

            private int tint = 0xEE7722;

            public ColoredFlameParticle() {
                super();
                color(tint);
                lifespan = 0.6f;
                acc.set(0, -80);
            }

            public void reset(float x, float y) {
                revive();
                this.x = x;
                this.y = y;
                left = lifespan;
                size = 4;
                speed.set(0);
            }

            @Override
            public void update() {
                super.update();
                float p = left / lifespan;
                am = p > 0.8f ? (1 - p) * 5 : 1;
            }
        }
    }
}
