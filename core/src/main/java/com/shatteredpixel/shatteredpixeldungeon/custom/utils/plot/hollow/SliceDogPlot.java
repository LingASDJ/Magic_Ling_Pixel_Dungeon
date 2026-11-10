package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceGirl;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SliceGirlSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.AlphaTweener;

public class SliceDogPlot extends Plot {


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
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(SliceDogPlot.class, "message1", hero.name()));
    }

    private void process_to_2() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(SliceDogPlot.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(" ");
        diagulewindow.changeText(Messages.get(SliceDogPlot.class, "message3"));
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            if (mob instanceof SliceGirl) {
                ((SliceGirl) mob).leaving = true;
                ((SliceGirl) mob).rd = false;
                if (mob.sprite == null || mob.sprite.parent == null) {
                    mob.destroy();
                    continue;
                }
                ((SliceGirlSprite)mob.sprite).leapPrep(hero.pos+4);
                mob.sprite.parent.add( new AlphaTweener( mob.sprite, 0, 3f ) {
                    @Override
                    protected void onComplete() {
                        mob.sprite.killAndErase();
                        // 光杀掉精灵还不够：NPC 本身仍在 mobs 列表里，
                        // 会留下一个看不见但能继续对话的“幽寂”，再次对话还会崩溃。
                        mob.destroy();
                        hero.spendAndNext(1);
                        hero.sprite.operate(hero.pos);
                    }
                } );
            }
        }
    }

}