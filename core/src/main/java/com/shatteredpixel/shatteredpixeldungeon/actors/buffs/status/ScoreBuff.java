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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame.MorphsPacManPlot;
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

    public boolean PacMan = false;
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
                    maxTurns = 510;
                break;
                case 5:
                    maxTurns = 985;
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

    @Override
    public boolean act() {
        if (target.isAlive()) {
            ScoreBar.updateScoreFromBuff(this);
            ScoreBar.HighScoreRules();

            if(ScoreBar.Rules == 2){
                turns++;
                maxTurns = moveMaxTurns();
                if(turns > maxTurns){
                    score = Math.max(0, score - 1);
                    if(turns % 50 == 0){
                        score = Math.max(0, score - 100);
                    }
                }
            }

            spend(1f);

            if(SmallPoint == 0 && BiggerPoint == 0 && !PacMan){
               if(score>6000){
                   MorphsPacManPlot.MorphsPacManPeactPlot plot = new MorphsPacManPlot.MorphsPacManPeactPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 6;
               } else if(score>=4500) {
                   MorphsPacManPlot.MorphsPacManVeryGoodlPlot plot = new MorphsPacManPlot.MorphsPacManVeryGoodlPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 5;
               } else if(score>=3001){
                   MorphsPacManPlot.MorphsPacManGoodPlot plot = new MorphsPacManPlot.MorphsPacManGoodPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 4;
               } else if(score>=2001){
                   MorphsPacManPlot.MorphsPacManEndPlot plot = new MorphsPacManPlot.MorphsPacManEndPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 3;
               } else if(score>=1000){
                   MorphsPacManPlot.MorphsPacManNormalPlot plot = new MorphsPacManPlot.MorphsPacManNormalPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 2;
               } else {
                   MorphsPacManPlot plot = new MorphsPacManPlot();
                   Game.runOnRenderThread(new Callback() {
                       @Override
                       public void call() {
                           GameScene.show(new WndDialog(plot,false));
                       }
                   });
                   PacManLevel = 1;
               }
               SPDSettings.PacManScore(score);
               PacMan = true;
            }

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
                string = Messages.get(this, "desc2", score, turns, maxTurns, maxTurns-turns);
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
        bundle.put(PACMAN, PacMan);
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
        PacMan = bundle.getBoolean(PACMAN);
        PacManLevel = bundle.getInt(PACMANLEVEL);
        GameRules = bundle.getInt(GAMERULES);
        turns = bundle.getInt(GAMETURNS);
    }

}
