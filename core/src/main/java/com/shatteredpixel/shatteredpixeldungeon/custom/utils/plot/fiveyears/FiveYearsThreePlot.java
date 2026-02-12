package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ATRINewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ArchettoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.BzmdrNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.HKNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.KongFuNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.NxhyFiveYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.PinkFoxNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.QianYueDeepNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.WhiteYanNewYears;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RedCrab;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.SummonElemental;
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

    public static class NxhyFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(NxhyFiveYears.class, "name"));
            diagulewindow.changeText(Messages.get(NxhyFiveYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(NxhyFiveYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(NxhyFiveYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(NxhyFiveYears.class, "messages4"));
        }

        private void DropRules(){
            Dungeon.level.drop(new NxhyFiveYears.MolotovBlazeBrew(),hero.pos);
        }

    }

    public static class ArchettoFiverYearsPlot extends Plot {
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
            SPDSettings.CatSee(true);
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName("???");
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.setLeftName(Messages.get(ArchettoNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages2"));
            SPDSettings.CatSee(true);
        }

    }

    public static class ArchettoBFiverYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(ArchettoNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages3",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages4"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages5"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new ScrollOfGolems(),hero.pos);
                Dungeon.level.drop(new SummonElemental(),hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class ArchettoCFiverYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(ArchettoNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ArchettoNewYears.class, "messages6"));
        }

    }

    public static class WhiteYanFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.WTX));
            diagulewindow.setLeftName(Messages.get(WhiteYanNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(WhiteYanNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(WhiteYanNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(WhiteYanNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(WhiteYanNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }


        private void DropRules(){
            Dungeon.level.drop(new WhiteYanNewYears.FlowerCake(), hero.pos);
        }

    }

    public static class ATRINewYearsFiverYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.ATRP));
            diagulewindow.setLeftName(Messages.get(ATRINewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ATRINewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(ATRINewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new RedCrab(),hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class BzmdrNewYearsFiverYearsPlot extends Plot {
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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
            diagulewindow.setLeftName(Messages.get(BzmdrNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(BzmdrNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(BzmdrNewYears.class, "messages2"));
        }

    }

    public static class BzmdrNewYearsFiverYearsGoPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
            diagulewindow.setLeftName(Messages.get(BzmdrNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(BzmdrNewYears.class, "messages3",hero.name()));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            Dungeon.level.drop(new BzmdrNewYears.BzmdrGift(),hero.pos);
        }
    }

    public static class BzmdrNewYearsFiverYearsSoPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BZMR));
            diagulewindow.setLeftName(Messages.get(BzmdrNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(BzmdrNewYears.class, "messages4",hero.name()));
        }


    }

    public static class KongFuNewYearsFiverYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(KongFuNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(KongFuNewYears.class, "messages1",hero.name()));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new Pasty(),hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class KongFuNewPoolYearsFiverYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(KongFuNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(KongFuNewYears.class, "messages2",hero.name()));
        }

    }

}
