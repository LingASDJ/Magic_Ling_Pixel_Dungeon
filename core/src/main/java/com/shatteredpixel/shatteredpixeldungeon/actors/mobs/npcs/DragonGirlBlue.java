package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;
import static com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DragonGirlBlue.Quest.survey_research_points;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.custom.utils.DragonBluePlot;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DragonGirlBlueSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDialog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndDragonGirlBlue;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndQuest;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class DragonGirlBlue extends NTNPC{

    {
        spriteClass = DragonGirlBlueSprite.class;
        properties.add(Property.IMMOVABLE);
    }

    private boolean first=true;
    private boolean secnod=true;


    private static final String FIRST = "first";
    private static final String SECNOD = "secnod";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(FIRST, first);
        bundle.put(SECNOD, secnod);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        first = bundle.getBoolean(FIRST);
        secnod = bundle.getBoolean(SECNOD);
    }

    @Override
    protected boolean act() {
        if (hero.buff(AscensionChallenge.class) != null){
            die(null);
            Notes.remove( Notes.Landmark.SMALLB);
            return true;
        }
        if (Dungeon.level.visited[pos]){
            Notes.add( Notes.Landmark.SMALLB );
        }
        throwItem();
        sprite.turnTo( pos, hero.pos );
        spend( TICK );
        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        sprite.turnTo(pos, hero.pos);

        DragonBluePlot plot = new DragonBluePlot();

        DragonBluePlot.TwoDialog plot_2 = new DragonBluePlot.TwoDialog();

        if(first){
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot,false));
                }
            });
            first=false;
        } else if(secnod) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndDialog(plot_2,false));
                }
            });
            secnod=false;
        } else if(!Dungeon.anCityQuest2Progress) {
            LockSword lockSword = Dungeon.hero.belongings.getItem(LockSword.class);
            if(lockSword!=null) {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GameScene.show(new WndOptions(new DragonGirlBlueSprite(),
                                Messages.titleCase(Messages.get(DragonGirlBlue.class, "name")),
                                Messages.get(DragonGirlBlue.class, "quest_start_prompt"),
                                Messages.get(DragonGirlBlue.class, "enter_yes"),
                                Messages.get(DragonGirlBlue.class, "enter_no")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                                    if (timeFreeze != null) timeFreeze.disarmPresses();
                                    Swiftthistle.TimeBubble timeBubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
                                    if (timeBubble != null) timeBubble.disarmPresses();
                                    InterlevelScene.mode = InterlevelScene.Mode.ANCITYBOSS;
                                    InterlevelScene.curTransition = new LevelTransition();
                                    InterlevelScene.curTransition.destDepth = depth;
                                    LockSword lockSword = Dungeon.hero.belongings.getItem(LockSword.class);
                                    lockSword.lvl -= 300;
                                    InterlevelScene.curTransition.destType = LevelTransition.Type.BRANCH_ENTRANCE;
                                    InterlevelScene.curTransition.destBranch = Dungeon.branch + 1;
                                    InterlevelScene.curTransition.type = LevelTransition.Type.BRANCH_ENTRANCE;
                                    InterlevelScene.curTransition.centerCell = -1;
                                    Game.switchScene(InterlevelScene.class);
                                }
                            }
                        });
                    }

                });
            } else {
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                GameScene.show(new WndQuest(new DragonGirlBlue(), Messages.get(DragonGirlBlue.class,"not_locked")));
                    }
                });
                return false;
            }
        } else if(DragonGirlBlue.Quest.survey_research_points == 0) {
            yell(Messages.get(DragonGirlBlue.class,"no_anymore"));
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    if(survey_research_points>4000) survey_research_points = 4000;
                    GameScene.show(new WndDragonGirlBlue(DragonGirlBlue.this, Dungeon.hero));
                }
            });
        }

        return true;
    }

    public static class Quest {
        public static int survey_research_points;

        public static int one_used_points;
        
        public static int three_used_points;

        public static int four_used_points;

        public static int two_used_points;



        public static boolean spawned;
        private static boolean completed;
        private static boolean bossBeaten;

        public static boolean beatBoss(){
            return bossBeaten = true;
        }

        public static void reset() {
            survey_research_points = 0;

            one_used_points = 0;

            two_used_points = 0;

            three_used_points = 0;

            four_used_points = 0;

            spawned		= false;
            bossBeaten  = false;
            completed	= false;
        }

        private static final String COMPLETED	= "completed";

        private static final String NODE	= "dragon_girl";
        private static final String SURVEY	= "survey_research_points";

        private static final String ONEUSED = "one_used_points";

        private static final String TWOUSED = "two_used_points";

        private static final String THREEUSED = "three_used_points";

        private static final String FOURUSED = "four_used_points";
        private static final String SPAWNED		= "spawned";

        private static final String BOSS_BEATEN	= "boss_beaten";

        public static void storeInBundle( Bundle bundle ) {
            Bundle node = new Bundle();
            node.put( SPAWNED, spawned );
            if (spawned) {
                node.put( SURVEY, survey_research_points );

                node.put( ONEUSED, one_used_points);

                node.put(TWOUSED, two_used_points);

                node.put(THREEUSED, three_used_points);

                node.put(FOURUSED, four_used_points);

                node.put( COMPLETED, completed );
                node.put( BOSS_BEATEN, bossBeaten );
            }
            bundle.put( NODE, node );
        }

        public static void restoreFromBundle( Bundle bundle ) {

            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {
                survey_research_points = node.getInt( SURVEY );
                completed = node.getBoolean( COMPLETED );

                one_used_points = node.getInt(ONEUSED);

                two_used_points = node.getInt(TWOUSED);

                three_used_points = node.getInt(THREEUSED);

                four_used_points = node.getInt(FOURUSED);

                bossBeaten = node.getBoolean( BOSS_BEATEN );
            } else {
                reset();
            }
        }

        public static void complete(){
            completed = true;
            if(survey_research_points>4000){
                survey_research_points = 4000;
            }
        }

    }

}
