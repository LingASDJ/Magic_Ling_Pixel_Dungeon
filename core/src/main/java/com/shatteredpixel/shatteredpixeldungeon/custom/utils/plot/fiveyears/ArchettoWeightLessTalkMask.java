package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.fiveyears;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.ArchettoWeightLess;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.OldSunShadow;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;

public class ArchettoWeightLessTalkMask extends Plot {
    int maxprocess;
    {
        process = 1;
    }

    boolean alt_diaglogic = false;

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
        if(diagulewindow!=null && process <= maxprocess && alt_diaglogic) {
            switch (process) {
                case 1:
                    process_to_1();
                    break;
            }
            diagulewindow.update();
            process ++;
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

        if(alt_diaglogic){
            maxprocess = 2;
        } else {
            maxprocess = 2;
        }

        return process > maxprocess;
    }

    @Override
    public void skip() {

    }

    RedButton Select_B_Button;
    RedButton Select_A_Button;

    private void process_to_1() {
        diagulewindow.removeSkip();
        diagulewindow.hideAll();
        diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages9"));
        Select_A_Button = new RedButton(Messages.get(ArchettoWeightLess.class,"A")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                Select_B_Button.destroy();
                process_to_3A();
                process = 2;
            }
        };
        Select_A_Button.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width()-60,diagulewindow.chrome.y-30,50,16);
        diagulewindow.add(Select_A_Button);

        Select_B_Button = new RedButton(Messages.get(ArchettoWeightLess.class,"B")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                Select_A_Button.destroy();
                process_to_3B();
                process = 2;
            }
        };
        Select_B_Button.setRect(diagulewindow.thirdAvatar.x + diagulewindow.rightname.width()-30,diagulewindow.chrome.y-30,50,16);
        diagulewindow.add(Select_B_Button);
    }

    private void process_to_3A() {
        diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages10a",hero.name()));
        process++;
        TengusMask tengusMask = hero.belongings.getItem(TengusMask.class);
        if(tengusMask!=null){
            tengusMask.detach( hero.belongings.backpack );
            Dungeon.level.drop(new OldSunShadow(), hero.pos).sprite.drop();
        }
    }

    private void process_to_3B() {
        diagulewindow.setLeftName(Messages.get(ArchettoWeightLess.class, "name"));
        diagulewindow.changeText(Messages.get(ArchettoWeightLess.class, "messages10b"));
        process++;
    }

}

