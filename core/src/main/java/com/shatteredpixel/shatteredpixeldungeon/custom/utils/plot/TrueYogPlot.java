package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.extra.TrueYog;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.zero.YetYog;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.YetYogSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;

public class TrueYogPlot extends Plot {


    private final static int maxprocess = 6;

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

    boolean stopdialog = false;
    @Override
    public boolean end() {
        return process > maxprocess || stopdialog;
    }

    @Override
    public void skip() {
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        Dungeon.hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.YTZY));
        diagulewindow.setLeftName(Messages.get(TrueYogPlot.class, "name"));
        diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message1"));
    }

    private void process_to_2() {
        diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message2"));
    }

    private void process_to_3() {
        LockSword lock = hero.belongings.getItem(LockSword.class);

        if(lock != null && !Statistics.TrueYogNoDied){
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message3b"));
        } else {
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message3a"));
            GameScene.scene.add(new Delayer(0.1f){
                @Override
                protected void onComplete() {
                    GameScene.show(new WndOptions(new YetYogSprite(),
                            Messages.titleCase(Messages.get(YetYog.class, "name")),
                            Messages.get(TrueYog.class, "quest_start_prompt"),
                            Messages.get(TrueYog.class, "enter_yes"),
                            Messages.get(TrueYog.class, "enter_no")) {
                        @Override
                        public void onBackPressed() {}
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.detach(hero, AscensionChallenge.class);
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = 0;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.destBranch = 0;
                                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.centerCell = -1;
                                Game.switchScene(InterlevelScene.class);
                            }
                        }
                    });
                }
            });
        }
    }

    private void process_to_4() {
        LockSword lock = hero.belongings.getItem(LockSword.class);

        if(lock != null && !Statistics.TrueYogNoDied){
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message4b"));
        } else {
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message4a"));
            stopdialog = true;
        }
    }

    private void process_to_5() {
        LockSword lock = hero.belongings.getItem(LockSword.class);

        if(lock != null){
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message5b"));
        }
    }

    private void process_to_6() {
        LockSword lock = hero.belongings.getItem(LockSword.class);

        if(lock != null){
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "message6b"));
            GameScene.scene.add(new Delayer(0.1f){
                @Override
                protected void onComplete() {
                    GameScene.show(new WndOptions(new YetYogSprite(),
                            Messages.titleCase(Messages.get(YetYog.class, "name")),
                            Messages.get(TrueYog.class, "quest_boss_prompt"),
                            Messages.get(TrueYog.class, "enter_yes"),
                            Messages.get(TrueYog.class, "enter_no")) {
                        @Override
                        public void onBackPressed() {}
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Buff.detach(hero, AscensionChallenge.class);
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.YOG;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = 26;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.destBranch = 10;
                                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.centerCell = -1;
                                Game.switchScene(InterlevelScene.class);
                            }
                        }
                    });
                }
            });
        }
    }

    public static class End extends Plot {


        private final static int maxprocess = 2;

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

        boolean stopdialog = false;

        @Override
        public boolean end() {
            return process > maxprocess || stopdialog;
        }

        @Override
        public void skip() {
        }

        private void process_to_1() {
            diagulewindow.hideAll();
            Dungeon.hero.interrupt();
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.YTZY));
            diagulewindow.setLeftName(Messages.get(TrueYogPlot.class, "name"));
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "messagea"));
        }

        private void process_to_2() {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.TRZY));
            diagulewindow.setLeftName(Messages.get(TrueYogPlot.class, "name2"));
            diagulewindow.changeText(Messages.get(TrueYogPlot.class, "messageb",hero.name()));
        }
    }
}
