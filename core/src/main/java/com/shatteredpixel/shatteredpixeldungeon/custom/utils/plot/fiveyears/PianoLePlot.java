package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.zeroItemLevel;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.PianoLe;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.AikeLaier;
import com.shatteredpixel.shatteredpixeldungeon.plants.SkyBlueFireBloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class PianoLePlot extends Plot {
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.PIANO));
        diagulewindow.setLeftName(Messages.get(PianoLe.class, "name"));
        diagulewindow.changeText(Messages.get(PianoLe.class, "messages1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(PianoLe.class, "messages2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(PianoLe.class, "messages3"));
        DropRules();
        skipGetItems = true;
    }

    private static String[] TXT_RANDOM = {
            Messages.get(PianoLe.class,"card1"),
            Messages.get(PianoLe.class,"card2"),
            Messages.get(PianoLe.class,"card3")
    };

    private void process_to_4() {
        diagulewindow.changeText(TXT_RANDOM[Random.Int(TXT_RANDOM.length)]);
    }

    private void DropRules(){
        if(Statistics.zeroItemLevel >=4 && Dungeon.depth == 0) {
            Dungeon.level.drop(new Gold(1), hero.pos);
        } else {
            for (int i = 0; i < 2; i++) {
                Dungeon.level.drop( ( Generator.randomUsingDefaults( Generator.Category.SEED ) ), hero.pos ).sprite.drop();
            }

            Dungeon.level.drop( PlusSeedRules(), hero.pos ).sprite.drop();
        }
        zeroItemLevel++;
    }

    private Item PlusSeedRules() {
        Item item;
        switch (Random.Int(3)){
            case 1:
                item = new Sungrass.Seed();
            break;
            case 2:
                item = new SkyBlueFireBloom.Seed();
            break;
            default:
                item = new AikeLaier.Seed();
                break;
        }
        return item;
    }


}

