package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class DragonBluePlot extends Plot {
    private final static int maxprocess = 4;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return CITY_NAME;
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
                    process_to_1();
                    //Mostly process to 1 is made directly when creating,it might not be used,just in case
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
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.SMALLB));
        diagulewindow.setLeftName(Script.Name(Script.Character.SMALLB));
        diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"message1",hero.name()));
    }

    private void process_to_2()
    {
        diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"message3"));
    }

    private void process_to_4()
    {
        diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"message4"));
    }


    public static class TwoDialog extends Plot {
        private final static int maxprocess = 2;

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
                        process_to_1();
                        //Mostly process to 1 is made directly when creating,it might not be used,just in case
                        break;
                    case 2:
                        process_to_2();
                        break;
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
            diagulewindow.setMainAvatar(Script.Portrait(Script.Character.SMALLB));
            diagulewindow.setLeftName(Script.Name(Script.Character.SMALLB));
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"b_message1"));
        }

        private void process_to_2()
        {
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"b_message2"));
        }

    }

    public static class ThreeDialog extends Plot {
        private final static int maxprocess = 5;

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
                        process_to_1();
                        //Mostly process to 1 is made directly when creating,it might not be used,just in case
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
            diagulewindow.setMainAvatar(Script.Portrait(Script.Character.SMALLB));
            diagulewindow.setLeftName(Script.Name(Script.Character.SMALLB));
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"c_message1"));
        }

        private void process_to_2()
        {
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"c_message2"));
        }

        private void process_to_3()
        {
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"c_message3"));
        }

        private void process_to_4()
        {
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"c_message4"));
        }

        private void process_to_5()
        {
            diagulewindow.changeText(Messages.get(DragonGirlBlue.class,"c_message5"));
        }

    }


}
