package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class SmallLeafPlot extends Plot {


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
            switch (process) {
                default:
                case 1:
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
                    break;
//                case 2:
//                    process_to_2();
//                    break;
//                case 3:
//                    process_to_3();
//                    break;
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
        diagulewindow.setLeftName(Messages.get(SmallLeaf.class,"name"));
        diagulewindow.changeText(Messages.get(SmallLeaf.class,"message1"));
    }

    public static class Second extends Plot {

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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class,"name"));
            switch (Dungeon.hero.heroClass){
                default:
                    case WARRIOR:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"a_message2"));
                    break;
                case MAGE:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"b_message2"));
                    break;
                case ROGUE:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"c_message2"));
                    break;
                case HUNTRESS:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"d_message2"));
                    break;
                case DUELIST:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"e_message2"));
                    break;
            }
        }
    }

    public static class EndLess extends Plot {


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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));
            switch (Random.NormalIntRange(1,3)){
                case 1:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"message3"));
                   break;
                case 2:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"message4"));
                   break;
                case 3:
                    diagulewindow.changeText(Messages.get(SmallLeaf.class,"message5"));
                   break;
            }
        }

    }

    public static class EffectPlot extends Plot {


        private final static int maxprocess = 2;

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
           //TODO Auto-generated method stub
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));
            diagulewindow.changeText(Messages.get(SmallLeaf.class,"message6"));
        }

    }

}
