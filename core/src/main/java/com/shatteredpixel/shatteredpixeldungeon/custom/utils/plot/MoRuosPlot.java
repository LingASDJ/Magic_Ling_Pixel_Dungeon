package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.MoRuoS;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.utils.DeviceCompat;

import java.util.List;

public class MoRuosPlot extends Plot {
    
    private static int maxprocess = 0;
    boolean ischeck;
    {
        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered(true);

        ischeck = (passwordbadges.contains(PaswordBadges.Badge.ALLCHSX) || passwordbadges.contains(PaswordBadges.Badge.GODCHSX));
        
        maxprocess = ischeck ? 5 : 3;

        process = 1;
    }

    protected String getPlotName() {
        return "";
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
        diagulewindow.setLeftName(Messages.get(MoRuoS.class,"name"));
        diagulewindow.changeText(Messages.get(MoRuoS.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(MoRuoS.class, "message2"));
    }

    private void process_to_3() {
        diagulewindow.changeText(Messages.get(MoRuoS.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.changeText(Messages.get(MoRuoS.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.changeText(Messages.get(MoRuoS.class, "message5", Dungeon.hero.name()));
        DropRules();
        skipGetItems = true;
    }

    private void DropRules(){
        if(ischeck){
            if(DeviceCompat.isDebug()){
                Dungeon.level.drop(new PotionOfPurity(), hero.pos);
            } else {
                Dungeon.level.drop(new PotionOfCleansing(), hero.pos);
            }
        }
    }
}
