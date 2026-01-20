package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.items.Generator.randomUsingDefaults;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.SelectableWand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class MorphsAllEndPlot extends Plot {

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
            for (int i = 0; i < 2; i++) {
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.POTION ), Dungeon.hero.pos ).sprite.drop();
            }
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
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5c"));
        }


        private void process_to_6() {
            for (int i = 0; i < 2; i++) {
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.POTION ), Dungeon.hero.pos ).sprite.drop();
            }
            Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
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
            diagulewindow.changeText(Messages.get(this, "message4d"));
        }

        private void process_to_5() {
            diagulewindow.changeText(Messages.get(this, "message5d"));
        }


        private void process_to_6() {
            for (int i = 0; i < 2; i++) {
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.POTION ), Dungeon.hero.pos ).sprite.drop();
            }
            Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            switch (Random.Int(8)) {
                case 2:
                    Dungeon.level.drop(new BrokenBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 3:
                    Dungeon.level.drop(new IceCityBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 4:
                    Dungeon.level.drop(new NoKingMobBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 5:
                    Dungeon.level.drop(new DeepBloodBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 6:
                    Dungeon.level.drop(new MagicGirlBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 7:
                    Dungeon.level.drop(new GrassKingBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                default:
                    Dungeon.level.drop(new YellowSunBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
            }
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
            for (int i = 0; i < 3; i++) {
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.POTION ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            }
            switch (Random.Int(8)) {
                case 2:
                    Dungeon.level.drop(new BrokenBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 3:
                    Dungeon.level.drop(new IceCityBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 4:
                    Dungeon.level.drop(new NoKingMobBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 5:
                    Dungeon.level.drop(new DeepBloodBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 6:
                    Dungeon.level.drop(new MagicGirlBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                case 7:
                    Dungeon.level.drop(new GrassKingBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
                default:
                    Dungeon.level.drop(new YellowSunBooks(), Dungeon.hero.pos).sprite.drop();
                    break;
            }
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
            diagulewindow.changeText(Messages.get(this, "message7f"));
            for (int i = 0; i < 3; i++) {
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
                Dungeon.level.drop( randomUsingDefaults( Generator.Category.POTION ), Dungeon.hero.pos ).sprite.drop();
            }
            for (int i = 0; i < 3; i++) {
                Dungeon.level.drop( new PotionOfHealing(), Dungeon.hero.pos ).sprite.drop();
            }
            for (int i = 0; i < 2; i++) {
                switch (Random.Int(8)) {
                    case 2:
                        Dungeon.level.drop(new BrokenBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    case 3:
                        Dungeon.level.drop(new IceCityBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    case 4:
                        Dungeon.level.drop(new NoKingMobBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    case 5:
                        Dungeon.level.drop(new DeepBloodBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    case 6:
                        Dungeon.level.drop(new MagicGirlBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    case 7:
                        Dungeon.level.drop(new GrassKingBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                    default:
                        Dungeon.level.drop(new YellowSunBooks(), Dungeon.hero.pos).sprite.drop();
                        break;
                }
            }

            Ankh ankh = new Ankh();
            ankh.blessed = true;
            Dungeon.level.drop(ankh, Dungeon.hero.pos).sprite.drop();

            Dungeon.level.drop(new SelectableWand(),Dungeon.hero.pos).sprite.drop();
            Dungeon.level.drop(new SelectableWand(),Dungeon.hero.pos).sprite.drop();
        }
    }

}
