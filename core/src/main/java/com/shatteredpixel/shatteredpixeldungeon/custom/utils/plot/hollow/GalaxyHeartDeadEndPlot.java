package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.RankingsScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;

public class GalaxyHeartDeadEndPlot extends Plot {
    private final static int maxprocess = 2;

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

    public static class RedEnd extends Item {
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

    private void process_to_2()
    {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
        diagulewindow.changeText(Messages.get(this,"message2"));
        GameScene.scene.add(new Delayer(2f){
            @Override
            protected void onComplete() {
                GameScene.flash(Window.R_COLOR);
                GameScene.scene.add(new Delayer(2f){
                    @Override
                    protected void onComplete() {
                        GameScene.flash(Window.R_COLOR);
                        GameScene.scene.add(new Delayer(1f){
                            @Override
                            protected void onComplete() {
                                GameScene.flash(Window.R_COLOR);
                                Badges.CITY_END();
                                GameScene.scene.add(new Delayer(3f){
                                    @Override
                                    protected void onComplete() {
                                        Badges.validateVictory();
                                        PaswordBadges.ALLCS(Challenges.activeChallenges());
                                        Dungeon.win(RedEnd.class);
                                        Game.switchScene( RankingsScene.class );
                                        Dungeon.deleteGame( GamesInProgress.curSlot, true );
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
