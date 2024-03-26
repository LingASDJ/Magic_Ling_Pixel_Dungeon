package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nyz;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class NyzPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.NYZ));
        diagulewindow.setLeftName(Messages.get(Nyz.class, "name"));
        if(Dungeon.isChallenged(CS)) {
            diagulewindow.changeText(Messages.get(Nyz.class, "xhat_1",hero.name()));
        } else if(RegularLevel.holiday == RegularLevel.Holiday.CJ) {
            diagulewindow.changeText(Messages.get(Nyz.class, "dhat_1",hero.name()));
        } else {
            diagulewindow.changeText(Messages.get(Nyz.class, "chat_1",hero.name()));
        }
    }

    private void process_to_2() {
        if(Dungeon.isChallenged(CS)) {
            diagulewindow.changeText(Messages.get(Nyz.class, "xhat_2",hero.name()));
        } else if(RegularLevel.holiday == RegularLevel.Holiday.CJ) {
            diagulewindow.changeText(Messages.get(Nyz.class, "dhat_2",hero.name()));
        } else {
            diagulewindow.changeText(Messages.get(Nyz.class, "chat_2",hero.name()));
        }
    }

    private void process_to_3() {
        if(Dungeon.isChallenged(CS)) {
            diagulewindow.changeText(Messages.get(Nyz.class, "xhat_3",hero.name()));
        } else if(RegularLevel.holiday == RegularLevel.Holiday.CJ) {
            diagulewindow.changeText(Messages.get(Nyz.class, "dhat_3",hero.name()));
        } else {
            diagulewindow.changeText(Messages.get(Nyz.class, "chat_3",hero.name()));
        }
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.NYZ));
            diagulewindow.setLeftName(Messages.get(Nyz.class, "name"));
            if(Dungeon.isChallenged(CS)) {
                if(Statistics.amuletObtained){
                    diagulewindow.changeText(Messages.get(Nyz.class, "xhat_5", hero.name()));
                } else {
                    diagulewindow.changeText(Messages.get(Nyz.class, "xhat_4", hero.name()));
                }

            } else {
                diagulewindow.changeText(Messages.get(Nyz.class, "chat_4"));
            }

        }
    }


}


