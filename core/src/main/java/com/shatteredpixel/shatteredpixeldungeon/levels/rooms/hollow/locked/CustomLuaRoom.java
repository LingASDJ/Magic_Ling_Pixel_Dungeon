package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Point;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.InputStream;

public abstract class CustomLuaRoom extends ConnectionRoom {

    public int width = 0;
    public int height = 0;

    public static String map_lua_file = Assets.Map_Luas.LockedOneRoom_MapLua;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return width;
    }
    @Override
    public int maxWidth() {
        return height;
    }
    @Override
    public int maxHeight() {
        return height;
    }

    // 从Lua文件加载地图数据
    private static int[] loadMapFromLua(String t) {
        try {
            Globals globals = JsePlatform.standardGlobals();

            InputStream inputStream = Gdx.files.internal(map_lua_file).read();

            if (inputStream == null) {
                inputStream = CustomLuaRoom.class.getResourceAsStream("/" + t);

                if (inputStream == null) {
                    throw new RuntimeException("The file map_room.lua cannot be found. Try the following path:: " +
                            "assets/" + t + " and " +
                            "/" + t);
                }
            }

            LuaValue chunk = globals.load(inputStream, "map_room.lua", "t", globals);
            LuaValue result = chunk.call();

            LuaValue layers = result.get("layers");
            if (!layers.istable()) {
                throw new RuntimeException("The ‘layers’ table was not found in the Lua file. Your Lua file may be corrupted.");
            }

            LuaValue layer1 = layers.get(1);
            if (!layer1.istable()) {
                throw new RuntimeException("The first layer was not found in the Lua file. Your Lua file may be corrupted.");
            }

            LuaValue data = layer1.get("data");
            if (!data.istable()) {
                throw new RuntimeException("The ‘data’ array was not found in the Lua file. Your Lua file may be corrupted.");
            }

            int size = data.length();
            int[] mapData = new int[size];
            for (int i = 1; i <= size; i++) {
                mapData[i-1] = data.get(i).toint();
            }

            return mapData;
        } catch (Exception e) {
            return null;
        }
    }

    private static final int[] pre_map = loadMapFromLua(map_lua_file);

    private int codeToTerrain(int code){
        switch (code){
            case 50:
                return Terrain.WALL_DECO;
            case 74:
                return Terrain.STATUE_SP;
            case 5:
                return Terrain.EMPTY_SP;
            case 59:
                return Terrain.LOCKED_DOOR;
            case 49:
                return Terrain.WALL;
            case 25:
                return Terrain.CHASM;
            case 21:
                return Terrain.PEDESTAL;
            default:
                return EMPTY_DECO;
        }
    }

    private void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill(level, this, 0, WALL);

        for (int i = left + 1; i <= right-1; i++) {
            for (int j = top + 1; j <= bottom-1; j++) {
                int dx = i - (left + 1);
                int dy = j - (top + 1);
                int index = dy * (minWidth()-2) + dx;
                if (index >= 0) {
                    if (index < pre_map.length) {
                        set(level, i, j, codeToTerrain(pre_map[index]));
                    }
                }
            }
        }

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
        }
    }
}
