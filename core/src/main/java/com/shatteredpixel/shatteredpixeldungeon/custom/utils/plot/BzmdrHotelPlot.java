package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Bzmdr;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class BzmdrHotelPlot extends Plot {


    private final static int maxprocess = 4;

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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
        diagulewindow.setLeftName(Messages.get(Bzmdr.class, "name"));
        diagulewindow.changeText(Messages.get(Bzmdr.class, "a_message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(Bzmdr.class, "b_message1"));

        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
            if(Random.NormalIntRange(0,100)<=50){
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SEED ) ), hero.pos );
            } else {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.STONE) ), hero.pos );
            }
        }

        zeroItemLevel++;
    }

    private void process_to_3() {
        if(Statistics.lanterfireactive){
            diagulewindow.changeText((Messages.get(Bzmdr.class, "c_message2")));
        } else if(Challenges.activeChallenges()==0){
            diagulewindow.changeText((Messages.get(Bzmdr.class, "c_message1")));
        } else {
            diagulewindow.changeText((Messages.get(Bzmdr.class, "c_message3")));
        }
    }

    private void process_to_4() {
        if(Challenges.activeChallenges()>=15){
            diagulewindow.changeText((Messages.get(Bzmdr.class, "d_message2")));
        } else if(Challenges.activeChallenges()>=13) {
            diagulewindow.changeText((Messages.get(Bzmdr.class, "d_message1")));
        } else {
            diagulewindow.changeText((Messages.get(Bzmdr.class, "d_message3")));
        }
    }

    public static class BzmdrHotelPlotEND extends Plot {


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
                        process_to_1();
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
            diagulewindow.setLeftName(Messages.get(Bzmdr.class, "name"));

                switch (Random.Int(3)){
                    case 0:
                        diagulewindow.changeText((Messages.get(Bzmdr.class, "e_message1")));
                        break;
                    case 1:
                        diagulewindow.changeText((Messages.get(Bzmdr.class, "e_message2")));
                        break;
                    case 2:
                        diagulewindow.changeText((Messages.get(Bzmdr.class, "e_message3")));
                        break;
                }
        }


    }


}

