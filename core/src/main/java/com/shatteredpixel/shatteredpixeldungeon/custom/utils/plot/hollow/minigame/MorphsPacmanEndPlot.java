package com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.hollow.minigame;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.ScoreBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.hollow.MorphsNPC;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.plot.Plot;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class MorphsPacmanEndPlot extends Plot {

    private final static int maxprocess = 5;

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
    }

    private void process_to_1() {
        diagulewindow.hideAll();
        hero.interrupt();
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.changeText(Messages.get(this, "message1"));

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){
            mob.die(true);
            mob.sprite.emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );
        }
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
    }

    private void process_to_2() {
        diagulewindow.hideAll();
        diagulewindow.setLeftName(hero.name());
        diagulewindow.changeText(Messages.get(this, "message2"));
    }

    private void process_to_3() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
        diagulewindow.setLeftName(Messages.get(MorphsNPC.class, "namex"));
        diagulewindow.changeText(Messages.get(this, "message3"));
    }

    private void process_to_4() {
        //最高评价
        if(Statistics.getPacManScore >= 6000){
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
            diagulewindow.changeText(Messages.get(this, "message4a"));
        //最低评价
        } else if(Statistics.getPacManScore < 1000){
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
            diagulewindow.changeText(Messages.get(this, "message4b"));
        } else {
            diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_2));
            diagulewindow.changeText(Messages.get(this, "message4c"));
        }
    }

    private void process_to_5() {
        diagulewindow.setMainAvatar(new Image(Assets.Splashes.Morphs_0));
        diagulewindow.changeText(Messages.get(this, "message5"));
        Dungeon.level.drop(new TwoLet_go(),389);
        ScrollOfTeleportation.teleportToLocation(hero,389);
        WandOfMagicMissile.AltWandOfMagicMissile alw = hero.belongings.getItem(WandOfMagicMissile.AltWandOfMagicMissile.class);
        if(alw != null){
            alw.detach(hero.belongings.backpack);
        }
        if(hero.buff(ScoreBuff.class)!=null) {
            ScoreBuff buff = hero.buff(ScoreBuff.class);
            buff.onlyChecker = true;
        }
    }

    public static class TwoLet_go extends Item {

        private static final String Read	= "Read";

        {
            image = ItemSpriteSheet.HLPBOOKS;
            cursed = false;
            stackable = true;
        }

        @Override
        public ArrayList<String> actions(Hero hero ) {
            ArrayList<String> actions = super.actions(hero);
            actions.add(Read);
            return actions;
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }

        private void ReadGame (){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new BuffIcon(BuffIndicator.BOX_GAME,true),
                            Messages.titleCase(Messages.get(TwoLet_go.class, "game")),
                            Messages.get(TwoLet_go.class, "quest_start_prompt"),
                            Messages.get(TwoLet_go.class, "enter_yes"),
                            Messages.get(TwoLet_go.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                ArrayList<TestItem> asi = hero.belongings.getAllItems(TestItem.class);
                                for (TestItem w : asi.toArray(new TestItem[0])){
                                    w.keptThoughLostInvent = true;
                                }
                                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = depth;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_ENTRANCE;
                                InterlevelScene.curTransition.destBranch = 2;
                                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.centerCell  = -1;
                                Game.switchScene( InterlevelScene.class );
                                Buff.affect( hero, LostInventory.class);
                                detach(hero.belongings.backpack);
                            }
                        }
                    });
                }
            });
        }

        @Override
        public void execute(final Hero hero, String action) {
            super.execute(hero, action);
            if (action.equals(Read)) {
               ReadGame();
            }
        }

        @Override
        public boolean doPickUp(Hero hero, int pos) {
            if (super.doPickUp( hero, pos )) {
                ReadGame();
                return true;
            } else {
                return false;
            }
        }
    }

}
