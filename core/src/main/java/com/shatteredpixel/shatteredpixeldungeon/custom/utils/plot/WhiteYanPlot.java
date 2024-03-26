package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class WhiteYanPlot extends Plot {


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
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.WTX));
        diagulewindow.setLeftName(Messages.get(WhiteYan.class, "name"));
        if(Statistics.wcDialogLevel<14){
            Statistics.wcDialogLevel++;
        }
            switch (Statistics.wcDialogLevel){
                case 1:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message1"));
                    break;
                case 2:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message2"));
                    break;
                case 3:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message3"));
                    break;
                case 4:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message4"));
                    break;
                case 5:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message5"));
                    break;
                case 6:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message6"));
                    break;
                case 7:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message7"));
                    break;
                case 8:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message8"));
                    break;
                case 9:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message9"));
                    break;
                case 10:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message10"));
                    break;
                case 11:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message11"));
                    break;
                case 12:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message12"));
                    break;
                default:
                case 13:
                    diagulewindow.changeText(Messages.get(WhiteYan.class, "message13"));
                    break;
            }
        }



}
