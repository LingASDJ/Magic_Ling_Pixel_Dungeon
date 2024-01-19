package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.MOREROOM;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.PRO;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Group;

import java.text.NumberFormat;
import java.util.Locale;

public class WndScoreBreakdown extends Window {

    private static final int WIDTH			= 115;

    private int GAP	= 4;

    public WndScoreBreakdown(){

        IconTitle title = new IconTitle( Icons.get(Icons.INFO), Messages.get(this, "title"));
        title.setRect(0, 0, WIDTH, 16);
        add(title);

        float pos = title.bottom()+2;

        NumberFormat num = NumberFormat.getInstance(Locale.US);


        pos = statSlot(this, Messages.get(this, "progress_title"),
                    num.format(Statistics.progressScore), pos, Statistics.progressScore >= 50_000);
        pos = addInfo(this, Messages.get(this, "progress_desc"), pos);
        pos = statSlot(this, Messages.get(this, "treasure_title"),
                    num.format(Statistics.treasureScore), pos, Statistics.treasureScore >= 20_000);
        pos = addInfo(this, Messages.get(this, "treasure_desc"), pos);
        pos = statSlot(this, Messages.get(this, "explore_title"),
                    num.format(Statistics.exploreScore), pos, Statistics.exploreScore >= 20_000);
        pos = addInfo(this, Messages.get(this, "explore_desc"), pos);
        pos = statSlot(this, Messages.get(this, "bosses_title"),
                    num.format(Statistics.totalBossScore), pos, Statistics.totalBossScore >= 18_000);
        pos = addInfo(this, Messages.get(this, "bosses_desc"), pos);
        pos = statSlot(this, Messages.get(this, "quests_title"),
                    num.format(Statistics.totalQuestScore), pos, Statistics.totalQuestScore >= 14_000);
        pos = addInfo(this, Messages.get(this, "quests_desc"), pos);


//        String chalString = "";
//        float ratio =  1f-(1f - ( (float) Rankings.INSTANCE.wonNumber / Rankings.INSTANCE.totalNumber ) * 100f );
//
//        pos = statSlot(this, "总局数", Rankings.INSTANCE.totalNumber+"局", pos, false);
//        pos = statSlot(this, "总胜利", Rankings.INSTANCE.wonNumber+"局", pos, false);
//
//        pos = statSlot(this, "总胜率", String.format("%.2f", ratio) + "%", pos, false);
//
//        pos = statSlot(this, "地牢意识", "再接再厉", pos, false);


        if (Statistics.winMultiplier > 1) {
            pos = statSlot(this, Messages.get(this, "win_multiplier"), Statistics.winMultiplier + "x", pos, false);
        }

        if(Statistics.seedCustom){
            pos = statSlot(this, Messages.get(this, "seed_multiplier"), "0.5" + "x", pos, false);
        }

        if (Statistics.chalMultiplier > 1) {
            pos = statSlot(this, Messages.get(this, "challenge_multiplier"), Statistics.chalMultiplier + "x", pos, false);
        }

        pos = statSlot(this, Messages.get(this, "total"), num.format(Statistics.totalScore), pos, false);


        int chCount = 0;
        for (int ch : Challenges.MASKS){
            if ((Dungeon.challenges & ch) != 0 && ch <= MOREROOM && ch != PRO && ch != DHXD) {
                chCount++;
            }
        }

        //评分系统
        LevelChecker result = new LevelChecker();

        if(chCount > 0){
            pos = statSlot(this, Messages.get(this, "total_level"), ""+chCount+"x-"+result.checkLevel(), pos,
                    false);
        } else {
            pos = statSlot(this, Messages.get(this, "total_level"), result.checkLevel(), pos,
                    false);
        }


        resize(WIDTH, (int)pos);

    }

    private float statSlot(Group parent, String label, String value, float pos, boolean highlight ) {

        RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
        if (highlight) txt.hardlight(Window.TITLE_COLOR);
        txt.setPos(0, pos);
        parent.add( txt );

        txt = PixelScene.renderTextBlock( value, 7 );
        if (highlight) txt.hardlight(Window.TITLE_COLOR);
        txt.setPos(WIDTH * 0.7f, pos);
        PixelScene.align(txt);
        parent.add( txt );

        return pos + GAP + txt.height();
    }

    private float addInfo(Group parent, String info, float pos){

        RenderedTextBlock txt = PixelScene.renderTextBlock( info, 5 );
        txt.maxWidth(WIDTH);
        txt.hardlight(0x999999);
        txt.setPos(0, pos-2);
        parent.add( txt );

        return pos - 2 + GAP + txt.height();

    }


}

