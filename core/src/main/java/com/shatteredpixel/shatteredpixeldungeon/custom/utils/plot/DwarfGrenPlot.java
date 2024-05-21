package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.DwarfGeneral;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;

public class DwarfGrenPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.DG));
        diagulewindow.setLeftName(Messages.get(DwarfGeneral.class, "name"));
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(DwarfGeneral.class, "message3"));
        GameScene.scene.add(new Delayer(1.6f){
            @Override
            protected void onComplete() {
                diagulewindow.hide();
                Dungeon.level.seal();
            }
        });
    }

}
