package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.Script;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndPacManReadyGo;
import com.watabou.noosa.Image;

public class MorphsNPCPlot extends Plot {


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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_6));
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "name"));
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_7));
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_8));
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.darkenMainAvatar();
        diagulewindow.setLeftName(Script.Name(Script.Character.SLICE));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message5"));
        Dungeon.level.seal();
        GameScene.bossReady();
    }


    private void process_to_6() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(" ");
        diagulewindow.changeText(Messages.get(MorphsNPC.class, "message6"));
        Dungeon.level.playLevelMusic();
        GameScene.show( new WndPacManReadyGo());
    }

}
