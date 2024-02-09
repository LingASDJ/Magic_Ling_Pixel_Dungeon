package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.WhiteLing;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.DevItem.CrystalLing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class LingPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.LINP));
        diagulewindow.setLeftName(Messages.get(WhiteLing.class, "name"));
        diagulewindow.changeText(Messages.get(WhiteLing.class, "message1", hero.name()));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(WhiteLing.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(WhiteLing.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(WhiteLing.class, "message4"));
        Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.WAND ) ), hero.pos );
    }

    public static class LingPlotGood extends Plot {


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
                        if(Random.NormalIntRange(1,100)<=20){
                            process_to_1A();
                        } else {
                            process_to_1B();
                        }
                        break;
                    case 2:
                        process_to_2();
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
            process_to_1A();
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

        private void process_to_1A() {
            diagulewindow.hideAll();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.LINP));
            diagulewindow.setLeftName(Messages.get(WhiteLing.class, "name"));
            diagulewindow.changeText(Messages.get(WhiteLing.class, "a_message5"));
            Dungeon.level.drop( new CrystalLing(), hero.pos );
            PaswordBadges.HelloLing();
        }

        private void process_to_1B() {
            diagulewindow.hideAll();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.LINP));
            diagulewindow.setLeftName(Messages.get(WhiteLing.class, "name"));
            diagulewindow.changeText(Messages.get(WhiteLing.class, "b_message5"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(WhiteLing.class, "message6",hero.name()));
        }

    }


}
