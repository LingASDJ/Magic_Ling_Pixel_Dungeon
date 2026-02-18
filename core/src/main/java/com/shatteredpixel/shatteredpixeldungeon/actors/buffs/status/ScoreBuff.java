/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.branch;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame.MorphsMoveBoxEndPlot;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame.MorphsPacmanEndPlot;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScoreBar;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class ScoreBuff extends Buff {

    {
        type = buffType.NEUTRAL;
        announced = false;
    }

    public int score = 0;
    public int hscore = 0;

    public int SmallPoint = 300;
    public int BiggerPoint = 300;

    public boolean onlyChecker = false;
    public int PacManLevel = 0;

    public int GameRules;

    //MoveBox
    public int turns;
    public int maxTurns;

    private int moveMaxTurns(){
        MoveBoxHollowActorLevel level = (Dungeon.level instanceof MoveBoxHollowActorLevel) ? (MoveBoxHollowActorLevel) Dungeon.level : null;
        if(level != null){
            switch (level.rules){
                case 1:
                    maxTurns = 375;
                    break;
                case 2:
                    maxTurns = 400;
                    break;
                case 3:
                    maxTurns = 325;
                    break;
                case 4:
                    maxTurns = 985;
                    break;
                case 5:
                    maxTurns = 510;
                    break;
                case 6:
                    maxTurns = 550;
                    break;
            }
        }
        return maxTurns;
    }

    public void setGameRules(int rules){
        GameRules = rules;
    }

    private static final int HIGH_SCORE_THRESHOLD = 6000;
    private static final int MAX_LEVEL = 6;

    private int PacManScoreRules() {
        if (score <= 0) return 0;
        return Math.min(Statistics.getPacManScore / (HIGH_SCORE_THRESHOLD / MAX_LEVEL), MAX_LEVEL);
    }


    @Override
    public boolean act() {
        if (target.isAlive()) {
            ScoreBar.updateScoreFromBuff(this);
            ScoreBar.HighScoreRules();

            //推箱子
            MoveBoxHollowActorLevel level = (Dungeon.level instanceof MoveBoxHollowActorLevel) ? (MoveBoxHollowActorLevel) Dungeon.level : null;
            if(ScoreBar.Rules == 2 && level != null && !onlyChecker){
                turns++;
                maxTurns = moveMaxTurns();
                if(turns > maxTurns){
                    score = Math.max(0, score - 1);
                    if(turns-maxTurns % 50 == 0){
                        score = Math.max(0, score - 100);
                    }
                }
                if(score == 0 || level.SkipGame){
                    MorphsMoveBoxEndPlot plot = new MorphsMoveBoxEndPlot();
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndDialog(plot,false));
                        }
                    });
                    Buff.detach(hero, LostInventory.class);

                    Dungeon.level.drop(new MoveBoxHollowActorLevel.ThreeLet_go(),level.heroCellRules());
                    ScrollOfTeleportation.appear(hero, level.heroCellRules());

                    int count = 0;
                    for (Mob mob : Dungeon.level.mobs) {
                        if (mob instanceof MoveBoxHollowActorLevel.Box) {
                            if ((Dungeon.level.map[mob.pos] == Terrain.PEDESTAL)) {
                                count++;
                            }
                        }
                    }
                    Statistics.getMoveBoxScore = count * 10;
                    SPDSettings.MoveBoxScore(count * 10);

                    int levelc = Math.min(Statistics.getMoveBoxScore / (level.InitScore() / MAX_LEVEL), MAX_LEVEL);
                    if (levelc > 0) {
                        Statistics.miniGamesTotalLevel += levelc;
                    }
                }
            }

            //吃豆人
            if(SmallPoint == 0 && BiggerPoint == 0 && !onlyChecker && Dungeon.depth == 31 && branch == 1){
                MorphsPacmanEndPlot plot = new MorphsPacmanEndPlot();
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndDialog(plot,false));
                    }
                });
               SPDSettings.PacManScore(score);
               Statistics.getPacManScore = score;

               //S评估
               if(score >= 4500 && branch == 1 && Dungeon.depth == 31){
                   Badges.MINIGAME_MASTER_ONE();
               }

                Statistics.miniGamesTotalLevel += PacManScoreRules();

               Buff.detach(hero, LostInventory.class);
            }

            spend(1f);

        } else {
            detach();
        }

        return true;
    }

    public void addScore(int amount) {
        if (amount > 0) {
            score += amount;
            ScoreBar.updateScoreFromBuff(this);
        }
    }

    public void downScore(int amount) {
        if (amount > 0) {
            score -= Math.min(amount,score);
            ScoreBar.updateScoreFromBuff(this);
        }
    }

    public void setScore(int newScore) {
        if (newScore >= 0) {
            score = newScore;
            ScoreBar.updateScoreFromBuff(this);
        }
    }

    public int getScore() {
        return score;
    }

        @Override
    public int icon() {
        switch (ScoreBar.Rules){
            default:
                return BuffIndicator.PACMAN_GAME;
            case 2:
                return BuffIndicator.BOX_GAME;
            case 3:
                return BuffIndicator.ALL_SEARCH;
        }
    }

    @Override
    public String iconTextDisplay() {
        return String.valueOf(score);
    }

    @Override
    public String desc() {

        String string;

        switch (ScoreBar.Rules){
            default:
                string = Messages.get(this, "desc", score, SmallPoint, BiggerPoint);
                break;
            case 2:
                string = Messages.get(this, "desc2", score, turns, maxTurns, Math.max(0, maxTurns - turns));
                break;
            case 3:
                string = Messages.get(this, "desc3", score, PacManLevel);
                break;
        }

        return string;
    }

    public String name() {
       String string;
       switch (ScoreBar.Rules){
           default:
               string = Messages.get(this, "name");
               break;
           case 2:
               string = Messages.get(this, "name2");
               break;
           case 3:
               string = Messages.get(this, "name3");
               break;
       }
       return string;
    }

    private static final String SCORE    = "score";
    private static final String HSOCRE    = "hsocre";
    private static final String SMALL =  "small";
    private static final String BIGGER =  "bigger";
    private static final String PACMAN = "pacman";
    private static final String PACMANLEVEL = "pacmanlevel";

    private static final String GAMERULES = "gamerules";

    private static final String GAMETURNS = "gameturns";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( SCORE, score );
        bundle.put( HSOCRE, hscore);
        bundle.put(SMALL, SmallPoint);
        bundle.put(BIGGER, BiggerPoint);
        bundle.put(PACMAN, onlyChecker);
        bundle.put(PACMANLEVEL, PacManLevel);
        bundle.put(GAMERULES, GameRules);
        bundle.put(GAMETURNS, turns);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        score = bundle.getInt( SCORE );
        hscore = bundle.getInt( HSOCRE );
        SmallPoint = bundle.getInt(SMALL);
        BiggerPoint = bundle.getInt(BIGGER);
        onlyChecker = bundle.getBoolean(PACMAN);
        PacManLevel = bundle.getInt(PACMANLEVEL);
        GameRules = bundle.getInt(GAMERULES);
        turns = bundle.getInt(GAMETURNS);
    }

}
