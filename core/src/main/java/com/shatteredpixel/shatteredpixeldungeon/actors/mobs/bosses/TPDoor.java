package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.bosses;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.status.FoundChest;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NTNPC;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TPDoorSprites;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class TPDoor extends NTNPC {

    @Override
    public int drRoll() {
        return 0;
    }

    {
        spriteClass = TPDoorSprites.class;
        HP = HT = 100;
        properties.add(Property.MINIBOSS);
        state = PASSIVE;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return false;
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);

        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                GameScene.show(new WndOptions(new TPDoorSprites(),
                        Messages.titleCase(Messages.get(this, Statistics.TPDoorDieds ? "leave" : "name")),
                        Messages.get(this, Statistics.TPDoorDieds ?"quest_start_ender" : "quest_start_prompt"),
                        Messages.get(this, "enter_yes"),
                        Messages.get(this, "enter_no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            if(Statistics.TPDoorDieds){
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = depth;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
                                InterlevelScene.curTransition.destBranch = 0;
                                InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
                                InterlevelScene.curTransition.centerCell = -1;
                                Game.switchScene(InterlevelScene.class);
                                Buff.detach(hero, FoundChest.class);
                            } else {
                                TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                if (timeFreeze != null) timeFreeze.disarmPresses();
                                Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                if (timeBubble != null) timeBubble.disarmPresses();
                                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
                                InterlevelScene.curTransition = new LevelTransition();
                                InterlevelScene.curTransition.destDepth = depth;
                                InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.destBranch = 4;
                                InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                InterlevelScene.curTransition.centerCell = -1;
                                Game.switchScene(InterlevelScene.class);
                                Buff.affect(hero, FoundChest.class).set(100, 1);
                            }

                        }
                    }
                });
            }

        });

        return true;
    }


}

