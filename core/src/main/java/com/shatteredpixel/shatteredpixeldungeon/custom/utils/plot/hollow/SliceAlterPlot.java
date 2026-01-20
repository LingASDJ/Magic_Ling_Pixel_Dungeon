package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceAlter;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceGirl;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class SliceAlterPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(SliceAlter.class, "message1", hero.name()));
    }

    private void process_to_2() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(SliceAlter.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(SliceAlter.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(SliceAlter.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_4));
        diagulewindow.changeText(Messages.get(SliceAlter.class, "message5"));
    }

}
