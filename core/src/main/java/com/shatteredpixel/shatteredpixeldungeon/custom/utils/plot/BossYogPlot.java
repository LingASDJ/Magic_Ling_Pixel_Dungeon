package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.galaxy.SothothNPC;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SothothNPCSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;

public class BossYogPlot extends Plot {


    private final static int maxprocess = 2;

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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TRZY));
        diagulewindow.setLeftName("???");
        diagulewindow.changeText(Messages.get(SothothNPC.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.setLeftName(Messages.get(SothothNPC.class, "name"));
        diagulewindow.changeText(Messages.get(SothothNPC.class, "message2",Dungeon.hero.name()));

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SothothNPC){
                ((SothothNPCSprite)mob.sprite).setChangesBody();
            }
        }

        GameScene.scene.add(new Delayer(2.5f){
            @Override
            protected void onComplete() {
                diagulewindow.hide();
                Dungeon.level.seal();
            }
        });
    }

}
