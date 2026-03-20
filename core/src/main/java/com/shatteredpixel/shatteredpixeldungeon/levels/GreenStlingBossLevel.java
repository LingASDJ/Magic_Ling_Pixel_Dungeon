package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DimandKing;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GreenStingCV;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.HashSet;

public class GreenStlingBossLevel extends Level {

    {
        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
    }

    @Override
    public void playLevelMusic(){
        Music.playModeBGM(Assets.Music.BGM_4,true);
    }

    @Override
    public void playBossMusic(){
        Music.playModeBGM(Assets.Music.CITY_BOSS,true);
    }

    private static final int WIDTH = 15;
    private static final int HEIGHT = 48;

    private static final Rect entry = new Rect(1, 37, 14, 48);
    private static final Rect arena = new Rect(1, 25, 14, 38);
    private static final Rect end = new Rect(0, 0, 15, 22);

    private static final int bottomDoor = 7 + (arena.bottom-1)*15;
    private static final int topDoor = 7 + arena.top*15;

    public static final int throne;
    private static final int[] pedestals = new int[4];

    static {
        Point c = arena.center();
        throne = c.x + (c.y) * WIDTH;
        pedestals[0] = c.x-3 + (c.y-3) * WIDTH;
        pedestals[1] = c.x+3 + (c.y-3) * WIDTH;
        pedestals[2] = c.x+3 + (c.y+3) * WIDTH;
        pedestals[3] = c.x-3 + (c.y+3) * WIDTH;
    }

    @Override
    public String tilesTex() {
        return Statistics.bossRushMode ? Assets.Environment.TILES_CITY : Assets.Environment.TILES_PRISON;
    }

    @Override
    public String waterTex() {
        return Statistics.bossRushMode ? Assets.Environment.WATER_CITY : Assets.Environment.WATER_PRISON;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
    }

