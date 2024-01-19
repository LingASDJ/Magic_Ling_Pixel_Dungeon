package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Red;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.JunglePainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Random;

public class GardenLevel extends RegularLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }


    @Override
    protected int standardRooms(boolean forceMax) {
        if (forceMax) return 6;
        //4 to 6, average 5
        return 4+Random.chances(new float[]{1, 3, 1});
    }

    @Override
    protected int specialRooms(boolean forceMax) {
        if (forceMax) return 2;
        //1 to 2, average 1.8
        return 1+Random.chances(new float[]{1, 4});
    }

    @Override
    protected Painter painter() {
        return new JunglePainter()
                .setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 5)
                .setGrass(feeling == Feeling.GRASS ? 6.80f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_GARDEN;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return Dungeon.depth == 1 ?
                new Class<?>[]{ WornDartTrap.class } :
                new Class<?>[]{
                        ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, WornDartTrap.class,
                        AlarmTrap.class, OozeTrap.class,
                        ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, GatewayTrap.class };
    }

    @Override
    protected float[] trapChances() {
        return Dungeon.depth == 1 ?
                new float[]{1} :
                new float[]{
                        4, 4, 4, 4,
                        2, 2,
                        1, 1, 1, 1, 1};
    }

    @Override
    protected void createMobs() {
        super.createMobs();
    }

    @Override
    protected void createItems() {
        addItemToSpawn( new Red() );
        super.createItems();
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        return false;
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(SewerLevel.class, "water_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return Messages.get(SewerLevel.class, "empty_deco_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(SewerLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }
}
