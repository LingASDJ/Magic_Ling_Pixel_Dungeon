package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.BzmdrLand;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class BzmdrLandPlot extends Plot {


    private final static int maxprocess = Badges.isUnlocked(Badges.Badge.HOLLOWCITY) ? 6 : 2;

    {
        process = 1;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while (this.process < needed_process) {
            this.process();
        }
    }

    @Override
    public void process() {
        if (diagulewindow != null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
                case 5:
                    process_to_5();
                    break;
                case 6:
                    process_to_6();
                    break;
            }
            diagulewindow.update();
            process++;
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        process_to_1();
    }

    @Override
    public boolean end() {
        return process > maxprocess;
    }

    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
        if(!skipGetItems){
            DropRules();
        }
        if(Badges.isUnlocked(Badges.Badge.HOLLOWCITY) && !skipGetItems_ALT){
            Dungeon.level.drop( new BzmdrNewYears.BzmdrGift(), Dungeon.hero.pos+1);
        }
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
        diagulewindow.setLeftName(Messages.get(BzmdrLand.class, "name"));
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message2"));
        DropRules();
        skipGetItems = true;
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message5"));
    }

    private void process_to_6() {
        diagulewindow.changeText(Messages.get(BzmdrLand.class, "message6"));
        if(Badges.isUnlocked(Badges.Badge.HOLLOWCITY)){
            Dungeon.level.drop( new BzmdrNewYears.BzmdrGift(), Dungeon.hero.pos+1);
        }
        skipGetItems_ALT = true;
    }

    private void DropRules(){
        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
            if(Random.NormalIntRange(0,100)<=50){
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SEED ) ), hero.pos );
            } else {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.STONE) ), hero.pos );
            }
        }
        zeroItemLevel++;
    }
}
