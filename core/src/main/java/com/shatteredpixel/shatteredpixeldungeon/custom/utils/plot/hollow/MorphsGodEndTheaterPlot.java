package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.DeathRong;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class MorphsGodEndTheaterPlot extends Plot {

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    boolean alt_diaglogic = false;

    boolean branch_logic = false;

    @Override
    public void process() {

        if(diagulewindow!=null && process < 4) {
            switch (process) {
                case 1:
                    process_to_1();
                    break;
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_Select();
                    break;
            }
            diagulewindow.update();
            process++;
        } else if(diagulewindow != null && alt_diaglogic) {
            if (branch_logic) {
                switch (process) {
                    case 4:
                        process_to_4A_1();
                        break;
                    case 5:
                        process_to_5A_1();
                        break;
                }
            } else {
                switch (process) {
                    case 4:
                        process_to_4B_1();
                        break;
                    case 5:
                        process_to_5B_1();
                        break;
                }
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
        int maxprocess;
        maxprocess = 5;
        return process > maxprocess;
    }



    @Override
    public void skip() {

    }

    private void process_to_1()
    {
        diagulewindow.hideSkip();
        diagulewindow.hideAll();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_1));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(MorphsGodEndTheaterPlot.class, "message1"));
    }

    private void process_to_2()
    {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(MorphsGodEndTheaterPlot.class, "message2"));
    }

    public static class Shadow extends Item {
        {
            image = ItemSpriteSheet.CITY_HOOD;
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }
    }


    RedButton Select_B_Button;
    RedButton Select_A_Button;
    /** 分支选项 */
    private void process_to_Select()
    {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_5));
        diagulewindow.changeText(Messages.get(MorphsGodEndTheaterPlot.class, "message3"));

        Select_A_Button = new RedButton(Messages.get(DeathRong.class,"button2a")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                branch_logic = true;
                destroy();
                Select_B_Button.destroy();
                process_to_4A_1();
                diagulewindow.update();
            }
        };
        Select_A_Button.setRect(diagulewindow.thirdAvatar.x - diagulewindow.rightname.width(),diagulewindow.chrome.y-30,20,16);
        diagulewindow.add(Select_A_Button);


        Select_B_Button = new RedButton(Messages.get(DeathRong.class,"button2b")){
            @Override
            protected void onClick() {
                alt_diaglogic = true;
                destroy();
                Select_A_Button.destroy();
                diagulewindow.update();
            }
        };
        Select_B_Button.setRect(diagulewindow.thirdAvatar.x + diagulewindow.rightname.width(),diagulewindow.chrome.y-30,20,16);
        diagulewindow.add(Select_B_Button);
    }

    /** 选择A分支 */
    private void process_to_4A_1()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_1));
        diagulewindow.changeText(Messages.get(MorphsGodEndTheaterPlot.class, "message4"));
        GameScene.flash(Window.DeepPK_COLOR);
        Badges.CITY_END();
    }

    private void process_to_5A_1()
    {
        if(!(Dungeon.isDLC(Conducts.Conduct.DEV))) {
            Badges.validateVictory();
            PaswordBadges.ALLCS(Challenges.activeChallenges());
            Badges.validateChampion(Challenges.activeChallenges());
            PaswordBadges.HERO_CLRE(Challenges.activeChallenges());
            Dungeon.win(Shadow.class);
            Game.switchScene(RankingsScene.class);
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
        }
    }



    /** 选择B分支 */
    private void process_to_4B_1()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
        diagulewindow.changeText(Messages.get(MorphsGodEndTheaterPlot.class, "message5"));
    }

    private void process_to_5B_1()
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

