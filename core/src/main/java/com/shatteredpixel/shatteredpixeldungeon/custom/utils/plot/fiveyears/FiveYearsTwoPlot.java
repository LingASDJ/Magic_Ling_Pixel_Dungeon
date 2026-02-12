package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.AutoShopBotNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ChocoNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MoRuoSNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ObSirNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.PinkLingNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.QinLiNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.YogSTSNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.ZakoFlowerNewYears;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfEnchantment;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class FiveYearsTwoPlot {
    public static class FlowerFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(ZakoFlowerNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ZakoFlowerNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(ZakoFlowerNewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new Pasty(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class FlowerFiveYearsEndPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COON));
            diagulewindow.setLeftName(Messages.get(ZakoFlowerNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ZakoFlowerNewYears.class, "messages3"));
        }

    }


    public static class PianoLeFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.PIANO));
            diagulewindow.setLeftName(Messages.get(QinLiNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(QinLiNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(QinLiNewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(
                        Random.Float() < 0.5f ?
                                Generator.random(Generator.Category.WEP_T2) :
                                Generator.random(Generator.Category.WEP_T3), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class MoRoseFiveYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(MoRuoSNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(MoRuoSNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(MoRuoSNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(MoRuoSNewYears.class, "messages3"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(MoRuoSNewYears.class, "messages4"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(MoRuoSNewYears.class, "messages5"));
        }

        private void DropRules(){
            Dungeon.level.drop(new StoneOfEnchantment(), hero.pos);
        }

    }

    public static class ObSirFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(ObSirNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ObSirNewYears.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(ObSirNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(ObSirNewYears.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(( Generator.randomUsingDefaults( Generator.Category.SCROLL )), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class PinkLingFiveYearsPlot extends Plot {
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
            if(!skipGetItems){
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(PinkLingNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(PinkLingNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(PinkLingNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(PinkLingNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(PinkLingNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(PinkLingNewYears.class, "messages5"));
        }


        private void DropRules(){
            Dungeon.level.drop(( Generator.randomUsingDefaults( Generator.Category.POTION )), hero.pos);
        }

    }

    public static class YogSTSFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YOGSTS_5));
            diagulewindow.setLeftName(Messages.get(YogSTSNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(YogSTSNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(YogSTSNewYears.class, "messages2"));
        }
    }

    public static class YogSTSBRFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YOGSTS_5));
            diagulewindow.setLeftName(Messages.get(YogSTSNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(YogSTSNewYears.class, "messages3"));
        }
    }

    public static class YogSTSARFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YOGSTS_5));
            diagulewindow.setLeftName(Messages.get(YogSTSNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(YogSTSNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            switch (Random.Int(3)){
                case 0:
                    Dungeon.level.drop(new ScrollOfDivination().identify(),hero.pos);
                break;
                case 1:
                    Dungeon.level.drop(new Gold(315),hero.pos);
                break;
                case 2:
                    Buff.affect(hero, Bless.class, 820f);
                break;
            }
        }



    }

    public static class YogSTSEndFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YOGSTS_5));
            diagulewindow.setLeftName(Messages.get(YogSTSNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(YogSTSNewYears.class, "messages5"));
        }
    }

    public static class AutoShopFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(AutoShopBotNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(AutoShopBotNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(AutoShopBotNewYears.class, "messages2"));
        }

    }

    public static class ChocoFiveYearsEndPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.CHOCO));
            diagulewindow.setLeftName(Messages.get(ChocoNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(ChocoNewYears.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules() {
            if (Statistics.zeroItemLevel < 4) {
                Dungeon.level.drop(new PhaseShift(), hero.pos);
                Statistics.zeroItemLevel++;
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
        }

    }


}
