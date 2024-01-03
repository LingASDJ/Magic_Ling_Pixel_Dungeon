package com.shatteredpixel.shatteredpixeldungeon.custom.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Typhon;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.Delayer;

public class TyphonPlot extends Plot {

    private final static int maxprocess = 12;

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

    @Override
    public void process() {
        if(diagulewindow!=null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
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
                case 10:
                    process_to_10();
                    break;
                case 11:
                    process_to_11();
                    break;
                case 12:
                    process_to_12();
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
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.TYPHON));
        diagulewindow.setLeftName(Messages.get(Morphs.class,"unknown"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message1",hero.name()));
    }

    private void process_to_2()
    {
        diagulewindow.setLeftName(Messages.get(Typhon.class,"name"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message2"));
    }

    private void process_to_3()
    {
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message3"));
    }

    private void process_to_4()
    {
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message4"));

    }

    private void process_to_5()
    {
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message5"));
    }

    private void process_to_6()
    {
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message6"));
    }

    private void process_to_7()
    {
        diagulewindow.darkenMainAvatar();
        diagulewindow.setSecondColor(Window.CBLACK);
        diagulewindow.setSecondAvatar(new Image(Assets.Splashes.MOSRDX));
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message7",hero.name()));
    }

    private void process_to_8()
    {
        diagulewindow.setSecondResetColor();
        diagulewindow.setRightName(Messages.get(Morphs.class,"name"));
        diagulewindow.setSecondAvatar(new Image(Assets.Splashes.MOSRDX));
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message8",hero.name()));
    }

    private void process_to_9()
    {
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message9",hero.name()));
    }

    private void process_to_10()
    {
        diagulewindow.darkenSecondAvatar();
        diagulewindow.lightenMainAvatar();
        diagulewindow.setLeftName(Messages.get(Typhon.class,"name"));
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message10",hero.name()));
    }

    private void process_to_11()
    {
        diagulewindow.darkenSecondAvatar();
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message11",hero.name()));
    }

    private void process_to_12()
    {
        diagulewindow.hideSecondAvatar();
        diagulewindow.changeText(Messages.get(TyphonPlot.class,"message12",hero.name()));
        Statistics.questScores[4] += 30000;
        Dungeon.win( Typhon.class );
        Dungeon.deleteGame( GamesInProgress.curSlot, true );
        Badges.CITY_END();
        GameScene.scene.add(new Delayer(0.1f){
            @Override
            protected void onComplete() {
                if (BadgeBanner.isShowingBadges()){
                    GameScene.scene.add(new Delayer(3f){
                        @Override
                        protected void onComplete() {
                            Game.switchScene( RankingsScene.class );
                        }
                    });
                } else {
                    Game.switchScene( RankingsScene.class );
                }
            }
        });
        Music.INSTANCE.playTracks(
                new String[]{Assets.Music.THEME_2, Assets.Music.THEME_1},
                new float[]{1, 1},
                false);
    }

//    private void process_to_4()
//    {
//        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.CHEN));
//        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));
//        diagulewindow.changeText(Messages.get(this, "txt1"));
//    }

//    private void process_to_5()
//    {
//        diagulewindow.darkenMainAvatar();
//
//        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.RED));
//        diagulewindow.setRightName(Script.Name(Script.Character.RED));
//        diagulewindow.changeText(Messages.get(this, "txt2"));
//    }

}


