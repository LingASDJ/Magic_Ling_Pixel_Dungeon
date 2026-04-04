package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.SmallLeaf;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class SmallLeafPlot extends Plot {


    private final static int maxprocess = 1;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();
                    break;
            }
            diagulewindow.update();
            process ++;
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

    private void DropRules(){
        Dungeon.level.drop(new IceCyanBlueSquareCoin(100), hero.pos).sprite.drop();
        SPDSettings.SmallLeafGetCoin(true);
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLS));
        diagulewindow.setLeftName(Messages.get(SmallLeaf.class,"name"));
        diagulewindow.changeText(Messages.get(SmallLeaf.class,"getcoin"));
        DropRules();
        skipGetItems = true;
    }

    public static class Second extends Plot {


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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
            if (!skipGetItems) {
                DropRules();
            }
        }

        private void DropRules() {
            Dungeon.level.drop(new IceCyanBlueSquareCoin(200), hero.pos).sprite.drop();
            SPDSettings.SmallLeafHCGetCoin(true);
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLS));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));
            diagulewindow.changeText(Messages.get(SmallLeaf.class, "getcoin"));
            DropRules();
            skipGetItems = true;
        }
    }

    public static class EndLess extends Plot {


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

        private static String[] TXT_RANDOM = {
                Messages.get(SmallLeaf.class,"card1"),
                Messages.get(SmallLeaf.class,"card2"),
                Messages.get(SmallLeaf.class,"card3")
        };

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
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLS));
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));
            diagulewindow.changeText(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
        }



        private void process_to_2() {
            diagulewindow.changeText(Messages.get(SmallLeaf.class, "talk"));
            DropRules();
            skipGetItems = true;
        }

        private void DropRules(){

            Item w;
            switch (Random.Int(5)){
                case 1:
                    w = new ElixirOfArcaneArmor();
                    break;
                case 2:
                    w = new ElixirOfDragonsBlood();
                    break;
                case 3:
                    w = new ElixirOfHoneyedHealing();
                    break;
                case 4:
                    w = new ElixirOfIcyTouch();
                    break;
                default:
                    w = new ElixirOfAquaticRejuvenation();
                    break;
            }

            if(zeroItemLevel >=4 && Dungeon.depth == 0) {
                Dungeon.level.drop(new Gold(1), hero.pos);
            } else {
                Dungeon.level.drop(w, hero.pos);
            }
            zeroItemLevel++;
        }

    }

    public static class PropChange extends Plot {

        public boolean change = false;

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
            diagulewindow.cancel();
            WndDialog.settedPlot = null;
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            if(Statistics.amuletObtained){
                diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLS));
            } else {
                diagulewindow.setMainAvatar(new Image(Assets.Splashes.SMLF));
            }
            diagulewindow.setLeftName(Messages.get(SmallLeaf.class, "name"));

            if(!change) {
                diagulewindow.changeText(Messages.get(SmallLeaf.class, "descofpropchange"));
            }else{
                diagulewindow.changeText(Messages.get(SmallLeaf.class, "rest"));
            }
        }
    }


}
