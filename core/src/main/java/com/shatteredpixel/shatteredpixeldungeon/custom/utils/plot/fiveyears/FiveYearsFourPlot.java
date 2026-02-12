package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.notsync.FayiNaSayBye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.KuzumiNewYears;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.TimeFlower;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;

public class FiveYearsFourPlot {
    public static class KuzumiFiveYearsPlot extends Plot {
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
                    case 1:
                        process_to_1();
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(KuzumiNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(KuzumiNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            DropRules();
            skipGetItems = true;
            diagulewindow.changeText(Messages.get(KuzumiNewYears.class, "messages2"));
        }

        private void DropRules(){
            Dungeon.level.drop(new TimeFlower(), hero.pos);
        }

    }

    public static class KuzumiFiveYearsBPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(KuzumiNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(KuzumiNewYears.class, "messages5"));
        }

    }

    public static class FayinaSayByePlotOne extends Plot {
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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.removeSkip();
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_3));
            diagulewindow.setLeftName(Messages.get(FayiNaSayBye.class, "name"));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(" ");
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages3"));
            Sample.INSTANCE.play(Assets.Sounds.DRINK);
        }


    }

    public static class FayinaSayByePlotTwo extends Plot {
        private final static int maxprocess = 12;

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
                    case 6:
                        process_to_6();
                        break;
                    case 7:
                        process_to_7();
                        break;
                    case 8:
                        process_to_8();
                        break;
                    case 9:
                        process_to_9();
                        break;
                    case 10:
                        process_to_10();
                        break;
                    case 11:
                        process_to_11();
                        break;
                    case 12:
                        process_to_12();
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
            diagulewindow.removeSkip();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_1));
            diagulewindow.setLeftName(Messages.get(FayiNaSayBye.class, "name"));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages4"));
        }

        private void process_to_2() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages5"));
        }

        private void process_to_3() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_3));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages6"));
        }

        private void process_to_4() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages7"));
        }

        private void process_to_5() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages8"));
        }

        private void process_to_6() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_1));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages9"));
        }

        private void process_to_7() {
            diagulewindow.hideAll();
            diagulewindow.setLeftName(" ");
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages10"));
        }

        private void process_to_8() {
            diagulewindow.setLeftName(Messages.get(FayiNaSayBye.class, "name"));
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_1));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages11"));
        }

        private void process_to_9() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_1));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages12"));
        }

        private void process_to_10() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages13"));
        }

        private void process_to_11() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_2));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages14"));
        }

        private void process_to_12() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_3));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages15"));
            SPDSettings.FayiNaBerry(true);
        }
    }

    public static class FayinaSayByeEndPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.FAYINA_1));
            diagulewindow.setLeftName(Messages.get(FayiNaSayBye.class, "name"));
            diagulewindow.changeText(Messages.get(FayiNaSayBye.class, "messages4"));
        }

    }

}
