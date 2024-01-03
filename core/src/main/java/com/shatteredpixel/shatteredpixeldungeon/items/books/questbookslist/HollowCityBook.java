package com.shatteredpixel.shatteredpixeldungeon.items.books.questbookslist;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.books.Books;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class HollowCityBook extends Books {

    private static final String Read	= "Read";

    {
        image = ItemSpriteSheet.HLPBOOKS;
    }



    @Override
    public void execute( Hero hero, String action ) {
        super.execute(hero, action);
        if (action.equals(Read)) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.HLPBOOKS),
                            Messages.titleCase(Messages.get(HollowCityBook.class, "name")),
                            Messages.get(HollowCityBook.class, "quest_start_prompt"),
                            Messages.get(HollowCityBook.class, "enter_yes"),
                            Messages.get(HollowCityBook.class, "enter_no")) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0 && !Statistics.endingbald) {
                                if (Dungeon.branch == 5) {
                                    InterlevelScene.mode = InterlevelScene.Mode.AMULET;
                                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                    if (timeFreeze != null) timeFreeze.disarmPresses();
                                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                    Statistics.endingbald = true;
                                    if (timeBubble != null) timeBubble.disarmPresses();
                                    InterlevelScene.curTransition = new LevelTransition();
                                    InterlevelScene.curTransition.destDepth = 25;
                                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;
                                    InterlevelScene.curTransition.destBranch = 4;
                                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                    InterlevelScene.curTransition.centerCell  = -1;
                                    Game.switchScene(InterlevelScene.class);
                                } else {
                                    InterlevelScene.mode = InterlevelScene.Mode.AMULET;
                                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                    if (timeFreeze != null) timeFreeze.disarmPresses();
                                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                    if (timeBubble != null) timeBubble.disarmPresses();
                                    InterlevelScene.curTransition = new LevelTransition();
                                    InterlevelScene.curTransition.destDepth = 25;
                                    InterlevelScene.curTransition.destType = LevelTransition.Type.REGULAR_EXIT;

                                    if(Statistics.endingbald){
                                        InterlevelScene.curTransition.destBranch = 4;
                                    } else {
                                        InterlevelScene.curTransition.destBranch = 5;
                                    }

                                    InterlevelScene.curTransition.type = LevelTransition.Type.REGULAR_EXIT;
                                    InterlevelScene.curTransition.centerCell  = -1;
                                    Game.switchScene(InterlevelScene.class);
                                }
                                detach( hero.belongings.backpack );
                            }
                        }
                    });
                }

            });
        }
    }

}
