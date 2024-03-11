package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.CS;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.PRO;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.LuoWhite;
import com.shatteredpixel.shatteredpixeldungeon.items.IceCyanBlueSquareCoin;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Image;

public class LuoWhitePlot extends Plot {


    private final static int maxprocess = 1;

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
        hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.LINPX));
        diagulewindow.setLeftName(Messages.get(LuoWhite.class, "name"));

        if(SPDSettings.UPICE() && !Dungeon.isChallenged(PRO)) {
            int x = 50;
            diagulewindow.changeText(Messages.get(LuoWhite.class, "message_ice2",x));
            Dungeon.level.drop_hard(new IceCyanBlueSquareCoin(x),hero.pos);
            SPDSettings.UPICE(false);
        } else if(SPDSettings.HiICE() && !Dungeon.isChallenged(PRO)) {
            diagulewindow.changeText(Messages.get(LuoWhite.class, "message_ice"));
            Dungeon.level.drop_hard(new IceCyanBlueSquareCoin(30),hero.pos);
            SPDSettings.HiICE(false);
        } else {
            if(Dungeon.isChallenged(CS)){
                diagulewindow.changeText(Messages.get(LuoWhite.class, "message3"));
            } else {
                diagulewindow.changeText(Messages.get(LuoWhite.class, "message1"));
            }

        }


    }
}
