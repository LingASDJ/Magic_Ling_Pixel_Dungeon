package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class MorphsEndTheaterPlot extends Plot {
    private final static int maxprocess = 4;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return CITY_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
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
        return process > maxprocess;
    }

    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(this,"message1"));
    }

    private void process_to_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
        diagulewindow.changeText(Messages.get(this,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_3));
        diagulewindow.changeText(Messages.get(this,"message3", Dungeon.hero.name()));
    }

    private void process_to_4()
    {
        InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
        TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (timeFreeze != null) timeFreeze.disarmPresses();
        Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
        if (timeBubble != null) timeBubble.disarmPresses();
        InterlevelScene.curTransition = new LevelTransition();
        InterlevelScene.curTransition.destDepth = 33;
        InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
        InterlevelScene.curTransition.destBranch = 0;
        InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
        InterlevelScene.curTransition.centerCell  = -1;
        Game.switchScene( InterlevelScene.class );
        Buff.detach( hero, LostInventory.class);
    }
}
