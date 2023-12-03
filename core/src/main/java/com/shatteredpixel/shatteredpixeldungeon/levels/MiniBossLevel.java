package com.shatteredpixel.shatteredpixeldungeon.levels;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.gooFight;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Goo;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nxhy;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.GooBossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.CrystalChoiceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.CrystalPathRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.CrystalVaultRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.YinYangRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NxhySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MiniBossLevel extends RegularLevel {

    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        return 0;
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();

        initRooms.add( roomEntrance = new SewerBossEntranceRoom() );

        SpecialRoom s1;
        switch (Random.Int(4)) {
            case 0:
            default:
                s1 = new CrystalVaultRoom();
                initRooms.add(s1);
            break;
            case 1:
                s1 = new CrystalChoiceRoom();
                initRooms.add(s1);
                break;
            case 2:
                s1 = new CrystalPathRoom();
                initRooms.add(s1);
                break;
            case 3:
                s1 = new YinYangRoom();
                initRooms.add(s1);
                break;
        }


        GooBossRoom gooRoom = GooBossRoom.randomGooRoom();
        initRooms.add(gooRoom);
        return initRooms;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        if (transition.type == LevelTransition.Type.BRANCH_ENTRANCE) {

            if (!gooFight){
                Game.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        GLog.w(Messages.get(Goo.class, "cant_enter_1"));
                    }
                });
                return false;
            }


            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions( new NxhySprite(),
                            Messages.titleCase(Messages.get(Nxhy.class, "name")),
                            Messages.get(Goo.class, "exit_warn_none"),
                            Messages.get(Goo.class, "exit_yes"),
                            Messages.get(Goo.class, "exit_no")){
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0){
                                MiniBossLevel.super.activateTransition(hero, transition);
                            }
                        }
                    } );
                }
            });
            return false;
        } else {
            return super.activateTransition(hero, transition);
        }
    }

    @Override
    protected void createItems() {
    }

    @Override
    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createMobs() {
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
                .setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3);
    }

    @Override
    public String tilesTex() {
        if(depth>=16){
            return Assets.Environment.TILES_CITY;
        } else if(depth>=6){
            return Assets.Environment.TILES_PRISON;
        } else {
            return Assets.Environment.TILES_SEWERS;
        }

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        if (Game.versionCode > 20231118 && !gooFight){
            gooFight = true;
        }
    }

    @Override
    public String waterTex() {
        if (depth >= 16) {
            return Assets.Environment.WATER_CITY;
        } else if (depth >= 6) {
            return Assets.Environment.WATER_PRISON;
        } else {
            return Assets.Environment.WATER_SEWERS;
        }
    }

}
