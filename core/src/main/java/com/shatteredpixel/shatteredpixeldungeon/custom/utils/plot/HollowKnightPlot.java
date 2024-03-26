package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.HollowKnight;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class HollowKnightPlot extends Plot {
    private final static int maxprocess = 1;

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
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
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
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.HK));
        diagulewindow.setLeftName(Messages.get(HollowKnight.class, "name"));
        if(Statistics.hcDialogLevel<9){
            Statistics.hcDialogLevel++;
        }
        switch (Statistics.hcDialogLevel) {
            case 1:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message1"));
                break;
            case 2:
            if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message2B"));
            } else {
                Dungeon.level.drop( new StoneOfBlink(), hero.pos );
                Statistics.zeroItemLevel++;
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message2"));
            }
                break;
            case 3:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message3"));
                break;
            case 4:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message4"));
                break;
            case 5:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message5"));
                break;
            case 6:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message6"));
                break;
            case 7:
                diagulewindow.changeText( Messages.get(HollowKnight.class, "message7"));
                break;
            default:
            case 8:
                diagulewindow.changeText( "…………");
                break;
        }

    }
}

