package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.FireMagicGirl;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class FireMagicGirlPlot extends Plot {
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
        diagulewindow.setLeftName(Messages.get(FireMagicGirl.class, "name"));
        diagulewindow.changeText(Messages.get(FireMagicGirl.class, "messages1",hero.name()));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(FireMagicGirl.class, "messages2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(FireMagicGirl.class, "messages3",hero.name()));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(FireMagicGirl.class, "messages4"));
        DropRules();
        skipGetItems = true;
    }


    private void DropRules(){
        Dungeon.level.drop(new PotionOfLiquidFlame().identify(), hero.pos).sprite.drop();
    }

}
