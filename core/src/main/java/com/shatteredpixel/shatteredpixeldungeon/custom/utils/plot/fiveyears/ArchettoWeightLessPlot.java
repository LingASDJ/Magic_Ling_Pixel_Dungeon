package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TimeStasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.ArchettoWeightLess;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ArchettoWeightLessPlot extends Plot {
    private final static int maxprocess = 5;

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
        diagulewindow.removeSkip();
        diagulewindow.hideAll();
        hero.interrupt();
        diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages2"));
        Buff.affect(hero, TimeStasis.class, 100f);
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        hero.sprite.emitter().burst(Speck.factory(Speck.STEAM), 10);
        GLog.w(Messages.get(CursedWand.class, "petrify"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages3"));
        //Dungeon.level.drop(new BloodRedFlower(), hero.pos).sprite.drop();
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages4"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages5"));
    }

    public static class TalkOne extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.removeSkip();
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
            diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages6"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages7"));
            //Dungeon.level.drop(new BloodRedFlower(), hero.pos).sprite.drop();
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages8"));
            Buff.affect(hero, TimeStasis.class, 100f);
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
            hero.sprite.emitter().burst(Speck.factory(Speck.STEAM), 10);
            GLog.w(Messages.get(CursedWand.class, "petrify"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages10"));
        }

    }

    public static class TalkTwo extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private static String[] TXT_RANDOM = {
                Messages.get(ArchettoWeightLess.class,"card1"),
                Messages.get(ArchettoWeightLess.class,"card2"),
        };

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
            diagulewindow.changeText(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }
    }

    public static class TalkCrash extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
            diagulewindow.changeText(Messages.get(ArchettoWeightLess.class,"go"));
        }
    }

    public static class TalkEnd extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
            diagulewindow.changeText("………………");
        }
    }

}
