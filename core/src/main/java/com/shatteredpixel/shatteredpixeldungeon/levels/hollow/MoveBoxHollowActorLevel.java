package com.shatteredpixel.shatteredpixeldungeon.levels.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSight;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.PacManQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BoxSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MoveBoxHollowActorLevel extends Level {

    {
        onlyBoxMove = true;
        viewDistance = 10000;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.MOVEBOX,true);
    }

    public int rules = Random.Int(1,7);
    public int readscore;

    private int codeToTerrain(int code){
        switch (code){
            case 25:
                return Terrain.CHASM;
            case 49:
                return Terrain.WALL;
            case 21:
                return Terrain.PEDESTAL;
            case 51:
                return Terrain.BOOKSHELF;
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

    private static final int[] boxMap_hard_one = {
            25, 25, 25, 25, 25, 25, 25, 49, 51, 51, 51, 51, 51, 49, 25, 25, 25, 25,
            25, 49, 49, 49, 49, 49, 49, 49, 5, 5, 5, 5, 5, 49, 25, 25, 25, 25,
            25, 49, 1, 1, 5, 5, 5, 49, 5, 5, 5, 5, 5, 49, 25, 25, 25, 25,
            25, 49, 1, 1, 5, 49, 5, 5, 5, 49, 49, 49, 49, 49, 49, 49, 49, 49,
            25, 49, 1, 49, 49, 49, 21, 21, 21, 21, 21, 21, 49, 49, 5, 5, 1, 49,
            25, 49, 1, 5, 5, 5, 21, 21, 21, 21, 21, 21, 49, 49, 5, 49, 1, 49,
            25, 49, 1, 51, 51, 49, 21, 21, 21, 21, 21, 21, 5, 5, 5, 1, 1, 49,
            49, 49, 1, 1, 1, 49, 49, 49, 49, 5, 49, 49, 49, 5, 49, 1, 49, 49,
            49, 1, 1, 49, 1, 1, 1, 5, 49, 5, 5, 5, 5, 5, 49, 1, 49, 25,
            49, 1, 1, 1, 1, 1, 1, 5, 5, 5, 49, 5, 5, 49, 49, 1, 49, 25,
            49, 1, 1, 1, 1, 1, 1, 1, 51, 51, 51, 1, 1, 1, 49, 1, 49, 25,
            49, 49, 49, 49, 49, 1, 1, 1, 1, 1, 1, 1, 1, 1, 49, 1, 49, 25,
            25, 25, 25, 25, 49, 49, 49, 1, 49, 49, 49, 1, 1, 1, 49, 5, 49, 25,
            25, 25, 25, 25, 25, 25, 49, 1, 1, 1, 1, 1, 49, 5, 5, 5, 49, 25,
            25, 25, 25, 25, 25, 25, 49, 49, 49, 49, 49, 49, 49, 49, 5, 5, 49, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 49, 49, 49, 49, 25
    };

    private static final int[] boxMap_hard_two = {
            25, 25, 25, 49, 49, 51, 51, 49, 49, 49, 49, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 49, 1, 1, 1, 49, 1, 1, 49, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 49, 25, 25, 25, 25, 25, 25, 25,
            25, 49, 49, 49, 1, 49, 1, 1, 1, 1, 51, 51, 49, 49, 25, 25, 25, 25,
            25, 51, 1, 1, 1, 1, 1, 49, 49, 1, 1, 1, 1, 49, 25, 25, 25, 25,
            25, 51, 1, 5, 49, 5, 5, 5, 5, 5, 49, 1, 1, 49, 25, 25, 25, 25,
            25, 51, 5, 5, 49, 5, 5, 5, 5, 5, 5, 5, 1, 51, 51, 51, 49, 49,
            25, 51, 51, 5, 49, 49, 49, 49, 5, 49, 49, 5, 1, 1, 1, 1, 1, 49,
            25, 49, 5, 5, 49, 21, 21, 21, 21, 21, 49, 5, 49, 1, 1, 1, 1, 49,
            25, 49, 5, 5, 5, 21, 21, 21, 21, 21, 5, 5, 49, 5, 49, 49, 49, 49,
            49, 49, 5, 5, 49, 21, 21, 21, 21, 21, 49, 5, 5, 5, 49, 25, 25, 25,
            49, 1, 1, 1, 49, 49, 49, 5, 49, 49, 49, 49, 49, 49, 49, 25, 25, 25,
            49, 1, 1, 1, 1, 1, 49, 5, 5, 49, 25, 25, 25, 25, 25, 25, 25, 25,
            49, 1, 1, 49, 1, 1, 5, 5, 5, 49, 25, 25, 25, 25, 25, 25, 25, 25,
            49, 49, 49, 49, 49, 49, 5, 5, 5, 49, 25, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 25, 25, 49, 49, 49, 49, 49, 25, 25, 25, 25, 25, 25, 25, 25
    };

    private static final int[] boxMap_hard_three = {
            25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 25, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 25, 25, 25, 25,
            25, 25, 25, 25, 49, 21, 21, 5, 5, 49, 5, 5, 5, 49, 25, 25, 25, 25,
            25, 25, 25, 25, 49, 21, 21, 5, 5, 5, 5, 5, 5, 49, 25, 25, 25, 25,
            25, 25, 25, 25, 49, 21, 21, 5, 5, 49, 5, 5, 49, 49, 49, 49, 25, 25,
            25, 25, 25, 49, 49, 49, 51, 51, 51, 51, 1, 1, 49, 1, 1, 49, 49, 25,
            25, 25, 25, 49, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 49, 25,
            25, 25, 25, 49, 1, 1, 51, 1, 1, 49, 49, 1, 1, 49, 1, 1, 49, 25,
            25, 51, 51, 51, 51, 1, 51, 51, 1, 1, 49, 49, 49, 49, 5, 49, 49, 25,
            25, 51, 5, 5, 5, 1, 1, 51, 51, 51, 49, 49, 5, 49, 5, 5, 49, 25,
            25, 51, 5, 49, 5, 1, 1, 1, 1, 1, 1, 49, 5, 5, 5, 5, 49, 25,
            25, 49, 5, 5, 5, 1, 1, 1, 1, 1, 1, 49, 5, 5, 5, 49, 49, 25,
            25, 49, 49, 49, 49, 1, 49, 49, 1, 49, 49, 49, 49, 49, 49, 49, 25, 25,
            25, 25, 25, 25, 49, 1, 1, 1, 1, 49, 25, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 25, 49, 49, 49, 49, 49, 49, 25, 25, 25, 25, 25, 25, 25, 25,
            25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25
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
            case 4: case 5: case 6:
                width = 18;
                height = 16;
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

    public boolean allBoxesOnTarget() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Box) {
                if (!(Dungeon.level.map[mob.pos] == Terrain.PEDESTAL)) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void occupyCell(Char ch) {
        super.occupyCell(ch);
        if(allBoxesOnTarget()){
            GLog.p("Boxes are on target!");
        }
    }


    private int heroCellRules() {
        switch (rules) {
            case 2:
                return 133;
            case 3:
                return 21;
            case 4:
                return 46;
            case 5:
                return 96;
            case 6:
                return 201;
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
            case 4:
                return boxMap_hard_one;
            case 5:
                return boxMap_hard_two;
            case 6:
                return boxMap_hard_three;
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

    private static final int[] Box_Map_Four = {
            148,165,167,184,168,186,208,191,192,174,155,141,45,47,56,57,95,169
    };

    private static final int[] Box_Map_Five = {
            169,170,166,147,218,219,76,60,42,134,98,81,119,102,173
    };

    private static final int[] Box_Map_Six = {
            202,185,166,205,188,193
    };

    private int InitScore(){
        int highScoreThreshold;
        switch (rules){
            case 1:
                highScoreThreshold = 1110;
                break;
            case 2:
                highScoreThreshold = 1200;
                break;
            case 3:
                highScoreThreshold = 1000;
                break;
            case 4:
                highScoreThreshold = 1800;
                break;
            case 5:
                highScoreThreshold = 1500;
                break;
            case 6:
                highScoreThreshold = 1650;
                break;
            default:
                highScoreThreshold = 100;
                break;
        }
        return highScoreThreshold;
    }

    @Override
    protected void createMobs() {

        for (int i : BoxRules()) {
            Box box = new Box();
            box.pos = i;
            mobs.add(box);
        }
        Buff.detach(hero, ScoreBuff.class);
        Buff.affect(hero, ScoreBuff.class);
        ScoreBar.updateScoreFromBuff(hero.buff(ScoreBuff.class));
        ScoreBar.setRules(2);
        Buff.affect(hero, PacManQuest.RandomItemPlus.class);
        Buff.affect(hero, MagicalSight.class, MagicalSight.DURATION*200);
        ScoreBar.assignScore(InitScore(),InitScore());
    }

    public int[] BoxRules() {
        switch (rules){
            case 2:
                return Box_Map_Two;
            case 3:
                return Box_Map_Three;
            case 1:
                return Box_Map_One;
            case 4:
                return Box_Map_Four;
            case 5:
                return Box_Map_Five;
            case 6:
                return Box_Map_Six;
            default:
                return new int[0];
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
                Dungeon.hero.spendAndNext(1f);
            }

            return true;
        }

    }

    public final String RULES = "map_rules";
    public final String READSCORE = "readscore";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(RULES, rules);
        bundle.put(READSCORE, readscore);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        rules = bundle.getInt(RULES);
        readscore = bundle.getInt(READSCORE);
        ScoreBar.setRules(2);
        if(readscore != 0){
            ScoreBar.assignScore(readscore,InitScore());
        }
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return false;
    }

}
