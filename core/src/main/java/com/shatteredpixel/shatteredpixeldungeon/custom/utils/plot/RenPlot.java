package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.REN;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.utils.Random;

public class RenPlot extends Plot {


    private final static int maxprocess = 3;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
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
//                case 4:
//                    process_to_4();
//                    break;
//                case 5:
//                    process_to_5();
//                    break;
//                case 6:
//                    process_to_6();
//                    break;
//                case 7:
//                    process_to_7();
//                    break;
//                case 8:
//                    process_to_8();
//                    break;
//                case 9:
//                    process_to_9();
//                    break;
//                case 10:
//                    process_to_10();
//                    break;
            }
            diagulewindow.update();
            process ++;
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

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.REN));
        diagulewindow.setLeftName(Script.Name(Script.Character.REN));
        diagulewindow.changeText(Messages.get(REN.class,"message1"));
    }

    private void process_to_2()
    {
        diagulewindow.changeText(Messages.get(REN.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.changeText(Messages.get(REN.class,"message3"));
    }

    public static class End extends Plot {
        private static String[] TXT_RANDOM = {Messages.get(REN.class,"roll1"),Messages.get(REN.class,"roll2"),
                Messages.get(REN.class,"roll3"), Messages.get(REN.class,"roll4"), Messages.get(REN.class,"roll5"), Messages.get(REN.class,"roll6"), Messages.get(REN.class,"roll7"), Messages.get(REN.class,"roll8"), Messages.get(REN.class,"roll9")};
        private final static int maxprocess = 1;

        {
            process = 1 ;
        }

        protected String getPlotName() {
            return SEWER_NAME;
        }

        @Override
        public void reachProcess(WndDialog wndDialog) {
            diagulewindow = wndDialog;

            while(this.process < needed_process )
            {
                this.process();
            }
        }

        @Override
        public void process() {
            if(diagulewindow!=null) {
                if (process == 1) {
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
                }
                diagulewindow.update();
                process ++;
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

        private void process_to_1()
        {
            diagulewindow.hideAll();
            diagulewindow.setMainAvatar(Script.Portrait(Script.Character.REN));
            diagulewindow.setLeftName(Script.Name(Script.Character.REN));
            diagulewindow.changeText(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }
    }

//    private void process_to_4()
//    {
//        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.CHEN));
//        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));
//        diagulewindow.changeText(Messages.get(this, "txt1"));
//    }

//    private void process_to_5()
//    {
//        diagulewindow.darkenMainAvatar();
//
//        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.RED));
//        diagulewindow.setRightName(Script.Name(Script.Character.RED));
//        diagulewindow.changeText(Messages.get(this, "txt2"));
//    }

}
