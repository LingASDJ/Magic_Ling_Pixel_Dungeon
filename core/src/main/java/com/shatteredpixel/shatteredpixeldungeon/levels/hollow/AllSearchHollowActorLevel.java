package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.RiverPainter;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.watabou.utils.Random;

public class AllSearchHollowActorLevel extends RegularLevel {

    {
        extraGlass = false;
    }

    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 1;
        return 5 + Dungeon.depth/5+ Random.chances(new float[]{1,1,1});
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GHOST;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_GHOST;
    }

    @Override
    protected void createItems() {
        super.createItems();
    }

    @Override
    protected void createMobs() {
        Buff.detach(hero, ScoreBuff.class);
        Buff.affect(hero, ScoreBuff.class);
        ScoreBar.updateScoreFromBuff(hero.buff(ScoreBuff.class));
        ScoreBar.setRules(3);
        Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
        ScoreBar.assignScore(0,16000);
    }

    @Override
    protected Painter painter() {
        RiverPainter painter = new RiverPainter();
        painter.setWater(0.15f, 4);  // 设置河流参数
        painter.setGrass(0.25f, 3);  // 设置植被参数
        painter.paint(this, rooms);
        return painter;
    }

}
