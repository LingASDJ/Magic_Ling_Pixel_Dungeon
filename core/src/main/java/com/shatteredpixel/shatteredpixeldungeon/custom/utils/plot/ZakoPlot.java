package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Zako;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class ZakoPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
        diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
        diagulewindow.changeText(Messages.get(Zako.class, "message1"));
    }

    public static class ZakoSecondPlot extends Plot {


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
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                    case 3:
                        process_to_3();
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message2"));

            if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
            } else {
                Item item = ( Generator.randomUsingDefaults( Generator.Category.SCROLL ));
                item.level(0);
                item.identify();
                Dungeon.level.drop( item , hero.pos );
            }

            Statistics.zeroItemLevel++;
        }

        private void process_to_2() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message3"));
        }

        private void process_to_3() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message4"));
        }
    }


    public static class ZakoRDPlot extends Plot {


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
                        process_to_1();
                        break;
                    case 2:
                        process_to_2();
                        break;
                    case 3:
                        process_to_3();
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message5"));
        }

        private void process_to_2() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message6"));
        }

        private void process_to_3() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message7"));
        }
    }



    public static class ZakoEndPlot extends Plot {


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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(Zako.class, "name"));
            diagulewindow.changeText(Messages.get(Zako.class, "message8"));
        }
    }

}

