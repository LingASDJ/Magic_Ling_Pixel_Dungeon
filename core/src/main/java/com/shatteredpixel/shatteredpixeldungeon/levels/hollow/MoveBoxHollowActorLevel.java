package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BoxSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MoveBoxHollowActorLevel extends Level {

    {
        onlyBoxMove = true;
    }

    public int rules = Random.Int(1,4);

    private int codeToTerrain(int code){
        switch (code){
            case 25:
                return Terrain.CHASM;
            case 49:
                return Terrain.WALL;
            case 21:
                return Terrain.PEDESTAL;
            case 5:
                return Terrain.EMPTY_SP;
            default:
                return Terrain.EMPTY;
        }
    }

    private static final int[] boxMap_one = {
            25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 49, 49, 49, 49, 49, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 49, 1, 1, 49, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 1, 49, 25, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 1, 1, 1, 1, 49, 25, 25,
            49, 49, 49, 49, 49, 49, 49, 49, 49, 1, 1, 1, 49, 1, 49, 49, 49,
            49, 21, 21, 21, 21, 5, 5, 49, 49, 1, 1, 1, 1, 1, 1, 1, 49,
            49, 49, 21, 21, 21, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 49,
            49, 21, 21, 21, 21, 5, 5, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49,
            49, 49, 49, 49, 49, 49, 49, 49, 25, 25, 25, 25, 25, 25, 25, 25, 25
    };

    private static final int[] boxMap_two = {
            25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 49, 49, 49, 25, 25, 25, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 49, 49, 49, 49, 49,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 49, 1, 49, 49, 1, 1, 49,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 1, 49,
            49, 49, 49, 49, 49, 49, 49, 49, 49, 1, 49, 49, 49, 1, 1, 1, 49,
            49, 21, 21, 21, 21, 5, 5, 49, 49, 1, 1, 1, 1, 1, 49, 49, 49,
            49, 21, 21, 21, 21, 5, 5, 5, 5, 1, 1, 1, 1, 1, 49, 49, 25,
            49, 21, 21, 21, 21, 5, 5, 49, 49, 1, 1, 1, 1, 1, 1, 49, 25,
            49, 49, 49, 49, 49, 49, 49, 49, 49, 1, 1, 1, 1, 1, 49, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 49, 1, 49, 49, 1, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 49, 49, 49, 49, 25
    };

    private static final int[] boxMap_three = {
            49, 49, 49, 49, 49, 49, 25, 25, 49, 49, 49, 25,
            49, 21, 21, 5, 5, 49, 25, 49, 49, 1, 49, 49,
            49, 21, 21, 5, 5, 49, 49, 49, 1, 1, 1, 49,
            49, 21, 21, 5, 5, 5, 1, 1, 1, 1, 1, 49,
            49, 21, 21, 5, 5, 49, 1, 49, 1, 1, 1, 49,
            49, 21, 21, 49, 49, 49, 1, 49, 1, 1, 1, 49,
            49, 49, 49, 49, 1, 1, 1, 49, 1, 1, 1, 49,
            25, 25, 25, 49, 1, 1, 1, 49, 1, 1, 1, 49,
            25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 1, 49,
            25, 25, 25, 49, 1, 1, 49, 49, 1, 1, 1, 49,
            25, 25, 25, 49, 49, 49, 49, 49, 49, 49, 49, 49
    };


    @Override
    protected boolean build() {
        switch (rules){
            case 2:
                width = 17;
                height = 13;
                break;
            case 3:
                width = 12;
                height = 11;
                break;
            default:
                width = 17;
                height = 10;
                break;
        }

        setSize(width, height);

        for(int i= 0; i< width * height; ++i){
            map[i] = codeToTerrain(mapRules()[i]);
        }

        LevelTransition entrance = new LevelTransition(this, heroCellRules(), LevelTransition.Type.REGULAR_ENTRANCE);
        transitions.add(entrance);

        LevelTransition exit = new LevelTransition(this, 0, LevelTransition.Type.REGULAR_EXIT);
        transitions.add(exit);
        return true;
    }

    private int heroCellRules() {
        switch (rules) {
            case 2:
                return 133;
            case 3:
                return 21;
            default:
                return 31;
        }
    }

    private int[] mapRules() {
        switch (rules){
            case 2:
                return boxMap_two;
            case 3:
               return boxMap_three;
            default:
                return boxMap_one;
        }
    }

    private static final int[] Box_Map_One = {
            128,112,95,78,61,44,46,64,80,115,131
    };

    private static final int[] Box_Map_Two = {
            111,128,95,113,114,98,131,147,163,165,65,45
    };

    private static final int[] Box_Map_Three = {
            44,45,57,69,80,93,104,90,77,101
    };

    @Override
    protected void createMobs() {

        for (int i : BoxRules()) {
            Box box = new Box();
            box.pos = i;
            mobs.add(box);
        }

        ScoreBar.assignScore(0,1000);
        Buff.detach(hero, ScoreBuff.class);
        Buff.affect(hero, ScoreBuff.class);
        ScoreBar.updateScoreFromBuff(hero.buff(ScoreBuff.class));
        ScoreBar.setRules(2);
        Buff.affect(hero, PacManQuest.RandomItemPlus.class);
    }

    private int[] BoxRules() {
        switch (rules){
            case 2:
                return Box_Map_Two;
            case 3:
                return Box_Map_Three;
            default:
                return Box_Map_One;
        }
    }

    @Override
    protected void createItems() {

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GHOST;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_GHOST;
    }

    public static class Box extends NTNPC {

        private static final String[] QUOTES = {Messages.get(Box.class, "one"), Messages.get(Box.class, "two"), Messages.get(Box.class, "three"), Messages.get(Box.class, "four")};

        {
            spriteClass = BoxSprite.class;
            properties.add(Property.IMMOVABLE);
        }

        @Override
        public boolean interact(Char c) {
            int curPos = pos;
            int movPos = pos;
            int width = Dungeon.level.width();
            boolean moved = false;
            int posDif = Dungeon.hero.pos - curPos;

            if (posDif == 1) {
                movPos = curPos - 1;
            } else if (posDif == -1) {
                movPos = curPos + 1;
            } else if (posDif == width) {
                movPos = curPos - width;
            } else if (posDif == -width) {
                movPos = curPos + width;
            }

            if (movPos != pos && (Dungeon.level.passable[movPos] || Dungeon.level.avoid[movPos]) && Actor.findChar(movPos) == null) {

                moveSprite(curPos, movPos);
                move(movPos);
                moved = true;

            }

            if (moved) {
                Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
                Dungeon.hero.move(curPos);
            }

            return true;
        }

    }

    public final String RULES = "map_rules";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(RULES, rules);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        rules = bundle.getInt(RULES);
        ScoreBar.setRules(2);
    }

}
