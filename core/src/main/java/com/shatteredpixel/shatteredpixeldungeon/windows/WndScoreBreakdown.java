package com.shatteredpixel.shatteredpixeldungeon.windows;

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
                    num.format(Statistics.totalBossScore), pos, Statistics.totalBossScore >= 15_000);
        pos = addInfo(this, Messages.get(this, "bosses_desc"), pos);
        pos = statSlot(this, Messages.get(this, "quests_title"),
                    num.format(Statistics.totalQuestScore), pos, Statistics.totalQuestScore >= 10_000);
        pos = addInfo(this, Messages.get(this, "quests_desc"), pos);

        if (Statistics.winMultiplier > 1) {
            pos = statSlot(this, Messages.get(this, "win_multiplier"), Statistics.winMultiplier + "x", pos, false);
        }
        if (Statistics.chalMultiplier > 1) {
            pos = statSlot(this, Messages.get(this, "challenge_multiplier"), Statistics.chalMultiplier + "x", pos, false);
        }
        pos = statSlot(this, Messages.get(this, "total"), num.format(Statistics.totalScore), pos, false);

        //评分系统
        LevelChecker result = new LevelChecker();
        pos = statSlot(this, Messages.get(this, "total_level"), result.checkLevel(), pos, false);

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

