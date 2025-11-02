package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses.hollow.Morphs;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.SliceGirl;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class HollowEndStoryPlot extends Plot {


    private final static int maxprocess = 12;

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
    public void skip() {}

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_6));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message2"));
    }

    private void process_to_3() {
        ScrollOfTeleportation.teleportToLocation(hero,362);
        Rat rat = new Rat();
        rat.flying = true;
        rat.pos = 1;
        GameScene.add(rat);
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Rat) {
                MagicMissile.boltFromChar(mob.sprite.parent,
                        MagicMissile.RAINBOW,
                        new MissileSprite(),
                        212,
                        () -> {
                            SliceGirl sliceGirl = new SliceGirl();
                            sliceGirl.pos = 212;
                            GameScene.add(sliceGirl);
                            mob.die(null);
                        });
            }
        }

        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_1));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message3"));
    }

    private void process_to_4() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_7));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message4"));
    }

    private void process_to_5() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_4));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message5"));
    }


    private void process_to_6() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_7));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message6"));

    }

    private void process_to_7(){
        Camera.main.shake(1f,5f);
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_3));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message7"));
    }

    private void process_to_8(){
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_7));
        diagulewindow.setLeftName(Messages.get(Morphs.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message8"));
    }

    private void process_to_9(){
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_5));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message9"));
    }

    private void process_to_10(){
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_2));
        diagulewindow.setLeftName(Messages.get(SliceGirl.class, "name"));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message10",hero.name()));
    }
    private static int[] FirstPos = new int[]{304,512,112,320};
    private static int[] EndPos =   new int[]{192,182,442,432};
    private void process_to_11(){
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_4));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message11"));
        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof Morphs) {
                mob.sprite.idle();
                mob.sprite.jump(312, 312, 5, 1f,new Callback() {
                    @Override
                    public void call() {
                        Game.runOnRenderThread(new Callback() {
                            @Override
                            public void call() {
                                mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[0], Effects.Type.M_CHAIN,null));
                                mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[1], Effects.Type.D_CHAIN,null));
                                mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[2], Effects.Type.P_CHAIN, null));
                                mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[3], Effects.Type.L_CHAIN, null));

                                mob.sprite.parent.add(new Chains(FirstPos[0], EndPos[0], Effects.Type.M_CHAIN, null));
                                mob.sprite.parent.add(new Chains(FirstPos[1], EndPos[1], Effects.Type.D_CHAIN, null));
                                mob.sprite.parent.add(new Chains(FirstPos[2], EndPos[2], Effects.Type.P_CHAIN,null));
                                mob.sprite.parent.add(new Chains(FirstPos[3], EndPos[3], Effects.Type.L_CHAIN,
                                        new Callback() {
                                    @Override
                                    public void call() {
                                        GameScene.flash(Window.Pink_COLOR);
                                        mob.die(null);
                                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                                            if (mob instanceof SliceGirl) {
                                                mob.die(true);
                                            }
                                        }
                                    }
                                }));
                            }
                        });
                    }
                });
            }
        }
    }

    private void process_to_12(){
        GameScene.fadeToWhite(2f,6f);
        GameScene.scene.add(new Delayer(5f){
            @Override
            protected void onComplete() {
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                if (timeFreeze != null) timeFreeze.disarmPresses();
                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                if (timeBubble != null) timeBubble.disarmPresses();
                InterlevelScene.curTransition = new LevelTransition();
                InterlevelScene.curTransition.destDepth = 25;
                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                InterlevelScene.curTransition.destBranch = 10;
                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                InterlevelScene.curTransition.centerCell  = -1;
                Game.switchScene( InterlevelScene.class );

                ArrayList<TestItem> asi = hero.belongings.getAllItems(TestItem.class);
                for (TestItem w : asi.toArray(new TestItem[0])){
                    w.keptThoughLostInvent = true;
                }

                Buff.affect( hero, LostInventory.class);
            }
        });
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Silence_0));
        diagulewindow.changeText(Messages.get(HollowEndStoryPlot.class, "message12"));
    }

}
