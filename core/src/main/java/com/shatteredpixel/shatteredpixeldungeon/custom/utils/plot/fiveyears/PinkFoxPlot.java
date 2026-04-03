package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.normal.PinkFox;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.utils.Random;

/**
 * N:夏渔雾溟
 * D:
 * 一只正在努力修炼成九尾的普通狐狸，此刻并无心思与人交谈。
 *
 * Talk:
 * - 不必多言，我没空。
 * - 你觉得我能帮你？或许吧。
 * - 你心中所求，我已看清：%s。
 * 【奖励：随机药水 / 随机卷轴
 * （不含力量药水、升级卷轴）】
 * - .......
 */
public class PinkFoxPlot extends Plot {
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
        diagulewindow.setLeftName(Messages.get(PinkFox.class, "name"));
        diagulewindow.changeText(Messages.get(PinkFox.class, "messages1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(PinkFox.class, "messages2"));
    }

    Item item;

    private void process_to_3() {
        item = Random.Float()>=0.5f ?  ( Generator.randomUsingDefaults( Generator.Category.SCROLL )) :  ( Generator.randomUsingDefaults( Generator.Category.POTION ));
        DropRules();
        diagulewindow.changeText(Messages.get(PinkFox.class, "messages3",item.name()));
    }

    private void DropRules(){
        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
           Dungeon.level.drop(item,hero.pos);
        }
        zeroItemLevel++;
    }

}

