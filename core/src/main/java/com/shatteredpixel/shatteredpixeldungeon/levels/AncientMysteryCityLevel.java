package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.Collection;
import java.util.HashSet;

public class AncientMysteryCityLevel extends Level {

    private static int WIDTH = 48;
    private static int HEIGHT = 48;
    {
        color1 = 0x534f3e;
        color2 = 0xb9d661;
    }


    public HashSet<Item> heapstogen;
    public int[] heapgenspots;
    public int[] teleportspots;
    public int[] portswitchspots;
    public int[] teleportassign;
    public int[] destinationspots;
    public int[] destinationassign;
    public int prizeNo;

    private static final String HEAPSTOGEN = "heapstogen";
    private static final String HEAPGENSPOTS = "heapgenspots";
    private static final String TELEPORTSPOTS = "teleportspots";
    private static final String PORTSWITCHSPOTS = "portswitchspots";
    private static final String DESTINATIONSPOTS = "destinationspots";
    private static final String TELEPORTASSIGN = "teleportassign";
    private static final String DESTINATIONASSIGN= "destinationassign";
    private static final String PRIZENO = "prizeNo";


    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(HEAPSTOGEN, heapstogen);
        bundle.put(HEAPGENSPOTS, heapgenspots);
        bundle.put(TELEPORTSPOTS, teleportspots);
        bundle.put(PORTSWITCHSPOTS, portswitchspots);
        bundle.put(DESTINATIONSPOTS, destinationspots);
        bundle.put(DESTINATIONASSIGN, destinationassign);
        bundle.put(TELEPORTASSIGN, teleportassign);
        bundle.put(PRIZENO, prizeNo);
    }


    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WATER:
                return "Dark cold water.";
            default:
                return super.tileName(tile);
        }
    }


    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return "There are old blood stains on the floor.";
            case Terrain.BOOKSHELF:
                return "This is probably a vestige of a prison library. Might it burn?";
            default:
                return super.tileDesc(tile);
        }
    }


    @Override
    public void restoreFromBundle(Bundle bundle) {

        super.restoreFromBundle(bundle);

        heapgenspots = bundle.getIntArray(HEAPGENSPOTS);
        teleportspots = bundle.getIntArray(TELEPORTSPOTS);
        portswitchspots = bundle.getIntArray(PORTSWITCHSPOTS);
        destinationspots = bundle.getIntArray(DESTINATIONSPOTS);
        destinationassign = bundle.getIntArray(DESTINATIONASSIGN);
        teleportassign = bundle.getIntArray(TELEPORTASSIGN);
        prizeNo = bundle.getInt(PRIZENO);

        heapstogen = new HashSet<Item>();

        Collection<Bundlable> collectionheap = bundle.getCollection(HEAPSTOGEN);
        for (Bundlable i : collectionheap) {
            Item item = (Item) i;
            if (item != null) {
                heapstogen.add(item);
            }
        }
    }

    @Override
    public void create() {
        heapstogen = new HashSet<Item>();
        heapgenspots = new int[20];
        teleportspots = new int[10];
        portswitchspots = new int[10];
        destinationspots = new int[10];
        destinationassign = new int[10];
        teleportassign = new int[10];
        super.create();
    }


    public Item genPrizeItem(Class<? extends Item> match) {

        if (heapstogen.size() == 0)
            return null;

        for (Item item : heapstogen) {
            if (match.isInstance(item)) {
                heapstogen.remove(item);
                return item;
            }
        }

        Item item = Random.element(heapstogen);
        heapstogen.remove(item);
        return item;

    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ANCIENT;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_ANCIENT;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        map = SokobanLayouts.SOKOBAN_CASTLE.clone();

        buildFlagMaps();
        cleanWalls();
        createSwitches();

        entrance = 15 + WIDTH * 11;
        exit = 0;


        return true;
    }

    @Override
    protected void createMobs() {
    }



    protected void createSwitches(){

        //spots where your portals are
        teleportspots[0] = 11 + WIDTH * 10;
        teleportspots[1] = 32 + WIDTH * 15;
        teleportspots[2] = 25 + WIDTH * 40;
        teleportspots[3] = 37 + WIDTH * 18;
        teleportspots[4] = 45 + WIDTH * 33;
        teleportspots[5] = 37 + WIDTH * 3;
        teleportspots[6] = 43 + WIDTH * 2;

        //spots where your portal switches are
        portswitchspots[0] = 19 + WIDTH * 10;
        portswitchspots[1] = 19 + WIDTH * 6;
        portswitchspots[2] = 9 + WIDTH * 8;
        portswitchspots[3] = 16 + WIDTH * 37;



        //assign each switch to a portal
        teleportassign[0] = 11 + WIDTH * 10;
        teleportassign[1] = 11 + WIDTH * 10;
        teleportassign[2] = 15 + WIDTH * 32;
        teleportassign[3] = 37 + WIDTH * 3;

        //assign each switch to a destination spot
        destinationassign[0] = 30 + WIDTH * 16;
        destinationassign[1] = 23 + WIDTH * 40;
        destinationassign[2] = 37 + WIDTH * 16;
        destinationassign[3] = 42 + WIDTH * 2;



        //set the original destination of portals
        destinationspots[0] = 0;
        destinationspots[1] = 23 + WIDTH * 8;
        destinationspots[2] = 23 + WIDTH * 8;
        destinationspots[3] = 23 + WIDTH * 8;
        destinationspots[4] = 34 + WIDTH * 6;
        destinationspots[5] = 23 + WIDTH * 8;
        destinationspots[6] = 23 + WIDTH * 8;




    }


    @Override
    protected void createItems() {

    }


}