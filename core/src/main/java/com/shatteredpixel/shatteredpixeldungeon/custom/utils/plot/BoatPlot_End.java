package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

public class BoatPlot_End extends Plot {

    private final static int maxprocess = 9;

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
                case 8:
                    process_to_8();
                    break;
                case 9:
                    process_to_9();
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_0));
        diagulewindow.setLeftName(Messages.get(DeathRong.class, "name"));
        diagulewindow.changeText(Messages.get(this,"message1", hero.name()));
    }

    private void process_to_2() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(this,"message2", hero.name()));
    }

    private void process_to_3() {
        diagulewindow.setLeftName(Messages.get(DeathRong.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_2));
        diagulewindow.changeText(Messages.get(this,"message3", hero.name()));
    }

    private void process_to_4() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_6));
        diagulewindow.changeText(Messages.get(this,"message4", hero.name()));
    }

    private void process_to_5() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(this,"message5", hero.name()));
    }

    private void process_to_6() {
        diagulewindow.setLeftName(Messages.get(DeathRong.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_2));
        diagulewindow.changeText(Messages.get(this,"message6", hero.name()));
    }

    private void process_to_7() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_2));
        diagulewindow.changeText(Messages.get(this,"message7", hero.name()));
    }

    private void process_to_8() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_5));
        diagulewindow.changeText(Messages.get(this,"message8", hero.name()));
    }

    private void process_to_9() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.KARONG_3));
        diagulewindow.changeText(Messages.get(this,"message9", hero.name()));
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof DeathRong) {
                mob.sprite.jump(mob.pos, 356, new Callback() {
                    @Override
                    public void call() {
                        mob.move(356);
                        mob.die(null);
                    }
                });
            }
        }
    }

}

