package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteYan;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class WhiteYanPlot extends Plot {


    private final static int maxprocess = 13;

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
                case 7:
                    process_to_7();
                    break;
                case 8:
                    process_to_8();
                    break;
                case 9:
                    process_to_9();
                    break;
                case 10:
                    process_to_10();
                    break;
                case 11:
                    process_to_11();
                    break;
                case 12:
                    process_to_12();
                    break;
                case 13:
                    process_to_13();
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
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message5"));
    }

    private void process_to_6() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message6"));
    }

    private void process_to_7() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message7"));
    }

    private void process_to_8() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message8"));
    }

    private void process_to_9() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message9"));
    }

    private void process_to_10() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message10"));
    }

    private void process_to_11() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message11"));
    }

    private void process_to_12() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message12"));
    }

    private void process_to_13() {
        diagulewindow.changeText(Messages.get(WhiteYan.class, "message13"));
    }

}
