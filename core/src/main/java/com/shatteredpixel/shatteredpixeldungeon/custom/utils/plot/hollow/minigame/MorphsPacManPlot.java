package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class MorphsPacManPlot extends Plot {

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
    public void skip() {}

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.changeText(Messages.get(this, "message1a"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(this, "message2a"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(this, "message3a"));
    }

    private void process_to_4() {
       
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_4));
        diagulewindow.changeText(Messages.get(this, "message4a"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(this, "message5a"));
    }


    private void process_to_6() {
        diagulewindow.changeText(Messages.get(this, "message6a"));
    }

    public static class MorphsPacManNormalPlot extends Plot {

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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
            diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
            diagulewindow.changeText(Messages.get(this, "message1b"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(this, "message2b"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(this, "message3b"));
        }

        private void process_to_4() {
           
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
            diagulewindow.changeText(Messages.get(this, "message4b"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5b"));
        }


        private void process_to_6() {
            diagulewindow.changeText(Messages.get(this, "message6b"));
        }
    }

    public static class MorphsPacManEndPlot extends Plot {

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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
            diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
            diagulewindow.changeText(Messages.get(this, "message1c"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(this, "message2c"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(this, "message3c"));
        }

        private void process_to_4() {
           
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
            diagulewindow.changeText(Messages.get(this, "message4c"));
            Item w;
            switch (Random.Int(6)){
                default:
                case 1: w = new WaterSoul();   break;
                case 2: w = new BlizzardBrew(); break;
                case 3: w = new CausticBrew();    break;
                case 4: w = new InfernalBrew();   break;
                case 5: w = new ShockingBrew();   break;
            }
            Dungeon.level.drop(w,Dungeon.hero.pos);
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5c"));
        }


        private void process_to_6() {
            diagulewindow.changeText(Messages.get(this, "message6c"));
        }
    }

    public static class MorphsPacManGoodPlot extends Plot {

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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
            diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
            diagulewindow.changeText(Messages.get(this, "message1d"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(this, "message2d"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(this, "message3d"));
        }

        private void process_to_4() {
           
            Ankh ankh = new Ankh();
            ankh.blessed = true;
            Dungeon.level.drop(ankh,Dungeon.hero.pos);
            diagulewindow.changeText(Messages.get(this, "message4d"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5d"));
        }


        private void process_to_6() {
            diagulewindow.changeText(Messages.get(this, "message6d"));
        }
    }

    public static class MorphsPacManVeryGoodlPlot extends Plot {

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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
            diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
            diagulewindow.changeText(Messages.get(this, "message1e"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(this, "message2e"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(this, "message3e"));
        }

        private void process_to_4() {
           
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
            diagulewindow.changeText(Messages.get(this, "message4e"));
        }

        private void process_to_5() {
            Badges.MINIGAME_MASTER_ONE();
            diagulewindow.changeText(Messages.get(this, "message5e"));
        }
    }

    public static class MorphsPacManPeactPlot extends Plot {

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
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
            diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
            diagulewindow.changeText(Messages.get(this, "message1f"));
        }

        private void process_to_2() {
            diagulewindow.changeText(Messages.get(this, "message2f"));
        }

        private void process_to_3() {
            diagulewindow.changeText(Messages.get(this, "message3f"));
        }

        private void process_to_4() {
           
            diagulewindow.changeText(Messages.get(this, "message4f"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5f"));
        }


        private void process_to_6() {
            diagulewindow.changeText(Messages.get(this, "message6f"));
        }

        private void process_to_7() {
            Badges.MINIGAME_MASTER_ONE();
            diagulewindow.changeText(Messages.get(this, "message7f"));
        }
    }

}
