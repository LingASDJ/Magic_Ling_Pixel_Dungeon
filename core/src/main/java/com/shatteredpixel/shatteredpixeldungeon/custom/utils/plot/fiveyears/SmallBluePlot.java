package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.SmallBlue;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class SmallBluePlot extends Plot {
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
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.SMALLB));
        diagulewindow.setLeftName(Messages.get(SmallBlue.class, "name"));
        diagulewindow.changeText(Messages.get(SmallBlue.class, "messages1",hero.name()));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(SmallBlue.class, "messages2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(SmallBlue.class, "messages3",hero.name()));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(SmallBlue.class, "messages4"));
        DropRules();
        skipGetItems = true;
    }


    private void DropRules(){
        Dungeon.level.drop(new Gold(200), hero.pos).sprite.drop();
        Dungeon.level.drop(new Torch(), hero.pos).sprite.drop();
    }

}
