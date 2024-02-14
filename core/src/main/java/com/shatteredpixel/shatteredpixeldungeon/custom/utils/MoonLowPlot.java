package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonLow;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class MoonLowPlot extends Plot {


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
                case 2:
                    process_to_2();//Mostly process to 1 is made directly when creating,it might not be used,just in case
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.LMOON));
        diagulewindow.setLeftName(Messages.get(MoonLow.class, "name"));
        diagulewindow.changeText(Messages.get(MoonLow.class, "a_message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(MoonLow.class, "b_message1"));
        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
            Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), hero.pos );
        }

        zeroItemLevel++;
    }



    public static class MoonLowPlotGO extends Plot {


        private final static int maxprocess =Dungeon.isChallenged(CS) ? 2 : 1;

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
                        if (Dungeon.isChallenged(CS)) {
                            process_to_2();
                        }
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
            diagulewindow.setLeftName(Messages.get(MoonLow.class, "name"));
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.LMOON));
            if(Challenges.activeChallenges() == 16){
                diagulewindow.changeText(Messages.get(MoonLow.class, "c_message3"));
            } else if(Challenges.activeChallenges()>0){
                diagulewindow.changeText(Messages.get(MoonLow.class, "c_message2"));
            } else {
                diagulewindow.changeText(Messages.get(MoonLow.class, "c_message1"));
            }
        }

        private void process_to_2() {
            if (Dungeon.isChallenged(CS)) {
                diagulewindow.changeText(Messages.get(MoonLow.class, "f_message1"));
            }
        }
    }

}
