package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.DwarfGrenPlot;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DwarfGeneralSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class DwarfGeneralNPC extends NTNPC {

    {
        spriteClass = DwarfGeneralSprite.class;
    }

    private boolean first=true;
    private static final String FIRST = "first";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
    }

    @Override
    public boolean interact(Char c) {

        sprite.turnTo(pos, hero.pos);
        if(first){
            DwarfGrenPlot plot = new DwarfGrenPlot();
            Game.runOnRenderThread(new Callback() {
                private int retries = 0;
                @Override
                public void call() {
                    // 切场景瞬间 GameScene.scene 可能为空，GameScene.show 会静默丢弃弹窗，这里下帧重试
                    if (GameScene.scene == null) {
                        if (retries++ < 10) {
                            Game.runOnRenderThread(this);
                        }
                        return;
                    }
                    try {
                        GameScene.show(new WndDialog(plot,false));
                    } catch (Exception e) {
                        // 弹窗构造失败时兜底：直接开启boss战，避免卡死
                        GLog.n("剧情弹窗加载失败，直接开始战斗！");
                        if (!Dungeon.level.locked) {
                            Dungeon.level.seal();
                        }
                    }
                }
            });
           first = false;
        }
        return true;
    }


}
