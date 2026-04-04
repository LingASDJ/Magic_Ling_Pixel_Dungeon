package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoonLow;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class MoonLowJuicePlot extends Plot {
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
        diagulewindow.setLeftName(Messages.get(MoonLow.class, "name"));
        diagulewindow.changeText(Messages.get(MoonLow.class, "messages5"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(MoonLow.class, "messages6"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(MoonLow.class, "messages7"));
        DropRules();
        skipGetItems = true;
    }

    private void DropRules(){
        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
            Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.POTION ) ), hero.pos );
        }
        zeroItemLevel++;
    }


}
