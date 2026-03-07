package com.shatteredpixel.shatteredpixeldungeon.windows;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.activeChallenges;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.items.props.Prop;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.ui.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class WndScoreBreakdown extends Window {

    private static final int WIDTH			= 115;
    private static final int HEIGHT = 144;

    private int GAP	= 4;

    public WndScoreBreakdown(){
        resize(WIDTH, HEIGHT);
        ScrollPane pane = new ScrollPane(new Component());
        Component content = pane.content();
        this.add(pane);
        pane.setRect(0,0,WIDTH, HEIGHT);

        IconTitle title = new IconTitle( Icons.get(Icons.INFO), Messages.get(this, "title"));
        title.setRect(0, 0, WIDTH, 16);
        content.add(title);

        float pos = title.bottom()+2;

        NumberFormat num = NumberFormat.getInstance(Locale.US);


        pos = statSlot(content, Messages.get(this, "progress_title"),
                    num.format(Statistics.progressScore), pos, Statistics.progressScore >= 50_000);
        pos = addInfo(content, Messages.get(this, "progress_desc", Statistics.deepestFloor, hero.lvl), pos);

        pos = statSlot(content, Messages.get(this, "treasure_title"),
                    num.format(Statistics.treasureScore), pos, Statistics.treasureScore >= 20_000);
        pos = addInfo(content, Messages.get(this, "treasure_desc", Statistics.goldCollected, Statistics.heldItemValue), pos);

        int floorsCount = 0;
        for  (Boolean b : Statistics.floorsExplored.valueList() ) {
            if (b) floorsCount += 1;
        }
        pos = statSlot(content, Messages.get(this, "explore_title"),
                    num.format(Statistics.exploreScore), pos, Statistics.exploreScore >= 20_000);
        pos = addInfo(content, Messages.get(this, "explore_desc", floorsCount ,Statistics.floorsExplored.size), pos);

        pos = statSlot(content, Messages.get(this, "bosses_title"),
                    num.format(Statistics.totalBossScore), pos, Statistics.totalBossScore >= 18_000);
        pos = addInfo(content, Messages.get(this, "bosses_desc", Statistics.bossScores[0], Statistics.bossScores[1], Statistics.bossScores[2], Statistics.bossScores[3], Statistics.bossScores[4], Statistics.bossScores[5]), pos);

        pos = statSlot(content, Messages.get(this, "quests_title"),
                    num.format(Statistics.totalQuestScore), pos, Statistics.totalQuestScore >= 14_000);
        pos = addInfo(content, Messages.get(this, "quests_desc", Statistics.questScores[0], Statistics.questScores[1], Statistics.questScores[2], Statistics.questScores[3], Statistics.questScores[4]), pos);

        if (Statistics.winMultiplier > 1) {
            pos = statSlot(content, Messages.get(this, "win_multiplier"), Statistics.winMultiplier + "x", pos, false);
        }

        if(Statistics.seedCustom){
            pos = statSlot(content, Messages.get(this, "seed_multiplier"), "0.5" + "x", pos, false);
        }


        if(Dungeon.dlcs.isConducted(Conducts.Conduct.EASY)){
            pos = statSlot(content, Messages.get(this, "diff_multiplier"), "0.5" + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "hard_desc"), pos);
        } else if(Dungeon.dlcs.isConducted(Conducts.Conduct.NORMAL)){
            pos = statSlot(content, Messages.get(this, "diff_multiplier"), "1" + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "hard_desc"), pos);
        } else if(Dungeon.dlcs.isConducted(Conducts.Conduct.HARD)){
            pos = statSlot(content, Messages.get(this, "diff_multiplier"), "2.5" + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "hard_desc"), pos);
        }


        if (Game.scene().getClass() == GameScene.class) {
            float score = 0;
            float scoreB = 0;
            float scoreC = 0;
            ArrayList<Prop> AllProps = hero.belongings.getAllItems(Prop.class);
            for (Prop w : AllProps.toArray(new Prop[0])) {
                if (w.kind == 0) {
                    score += 0.2f;
                } else if(w.kind == 2) {
                    scoreC += 0.25f;
                } else {
                    scoreB += 0.5f;
                }
            }
            pos = statSlot(content, Messages.get(this, "bad_prop"), String.format("%.1f", scoreB) + "x", pos, false);
            pos = statSlot(content, Messages.get(this, "good_prop"), String.format("%.1f", score) + "x", pos, false);
            pos = statSlot(content, Messages.get(this, "chaos_prop"), String.format("%.1f", scoreC) + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "prop_desc"), pos);
        } else {
            pos = statSlot(content, Messages.get(this, "total_props"), (Statistics.badMultiplier + Statistics.goodMultiplier) + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "prop_desc"), pos);
        }



        if (Statistics.chalMultiplier > 1) {
            pos = statSlot(content, Messages.get(this, "challenge_multiplier"), Statistics.chalMultiplier + "x", pos, false);
            pos = addInfo(content, Messages.get(this, "challenge_multiplier_desc", activeChallenges()), pos);
        }

        pos = statSlot(content, Messages.get(this, "total"), num.format(Statistics.totalScore), pos, false);
        pos = addInfo(content, Messages.get(this, "total_desc"), pos);

        int chCount = 0;
        for (int ch : Challenges.MASKS){
            if ((Dungeon.challenges & ch) != 0 && ch <= CS && ch != DHXD) {
                chCount++;
            }
        }

        //评分系统
        LevelChecker result = new LevelChecker();

        if(chCount > 0){
            pos = statSlot(content, Messages.get(this, "total_level"), ""+activeChallenges()+"x-"+result.checkLevel(), pos,
                    false);
        } else {
            pos = statSlot(content, Messages.get(this, "total_level"), result.checkLevel(), pos,
                    false);
        }
        pos = addInfo(content, Messages.get(this, "total_level_desc"), pos);


        content.setSize(WIDTH, pos + 2);
        pane.scrollTo(0, 0);
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

