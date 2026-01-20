package com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status;

import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class ScoreMiniGame extends Buff {

    {
        type = buffType.NEUTRAL;
    }

    private int level = 0;
    private int interval = 1;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            spend( interval );
            if (level <= 0) {
                detach();
            }

        } else {

            detach();

        }

        return true;
    }

    @Override
    public String desc() {
        String result;
        result = super.desc();
        result  += "\n\n" + Messages.get(this, "socre", Statistics.getPacManScore,PacManScoreRules(),Statistics.getMoveBoxScore,MoveBoxScoreRules(),Statistics.getAlLSearchScore,AllSearchScoreRules(),ScoreRules());
        return result;
    }

    private static final int HIGH_SCORE_THRESHOLD = 6000;
    private static final int SCORE_LEVELS = 6;
    private static final String[] SCORE_GRADES = {"F", "E", "D", "C", "B", "A", "S"};
    private String PacManScoreRules() {
        int score = Statistics.getPacManScore;
        int level = Math.min(score / (HIGH_SCORE_THRESHOLD / SCORE_LEVELS), SCORE_LEVELS);
        return SCORE_GRADES[Math.max(level, 0)];
    }

    private String MoveBoxScoreRules(){
        final int score = Statistics.getMoveBoxScore;
        final int maxScore = Statistics.moveBoxScoreMax;
        final int scorePerLevel = maxScore / SCORE_LEVELS;

        if (score <= 0) return "F";

        int level = Math.min(score / scorePerLevel, SCORE_LEVELS);
        return SCORE_GRADES[level];
    }

    private String AllSearchScoreRules(){
        final int score = Statistics.getAlLSearchScore;
        final int maxScore = 20000;
        final int scorePerLevel = maxScore / SCORE_LEVELS;

        if (score <= 0) return "F";

        int level = Math.min(score / scorePerLevel, SCORE_LEVELS);
        return SCORE_GRADES[level];
    }


    private static final int LEVEL_PER_GRADE = 3;
    private static final String[] LEVEL_GRADES = {"F", "E", "D", "C", "B", "A", "S"};
    private String ScoreRules() {
        int level = Statistics.miniGamesTotalLevel;
        if (level < 1) return "F";
        int gradeIndex = Math.min((level - 1) / LEVEL_PER_GRADE + 1, LEVEL_GRADES.length - 1);
        return LEVEL_GRADES[gradeIndex];
    }

    public int level() {
        return level;
    }

    public void set( int value, int time ) {
        if (level <= value) {
            level = value;
            interval = time;
            spend(time - cooldown() - 1);
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.AMULET;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(Window.SKYBULE_COLOR);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(level);
    }

    private static final String LEVEL	    = "level";
    private static final String INTERVAL    = "interval";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( INTERVAL, interval );
        bundle.put( LEVEL, level );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        interval = bundle.getInt( INTERVAL );
        level = bundle.getInt( LEVEL );
    }

}

