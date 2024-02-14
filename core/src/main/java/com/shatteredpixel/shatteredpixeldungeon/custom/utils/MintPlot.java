package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.Mint;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.BrokenBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.DeepBloodBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.IceCityBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.MagicGirlBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.NoKingMobBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class MintPlot extends Plot {


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
                default:
                case 1:
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
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
//                case 6:
//                    process_to_6();
//                    break;
//                case 7:
//                    process_to_7();
//                    break;
//                case 8:
//                    process_to_8();
//                    break;
//                case 9:
//                    process_to_9();
//                    break;
//                case 10:
//                    process_to_10();
//                    break;
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
        hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.MT));
        diagulewindow.setLeftName(Messages.get(Mint.class, "name"));
        diagulewindow.changeText(Messages.get(Mint.class, "message1"));
    }

    private Item RandomBooks() {
        switch (Random.Int(8)){
            case 1: default:
                return new YellowSunBooks().quantity(1);
            case 2:
                return new BrokenBooks().quantity(1);
            case 3:
                return new IceCityBooks().quantity(1);
            case 4:
                return new NoKingMobBooks().quantity(1);
            case 5:
                return new DeepBloodBooks().quantity(1);
            case 6:
                return new MagicGirlBooks().quantity(1);
            case 7:
                return new GrassKingBooks().quantity(1);
        }
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(Mint.class, "message2"));
        //GreenStorm(hero);
    }

    private void process_to_3() {
        Dungeon.level.drop(RandomBooks(), hero.pos).sprite.drop();
        diagulewindow.changeText(Messages.get(Mint.class, "message3"));
        GreenStorm(hero);
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(Mint.class, "message4"));
    }

    public static void GreenStorm(Char ch){
        Ballistica aim;
        aim = new Ballistica(ch.pos, ch.pos - 1, Ballistica.STOP_TARGET);
        int projectileProps = Ballistica.IGNORE_SOFT_SOLID;
        int aoeSize = 6;
        ConeAOE aoe = new ConeAOE(aim, aoeSize, 360, projectileProps);
        GameScene.flash(0x00dd00);
        for (Ballistica ray : aoe.outerRays){
            ((MagicMissile)ch.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.FOLIAGE,
                    ch.sprite,
                    ray.path.get(ray.dist),
                    null
            );
        }
    }
    

    public static class EndPlot extends Plot {


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
                        process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
                        break;
//                    case 2:
//                        process_to_2();
//                        break;
//                    case 3:
//                        process_to_3();
//                        break;
//                case 4:
//                    process_to_4();
//                    break;
//                case 5:
//                    process_to_5();
//                    break;
//                case 6:
//                    process_to_6();
//                    break;
//                case 7:
//                    process_to_7();
//                    break;
//                case 8:
//                    process_to_8();
//                    break;
//                case 9:
//                    process_to_9();
//                    break;
//                case 10:
//                    process_to_10();
//                    break;
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
            hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.MT));
            diagulewindow.setLeftName(Messages.get(Mint.class, "name"));
            diagulewindow.changeText(Messages.get(Mint.class, "message5",hero.name()));
        }
    }


}


