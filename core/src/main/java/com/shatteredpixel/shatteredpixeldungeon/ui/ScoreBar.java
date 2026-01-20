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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.CYELLOW;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.GREEN_COLOR;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.ORAGNECOLOR;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.Pink_COLOR;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.RED_COLOR;
import static com.shatteredpixel.shatteredpixeldungeon.ui.Window.SKYBULE_COLOR;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.levels.hollow.MoveBoxHollowActorLevel;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.lang.reflect.Field;

public class ScoreBar extends Component {

    private Image bar;
    private Image scoreBar;
    private BitmapText scoreText;

    private Image gameStatus;

    private static ScoreBar instance;
    public static int score = 0;

    public static int Rules;

    public static int highScoreThreshold = HighScoreRules();

    private static String asset = Assets.Interfaces.SCORE_BAR;

    public ScoreBar() {
        super();
        visible = active = hero.buff(ScoreBuff.class) != null;
        instance = this;
    }

    @Override
    public synchronized void destroy() {
        super.destroy();
        if (instance == this) instance = null;
    }

    public static void setRules(int rules) {
        ScoreBuff buff = hero.buff(ScoreBuff.class);
        if(buff != null){
          buff.setGameRules(rules);
          Rules = buff.GameRules;
        } else {
            Rules = rules;
        }
    }

    @Override
    protected void createChildren() {
        bar = new Image(asset, 0, 0, 64, 16);
        add(bar);

        scoreBar = new Image(asset, 0, 28, 24, 4);
        addToBack(scoreBar);

        gameStatus = new Image(ImageRules(Rules));
        add(gameStatus);

        width = bar.width;
        height = bar.height;

        scoreText = new BitmapText(PixelScene.pixelFont);
        scoreText.alpha(0.6f);
        add(scoreText);
    }

    public static Image ImageRules(int rules) {
        Rules = rules;
        switch (rules){
            case 2:
                return new Image(asset, 16, 16, 10, 10);
            case 3:
                return new Image(asset, 32, 16, 10, 10);
            default:
                return new Image(asset, 0, 16, 10, 10);
        }
    }

    @Override
    protected void layout() {
        bar.x = x;
        bar.y = y;

        scoreText.scale.set(PixelScene.align(0.5f));
        scoreText.x = bar.x + 15;
        scoreText.y = bar.y + (bar.height - (scoreText.baseLine() + scoreText.scale.y)) / 7f;
        scoreText.y -= 0.001f; // prefer to be slightly higher
        PixelScene.align(scoreText);

        scoreBar.x = bar.x + 15;
        scoreBar.y = bar.y + 8;

        gameStatus.x = bar.x + 2;
        gameStatus.y = bar.y + 2;

        PixelScene.align(scoreBar);
    }

    public static int HighScoreRules() {
        if(Dungeon.depth == 31){
            switch (Dungeon.branch){
                case 2:
                    MoveBoxHollowActorLevel level = (Dungeon.level instanceof MoveBoxHollowActorLevel) ? (MoveBoxHollowActorLevel) Dungeon.level : null;
                    if(level != null) {
                        switch (level.rules) {
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
                    }
                    break;
                case 3:
                    highScoreThreshold = 20000;
                    break;
                default:
                    highScoreThreshold = 6000;
                    break;
            }
        } else {
            highScoreThreshold = 0;
        }
        return highScoreThreshold;
    }

    @Override
    public void update() {
        super.update();

        asset = Assets.Interfaces.SCORE_BAR;
        ScoreBuff buff = hero.buff(ScoreBuff.class);
        if (hero.buff(ScoreBuff.class) != null && Dungeon.branch == 2) {
            scoreText.text(score + "/" + highScoreThreshold + "-TURNS:"+buff.turns);
        } else {
            scoreText.text(score + "/" + highScoreThreshold);
        }

        if (score >= (highScoreThreshold * 6) / 6) {
            scoreText.hardlight(Pink_COLOR); // 优秀
            scoreBar.scale.x = (float) 6 /6;
        } else if (score >= (highScoreThreshold * 5) / 6) {
            scoreText.hardlight(Window.WATA_COLOR); // 优秀
            scoreBar.scale.x = (float) 5 /6;
        } else if (score >= (highScoreThreshold * 4) / 6) {
            scoreText.hardlight(GREEN_COLOR); // 良好
            scoreBar.scale.x = (float) 4 /6;
        } else if (score >= (highScoreThreshold * 3) / 6) {
            scoreText.hardlight(CYELLOW); // 合格
            scoreBar.scale.x = (float) 3 /6;
        } else if (score >= (highScoreThreshold * 2) / 6) {
            scoreText.hardlight(ORAGNECOLOR); // 合格偏下
            scoreBar.scale.x =(float) 2 /6;
        } else if (score >= (highScoreThreshold) / 6) {
            scoreText.hardlight(SKYBULE_COLOR); // 不及格偏上
            scoreBar.scale.x =(float) 1 /6;
        } else {
            scoreText.hardlight(RED_COLOR); // 不及格
            scoreBar.scale.x = 0f;
        }
    }

    public static void assignScore(int newScore, int highScoreThreshold) {
        if (ScoreBar.score == newScore && ScoreBar.highScoreThreshold == highScoreThreshold) {
            return;
        }
        ScoreBar.score = newScore;
        updateStatisticsScore(newScore);
        ScoreBar.highScoreThreshold = highScoreThreshold;
        if (instance != null) {
            instance.visible = instance.active = true;
            instance.layout();
        }
    }

    public static void updateStatisticsScore(int newScore) {
        ScoreBuff buff = hero.buff(ScoreBuff.class);
        if (hero.buff(ScoreBuff.class) != null) {
          buff.score = newScore;
        } else {
            Statistics.PacManScore = newScore;
        }
    }

    public static boolean isAssigned() {
        return score >= 0 && score <= highScoreThreshold;
    }

    public static void updateScoreFromBuff(Buff buff) {
        if (buff != null) {
            try {
                Field scoreField = buff.getClass().getDeclaredField("score");
                scoreField.setAccessible(true);
                Object scoreValue = scoreField.get(buff);
                if (scoreValue instanceof Integer) {
                    int newScore = (Integer) scoreValue;
                    assignScore(newScore, highScoreThreshold);
                    updateStatisticsScore(newScore);
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
            }
        }
    }
}
