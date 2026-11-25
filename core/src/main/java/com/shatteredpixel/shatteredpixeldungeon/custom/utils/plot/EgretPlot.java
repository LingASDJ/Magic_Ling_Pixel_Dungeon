package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.tumulus.Egret;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

/**
 * 白鹭王座前的三段对话。
 *
 * <p>结构参考 {@link FayiNaPlot} 等现有 Plot：
 * {@code initial()} 设置第一段，{@code process()} 推进，{@code end()} 在最后一段后返回 true。</p>
 */
public class EgretPlot extends Plot {

    private static final int MAX_PROCESS = 3;

    private final Runnable onEnd;
    private boolean ended;

    {
        process = 1;
    }

    public EgretPlot(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    @Override
    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while (process < needed_process) {
            process();
        }
    }

    @Override
    public void process() {
        if (diagulewindow == null) {
            return;
        }

        switch (process) {
            case 2:
                processTo2();
                break;
            case 3:
                processTo3();
                break;
            default:
                break;
        }

        diagulewindow.update();
        process++;
    }

    @Override
    public void initial(WndDialog wndDialog) {
        diagulewindow = wndDialog;
        process = 2;
        processTo1();
    }

    @Override
    public boolean end() {
        if (process > MAX_PROCESS) {
            if (!ended) {
                ended = true;
                if (onEnd != null) {
                    onEnd.run();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void skip() {

    }


    private void processTo1() {
        diagulewindow.hideSkip();
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        showLine(1);
    }

    private void processTo2() {
        showLine(2);
    }

    private void processTo3() {
        showLine(3);
    }

    private void showLine(int line) {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.EGRET_0));
        diagulewindow.setLeftName(Messages.get(Egret.class, "name"));
        diagulewindow.changeText(Messages.get(Egret.class, "dialog_" + line));
    }
}
