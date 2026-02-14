package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.DreamLezi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Gudazi;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonCat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.DeepSeaNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.DreamLeziNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.FireMagicGirlNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.GudaziNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.IceMagicGirlNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.LuoWhiteNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MengDongXYNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.MoonCatNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.NyzNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SDragonBlue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SheepNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SlylNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.SmallLeafNewYears;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.fiveyears.YuYeNewYears;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ArcaneBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Firebomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Flashbang;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.FrostBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.HolyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Noisemaker;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShockBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.ShrapnelBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.WoollyBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChargrilledMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.FrozenCarpaccio;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.fantong.BoneSoup;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.RandomChest;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfBlessGoTend;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.curses.Wayward;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sickle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class FiveYearsPlot  {

    public static class SmallLeafPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));
            diagulewindow.changeText(Messages.get(SmallLeafNewYears.class, "message1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(SmallLeafNewYears.class, "message2",hero.name()));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new ScrollOfBlessGoTend(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class CJBlueFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.BLUECJ));
            diagulewindow.setLeftName(Messages.get(SDragonBlue.class, "name"));
            diagulewindow.changeText(Messages.get(SDragonBlue.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(SDragonBlue.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(SDragonBlue.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(Generator.random( Generator.Category.FOOD ), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class CJDreamFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(DreamLezi.class, "name"));
            diagulewindow.changeText(Messages.get(DreamLeziNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(DreamLeziNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(DreamLeziNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(DreamLeziNewYears.class, "messages4"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new FrozenCarpaccio().quantity(2), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }
    }

    public static class MDXYFiveYearsPlot extends Plot {
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
            DropRules();
            skipGetItems = true;
            diagulewindow.setLeftName(Messages.get(MengDongXYNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(MengDongXYNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(MengDongXYNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(MengDongXYNewYears.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new RandomChest(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class SlylFiveYearsPlot extends Plot {
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
            DropRules();
            skipGetItems = true;
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.COMPLEX));
            diagulewindow.setLeftName(Messages.get(SlylNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(SlylNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(SlylNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(SlylNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(SlylNewYears.class, "messages4"));
        }

        private void DropRules(){
            Dungeon.level.drop(new Pasty.RiceLiquor(), hero.pos);
        }
    }

    public static class DeepSeaFiveYearsPlot extends Plot {
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
            DropRules();
            skipGetItems = true;
            diagulewindow.setLeftName(Messages.get(DeepSeaNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(DeepSeaNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(DeepSeaNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(DeepSeaNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(DeepSeaNewYears.class, "messages4"));
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

    public static class LostWhiteFiverYearsPlot extends Plot {

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
            diagulewindow.setLeftName(Messages.get(LuoWhiteNewYears.class,"name"));
            diagulewindow.changeText(Messages.get(LuoWhiteNewYears.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(LuoWhiteNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(LuoWhiteNewYears.class, "messages3"));
        }

        private void DropRules(){
            Dungeon.level.drop(new LuoWhiteNewYears.LFRoad(), hero.pos);
        }
    }


    public static class FireMagicGirlNPCFiveYearsPlot extends Plot {

        private final static int maxprocess = 6;

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
            diagulewindow.setLeftName(Messages.get(FireMagicGirlNewYears.class,"name"));
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages1",hero.name()));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages4"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages5"));
        }

        private void process_to_6() {
            diagulewindow.changeText(Messages.get(FireMagicGirlNewYears.class, "messages6",hero.name()));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){
            Dungeon.level.drop(new BrokenBooks(),hero.pos);
            Dungeon.level.drop(new BoneSoup(),   hero.pos);
        }
    }

    public static class IceMagicGirlNPCFiveYearsPlot extends Plot {

        private final static int maxprocess = 7;

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
            if (!skipGetItems) {
                DropRules();
            }
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            hero.interrupt();
            diagulewindow.setLeftName(Messages.get(IceMagicGirlNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages4"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages5"));
        }

        private void process_to_6() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages6"));
        }

        private void process_to_7() {
            diagulewindow.changeText(Messages.get(IceMagicGirlNewYears.class, "messages7", hero.name()));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules() {
            Dungeon.level.drop(new MagicGirlBooks(), hero.pos);
            Dungeon.level.drop(new ChargrilledMeat(), hero.pos);
        }
    }

    public static class YuYeFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(YuYeNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(YuYeNewYears.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(YuYeNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(YuYeNewYears.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new ScrollOfTeleportation(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }

    public static class NyzFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.NYZ));
            diagulewindow.setLeftName(Messages.get(NyzNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(NyzNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(NyzNewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(NyzNewYears.class, "messages3"));
        }

        private void DropRules(){
            Bomb missile = generateBomb();
            missile.isLit = true;
            missile.fuse = new Bomb.Fuse();
            Actor.addDelayed(missile.fuse = missile.createFuse().ignite(missile), 2);
            Dungeon.level.drop( missile,hero.pos );
        }

        private Bomb generateBomb() {
            Bomb bomb = new Bomb();
            switch (Random.Int(8)) {
                case 0:
                    bomb = new ArcaneBomb();
                    break;
                case 1:
                    bomb = new Firebomb();
                    break;
                case 2:
                    bomb = new Flashbang();
                    break;
                case 3:
                    bomb = new FrostBomb();
                    break;
                case 4:
                    bomb = new ShrapnelBomb();
                    break;
                case 5:
                    bomb = new ShockBomb();
                    break;
                case 6:
                    bomb = new Noisemaker();
                    break;
                case 7:
                    bomb = new HolyBomb();
                    break;
            }
            return bomb;
        }

    }

    public static class MoonCatFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar((new Image(Assets.Splashes.MOON)));
            diagulewindow.setLeftName(Messages.get(MoonCat.class, "name"));
            diagulewindow.changeText(Messages.get(MoonCatNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(MoonCatNewYears.class, "messages2"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(MoonCatNewYears.class, "messages3"));
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(MoonCatNewYears.class, "messages4"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Sickle  sickle = new Sickle();
                sickle.enchantment = new Wayward();
                sickle.identify();
                Dungeon.level.drop(sickle,hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }
    }

    public static class GDaZiFiveYearsPlot extends Plot {
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
            diagulewindow.setMainAvatar((new Image(Assets.Splashes.GDZPLS)));
            diagulewindow.setLeftName(Messages.get(Gudazi.class, "name"));
            diagulewindow.changeText(Messages.get(GudaziNewYears.class, "messages1"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(GudaziNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(GudaziNewYears.class, "messages3"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_4() {
            diagulewindow.changeText(Messages.get(GudaziNewYears.class, "messages4"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new Gold(Random.Int(500,1000)), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }
    }


    public static class SheepFiveYearsPlot extends Plot {
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
            diagulewindow.setLeftName(Messages.get(SheepNewYears.class, "name"));
            diagulewindow.changeText(Messages.get(SheepNewYears.class, "messages1"));
            DropRules();
            skipGetItems = true;
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(SheepNewYears.class, "messages2"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(SheepNewYears.class, "messages3"));
        }

        private void DropRules(){
            if(Statistics.zeroItemLevel < 4){
                Dungeon.level.drop(new WoollyBomb(), hero.pos);
            } else {
                Dungeon.level.drop(new Gold(10), hero.pos);
            }
            Statistics.zeroItemLevel++;
        }

    }


}