    @Override
    protected boolean build() {

        setSize(WIDTH, HEIGHT);

        //entrance room
        Painter.fill(this, entry, Terrain.WALL);
        Painter.fill(this, entry, 1, Terrain.WATER);
        Painter.fill(this, entry, 2, Terrain.EMPTY);

        Painter.fill(this, entry.left+3, entry.top+3, 1, 5, Terrain.EMPTY);
        Painter.fill(this, entry.right-4, entry.top+3, 1, 5, Terrain.EMPTY);

        Point c = entry.center();

        Painter.fill(this, c.x-1, c.y-2, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x-1, c.y, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x-1, c.y+2, 3, 1, Terrain.STATUE);
        Painter.fill(this, c.x, entry.top+1, 1, 6, Terrain.EMPTY_SP);

        Painter.set(this, c.x, entry.top, Terrain.DOOR);

        int entrance = c.x + (c.y+2)*width();
        Painter.set(this, entrance, Terrain.ENTRANCE);
        transitions.add(new LevelTransition(this, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

        //DK's throne room
        Painter.fillDiamond(this, arena, 1, Terrain.EMPTY);

        Painter.fill(this, arena, 5, Terrain.WATER);
        Painter.fill(this, arena, 6, Terrain.WATER);

        c = arena.center();
        Painter.set(this, c.x-3, c.y, Terrain.STATUE);
        Painter.set(this, c.x-4, c.y, Terrain.STATUE);
        Painter.set(this, c.x+3, c.y, Terrain.STATUE);
        Painter.set(this, c.x+4, c.y, Terrain.STATUE);

        Painter.set(this, pedestals[0], Terrain.WATER);
        Painter.set(this, pedestals[1], Terrain.WATER);
        Painter.set(this, pedestals[2], Terrain.WATER);
        Painter.set(this, pedestals[3], Terrain.WATER);

        Painter.set(this, c.x, arena.top, Terrain.LOCKED_DOOR);

        //exit hallway
        Painter.fill(this, end, Terrain.CHASM);
        Painter.fill(this, end.left+4, end.top+5, 7, 18, Terrain.EMPTY);
        Painter.fill(this, end.left+4, end.top+5, 1, 1, Terrain.EXIT);

        int exitCell = end.left+7 + (end.top+8)*width();
        LevelTransition exit = new LevelTransition(this, exitCell, LevelTransition.Type.REGULAR_EXIT);
        exit.set(end.left+4, end.top+4, end.left+4+6, end.top+4+4);
        transitions.add(exit);

        Painter.fill(this, end.left+5, end.bottom+1, 5, 1, Terrain.EMPTY);
        Painter.fill(this, end.left+6, end.bottom+2, 3, 1, Terrain.EMPTY);

        //pillars last, no deco on these
        Painter.fill(this, end.left+1, end.top+2, 2, 2, Terrain.WALL);
        Painter.fill(this, end.left+1, end.top+7, 2, 2, Terrain.WALL);
        Painter.fill(this, end.left+1, end.top+12, 2, 2, Terrain.WALL);
        Painter.fill(this, end.left+1, end.top+17, 2, 2, Terrain.WALL);

        Painter.fill(this, end.right-1, end.top+2, 2, 2, Terrain.WALL);
        Painter.fill(this, end.right-1, end.top+7, 2, 2, Terrain.WALL);
        Painter.fill(this, end.right-1, end.top+12, 2, 2, Terrain.WALL);
        Painter.fill(this, end.right-1, end.top+17, 2, 2, Terrain.WALL);

        return true;
    }

    //returns a random pedestal that doesn't already have a summon inbound on it
    public int getSummoningPos(){
        Mob king = getKing();
        HashSet<DimandKing.Summoning> summons = king.buffs(DimandKing.Summoning.class);
        ArrayList<Integer> positions = new ArrayList<>();
        for (int pedestal : pedestals) {
            boolean clear = true;
            for (DimandKing.Summoning s : summons) {
                if (s.getPos() == pedestal) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                positions.add(pedestal);
            }
        }
        if (positions.isEmpty()){
            return -1;
        } else {
            return Random.element(positions);
        }
    }

    public int getSummoningXPos(){
        Mob king = getNoKing();
        HashSet<GreenStingCV.Summoning> summons = king.buffs(GreenStingCV.Summoning.class);
        ArrayList<Integer> positions = new ArrayList<>();
        for (int pedestal : pedestals) {
            boolean clear = true;
            for (GreenStingCV.Summoning s : summons) {
                if (s.getPos() == pedestal) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                positions.add(pedestal);
            }
        }
        if (positions.isEmpty()){
            return -1;
        } else {
            return Random.element(positions);
        }
    }

    private Mob getNoKing(){
        for (Mob m : mobs){
            if (m instanceof GreenStingCV) return m;
        }
        return null;
    }

    private Mob getKing(){
        for (Mob m : mobs){
            if (m instanceof DimandKing) return m;
        }
        return null;
    }

    @Override
    protected void createMobs() {
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = randomRespawnCell(null);
            } while (pos == entrance());
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        int cell;
        do {
            cell = entrance() + PathFinder.NEIGHBOURS8[Random.Int(8)];
        } while (!passable[cell]
                || (Char.hasProp(ch, Char.Property.LARGE) && !openSpace[cell])
                || Actor.findChar(cell) != null);
        return cell;
    }

    @Override
    public void occupyCell( Char ch ) {

        super.occupyCell( ch );

        if (map[bottomDoor] != Terrain.LOCKED_DOOR && map[topDoor] == Terrain.LOCKED_DOOR
                && ch.pos < bottomDoor && ch == Dungeon.hero) {

            seal();

        }
    }

    @Override
    public void seal() {
        super.seal();

        for (Mob m : mobs){
            //bring the first ally with you
            if (m.alignment == Char.Alignment.ALLY && !m.properties().contains(Char.Property.IMMOVABLE)){
                m.pos = Dungeon.hero.pos + (Random.Int(2) == 0 ? +1 : -1);
                m.sprite.place(m.pos);
                break;
            }
        }
        Mob boss;
        if(Statistics.bossRushMode){
           boss = new GreenStingCV();
        } else {
           boss = new DimandKing();
        }

        boss.state = boss.WANDERING;
        boss.pos = pointToCell(arena.center());
        GameScene.add( boss );

        if (heroFOV[boss.pos]) {
            boss.notice();
            boss.sprite.alpha( 0 );
            boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.7f ) );
        }

        set( bottomDoor, Terrain.LOCKED_DOOR );
        GameScene.updateMap( bottomDoor );
        Dungeon.observe();
    }

    @Override
    public void unseal() {
        super.unseal();

        set( bottomDoor, Terrain.DOOR );
        GameScene.updateMap( bottomDoor );

        set( topDoor, Terrain.DOOR );
        GameScene.updateMap( topDoor );

        Dungeon.observe();
    }

    @Override
    public String tileName( int tile ) {
        switch (tile) {
            case Terrain.WATER:
                return Messages.get(PrisonLevel.class, "water_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(PrisonLevel.class, "high_grass_name");
            default:
                return super.tileName( tile );
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.ENTRANCE:
                return Messages.get(CityLevel.class, "entrance_desc");
            case Terrain.EXIT:
                return Messages.get(CityLevel.class, "exit_desc");
            case Terrain.WALL_DECO:
            case Terrain.EMPTY_DECO:
                return Messages.get(CityLevel.class, "deco_desc");
            case Terrain.STATUE:
                return Messages.get(PrisonLevel.class, "statue");
            case Terrain.STATUE_SP:
                return Messages.get(PrisonLevel.class, "statue_desc");
            case Terrain.BOOKSHELF:
                return Messages.get(CityLevel.class, "bookshelf_desc");
            default:
                return super.tileDesc( tile );
        }
    }

    @Override
    public Group addVisuals( ) {
        super.addVisuals();
        CityLevel.addCityVisuals(this, visuals);
        return visuals;
    }

}

