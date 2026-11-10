package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.SliceDogPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.SlicePlot;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;

public class SliceGirl extends NTNPC {

    {
        spriteClass = SliceGirlSprite.class;
        flying = true;
    }

    public boolean first=true;
    public boolean secnod=true;
    public boolean rd = true;
    /** 29层告别对话已开始，精灵正在淡出；阻止重复对话 */
    public boolean leaving = false;

    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";
    private static final String RD = "rd";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
        bundle.put(RD, rd);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
        rd = bundle.getBoolean(RD);
    }

    @Override
    protected boolean act() {

        // rd=false 表示29层的告别剧情已经结束；旧存档可能只剩一个
        // 没有精灵的残留 NPC，这里顺手清理掉，避免继续对话崩溃。
        if (!rd || sprite == null || sprite.parent == null) {
            destroy();
            return true;
        }

        throwItem();

        sprite.turnTo( pos, Dungeon.hero.pos );
        spend( TICK );
        return true;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public boolean interact(Char c) {

        // 兼容旧存档：29层告别后精灵已被移除，但 NPC 还留在 mobs 列表里。
        // 此时再对话会在 SliceDogPlot 中访问已经脱离场景的 sprite.parent。
        if (sprite == null || sprite.parent == null || !isAlive()) {
            destroy();
            return false;
        }

        if (!rd) {
            destroy();
            return false;
        }

        if (leaving) {
            return false;
        }

        sprite.turnTo(pos, Dungeon.hero.pos);
        SlicePlot plot = new SlicePlot();

        SliceDogPlot plot2 = new SliceDogPlot();

        if(first && Dungeon.depth == 27 && Statistics.Hollow_Holiday){
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot,false)));
        } else if(Dungeon.depth == 29) {
            Game.runOnRenderThread(() -> GameScene.show(new WndDialog(plot2,false)));
        }
        Bestiary.setSeen(getClass());

        return true;
    }

    public static void tell(String text) {
        Game.runOnRenderThread(() -> GameScene.show(new WndQuest(new DeathRong(), text)));
    }

}

