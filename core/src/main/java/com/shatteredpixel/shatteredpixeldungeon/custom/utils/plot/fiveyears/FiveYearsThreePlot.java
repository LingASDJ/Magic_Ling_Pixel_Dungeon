package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.HKNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.PinkFoxNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.QianYueDeepNewYears;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class FiveYearsThreePlot {
    public static class PinkFoxFiveYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(PinkFoxNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(PinkFoxNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(PinkFoxNewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(PinkFoxNewYears.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new PotionOfLiquidFlame(), hero.pos);
                Dungeon.level.drop(new ScrollOfTransmutation(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class MoonLowFiveYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.LMOON));
            diagulewindow.setLeftName(Messages.get(QianYueDeepNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(QianYueDeepNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(QianYueDeepNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(QianYueDeepNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(QianYueDeepNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new ElixirOfDragonsBlood(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }


    public static class HKFiveYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.HK));
            diagulewindow.setLeftName(Messages.get(HKNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(HKNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(HKNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(HKNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(HKNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }


        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(Generator.random( Generator.Category.STONE ), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

}
