package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class DwarfGrenEndPlot extends Plot {


    private final static int maxprocess = 41;

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

    public void process() {
        if (diagulewindow != null) {
            switch (process) {
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
                case 14:
                    process_to_14();
                    break;
                case 15:
                    process_to_15();
                    break;
                case 16:
                    process_to_16();
                    break;
                case 17:
                    process_to_17();
                    break;
                case 18:
                    process_to_18();
                    break;
                case 19:
                    process_to_19();
                    break;
                case 20:
                    process_to_20();
                    break;
                case 21:
                    process_to_21();
                    break;
                case 22:
                    process_to_22();
                    break;
                case 23:
                    process_to_23();
                    break;
                case 24:
                    process_to_24();
                    break;
                case 25:
                    process_to_25();
                    break;
                case 26:
                    process_to_26();
                    break;
                case 27:
                    process_to_27();
                    break;
                case 28:
                    process_to_28();
                    break;
                case 29:
                    process_to_29();
                    break;
                case 30:
                    process_to_30();
                    break;
                case 31:
                    process_to_31();
                    break;
                case 32:
                    process_to_32();
                    break;
                case 33:
                    process_to_33();
                    break;
                case 34:
                    process_to_34();
                    break;
                case 35:
                    process_to_35();
                    break;
                case 36:
                    process_to_36();
                    break;
                case 37:
                    process_to_37();
                    break;
                case 38:
                    process_to_38();
                    break;
                case 39:
                    process_to_39();
                    break;
                case 40:
                    process_to_40();
                    break;
                case 41:
                    process_to_41();
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
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.DG));
        diagulewindow.setLeftName(Messages.get(DwarfGeneral.class, "name"));
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message6",Dungeon.hero.name()));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message7"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message8"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message9"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message10"));
    }

    private void process_to_6() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message11"));
    }

    private void process_to_7() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message12"));
    }

    private void process_to_8() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message13"));
    }

    private void process_to_9() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message14"));
    }

    private void process_to_10() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message15"));
    }

    private void process_to_11() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message16"));
    }

    private void process_to_12() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message17"));
    }

    private void process_to_13() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message18"));
    }

    private void process_to_14() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message19"));
    }

    private void process_to_15() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message20"));
    }

    private void process_to_16() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message21"));
    }

    private void process_to_17() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message22"));
    }

    private void process_to_18() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message23"));
    }

    private void process_to_19() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message24"));
    }

    private void process_to_20() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message25"));
    }

    private void process_to_21() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message26"));
    }

    private void process_to_22() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message27"));
    }

    private void process_to_23() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message28"));
    }

    private void process_to_24() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message29"));
    }

    private void process_to_25() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message30"));
    }

    private void process_to_26() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message31"));
    }

    private void process_to_27() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message32"));
    }

    private void process_to_28() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message33"));
    }

    private void process_to_29() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message34"));
    }

    private void process_to_30() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message35"));
    }

    private void process_to_31() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message36"));
    }

    private void process_to_32() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message37"));
    }

    private void process_to_33() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message38"));
    }

    private void process_to_34() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message39"));
    }

    private void process_to_35() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message40"));
    }

    private void process_to_36() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message41"));
    }

    private void process_to_37() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message42"));
    }

    private void process_to_38() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message42"));
    }

    private void process_to_39() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message43"));
    }

    private void process_to_40() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message44"));
    }

    private void process_to_41() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message45"));
    }
}

