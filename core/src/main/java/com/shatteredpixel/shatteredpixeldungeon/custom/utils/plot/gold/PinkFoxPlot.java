package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.gold;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.PinkFox;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.EndingBlade;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class PinkFoxPlot extends Plot {


    private final static int maxprocess = Statistics.isEndingbald ? 5 : 4;

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

    private void DropRules(){
        Item t1;
        t1 = new EndingBlade();
        t1.identify();
        if (t1.doPickUp(Dungeon.hero)){
            GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", t1.name())) );
        } else {
            Dungeon.level.drop(t1, Dungeon.hero.pos);
        }
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
        Dungeon.hero.interrupt();
        //diagulewindow.setMainAvatar(new Image(Assets.Splashes.PINKFOX));
        diagulewindow.setLeftName(Messages.get(PinkFox.class, "name"));
        diagulewindow.changeText(Messages.get(PinkFox.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(PinkFox.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(PinkFox.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(PinkFox.class, "message4"));
    }

    private void process_to_5() {
        if(Statistics.isEndingbald){
            diagulewindow.changeText(Messages.get(PinkFox.class, "message6"));
            skipGetItems = true;
            Item t1;
            t1 = new EndingBlade();
            t1.identify();
            if (t1.doPickUp(Dungeon.hero)){
                GLog.p( Messages.capitalize(Messages.get(Hero.class, "you_now_have", t1.name())) );
            } else {
                Dungeon.level.drop(t1, Dungeon.hero.pos);
            }
        }
    }

    public static class PinkFoxSPlot extends Plot {


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
            Dungeon.hero.interrupt();
//            diagulewindow.setMainAvatar(new Image(Assets.Splashes.PINKFOX));
            diagulewindow.setLeftName(Messages.get(PinkFox.class, "name"));
            diagulewindow.changeText(Messages.get(PinkFox.class, "message5"));
        }
    }

}

