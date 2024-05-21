package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class YetYogPlot extends Plot {


    private final static int maxprocess = 3;

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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.YTZY));
        diagulewindow.setLeftName(Messages.get(YetYog.class, "name"));
        diagulewindow.changeText(Messages.get(YetYog.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(YetYog.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(YetYog.class, "message3"));
        Dungeon.level.drop( new ScrollOfIdentify(), hero.pos );
    }

    public static class EndPlot extends Plot {


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
//                    case 2:
//                        process_to_2();
//                        break;
//                    case 3:
//                        process_to_3();
//                        break;
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YTZY));
            diagulewindow.setLeftName(Messages.get(YetYog.class, "name"));
            diagulewindow.changeText(Messages.get(YetYog.class, "message4"));
        }
    }


}

