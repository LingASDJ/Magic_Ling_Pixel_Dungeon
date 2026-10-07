package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.EMPTY_DECO;
import static com.shatteredpixel.shatteredpixeldungeon.levels.Terrain.WALL;

import com.badlogic.gdx.Gdx;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Point;

import java.util.ArrayList;

public abstract class CustomLuaRoom extends ConnectionRoom {

    public int width = 0;
    public int height = 0;
    public String map_lua_file;
    public int[] pre_map;

    @Override
    public int minWidth() {
        return width;
    }
    @Override
    public int minHeight() {
        return height;
    }
    @Override
    public int maxWidth() {
        return width;
    }
    @Override
    public int maxHeight() {
        return height;
    }

    // 从Lua文件加载地图数据
    // These map files are exported by Tiled (v1.10 Lua format). Instead of running
    // them through the LuaJ interpreter (which is not available on web/TeaVM targets),
    // we parse the deterministic Tiled-Lua layout directly: the first layer's "data"
    // table, an array of integer tile GIDs.
    public int[] loadMapFromLua(String t) {
        try {
            String luaText;
            if (Gdx.files.internal(map_lua_file).exists()) {
                luaText = Gdx.files.internal(map_lua_file).readString("UTF-8");
            } else {
                throw new RuntimeException("The file map_room.lua cannot be found. Try the following path:: " +
                        "assets/" + t + " and " +
                        "/" + t);
            }

            return parseTiledLuaMap(luaText);
        } catch (Exception e) {
            return null;
        }
    }

    // Minimal parser for Tiled's Lua export format. It locates the first layer's
    // "data" table and reads its integer array. Fully deterministic, no interpreter.
    private static int[] parseTiledLuaMap(String lua) {
        if (lua == null) return null;

        // strip Lua comments (-- to end of line), honoring simple string literals
        StringBuilder clean = new StringBuilder(lua.length());
        boolean inString = false;
        for (int i = 0; i < lua.length(); i++) {
            char c = lua.charAt(i);
            if (inString) {
                clean.append(c);
                if (c == '"') inString = false;
                continue;
            }
            if (c == '"') {
                inString = true;
                clean.append(c);
                continue;
            }
            if (c == '-' && i + 1 < lua.length() && lua.charAt(i + 1) == '-') {
                while (i < lua.length() && lua.charAt(i) != '\n') i++;
                clean.append('\n');
                continue;
            }
            clean.append(c);
        }
        String text = clean.toString();

        // locate the layers table
        int layersIdx = text.indexOf("[\"layers\"]");
        if (layersIdx == -1) layersIdx = text.indexOf("layers");
        if (layersIdx == -1) return null;
        int layersBrace = text.indexOf('{', layersIdx);
        if (layersBrace == -1) return null;

        // the first layer's data table
        int dataIdx = text.indexOf("[\"data\"]", layersBrace);
        if (dataIdx == -1) dataIdx = text.indexOf("data", layersBrace);
        if (dataIdx == -1) return null;
        int dataBrace = text.indexOf('{', dataIdx);
        if (dataBrace == -1) return null;

        int end = dataBrace + 1;
        int depth = 1;
        while (end < text.length() && depth > 0) {
            char c = text.charAt(end);
            if (c == '{') depth++;
            else if (c == '}') depth--;
            end++;
        }
        if (depth != 0) return null;

        String body = text.substring(dataBrace + 1, end - 1);
        ArrayList<Integer> values = new ArrayList<>();
        int idx = 0;
        while (idx < body.length()) {
            char c = body.charAt(idx);
            if (c == ',' || Character.isWhitespace(c)) {
                idx++;
                continue;
            }
            int start = idx;
            while (idx < body.length() && body.charAt(idx) != ',' && !Character.isWhitespace(body.charAt(idx))) idx++;
            String token = body.substring(start, idx);
            if (!token.isEmpty()) {
                try {
                    if (token.endsWith(".0")) token = token.substring(0, token.length() - 2);
                    values.add((int) Double.parseDouble(token));
                } catch (NumberFormatException ignored) {}
            }
        }

        int[] result = new int[values.size()];
        for (int i = 0; i < values.size(); i++) result[i] = values.get(i);
        return result;
    }

    private static int codeToTerrain(int code){
        switch (code){
            case 0:
                return Terrain.WATER;
            case 1:
                return EMPTY;
            case 50:
                return Terrain.WALL_DECO;
            case 67:
                return Terrain.HIGH_GRASS;
            case 73:
                return Terrain.STATUE;
            case 74:
                return Terrain.STATUE_SP;
            case 5: case 11:
                return Terrain.EMPTY_SP;
            case 51:
                return Terrain.BOOKSHELF;
            case 57:
                return Terrain.DOOR;
            case 59:
                return Terrain.LOCKED_DOOR;
            case 72:
                return Terrain.GOLDEN_DOOR;
            case 84:
                return Terrain.CRYSTAL_DOOR;
            case 49:
                return Terrain.WALL;
            case 25:
                return Terrain.CHASM;
            case 20:
                return Terrain.EMPTY_WELL;
            case 21:
                return Terrain.PEDESTAL;
            default:
                return EMPTY_DECO;
        }
    }

    private static void set(Level level, int x, int y, int value) {
        level.map[x + y * level.width()] = value;
    }

    @Override
    public boolean canPlaceTrap(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        if (pre_map == null) {
            pre_map = loadMapFromLua(map_lua_file);
        }
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

    public static abstract class FullLuaCustomRoom extends CustomLuaRoom {
        @Override
        public void paint(Level level) {
            if (pre_map == null) {
                pre_map = loadMapFromLua(map_lua_file);
            }
            if (pre_map == null) return;

            int roomW = width;
            int roomH = height;
            for (int dx = 0; dx < roomW; dx++) {
                for (int dy = 0; dy < roomH; dy++) {
                    int roomX = left + dx;
                    int roomY = top + dy;
                    int idx = dy * roomW + dx;
                    if (idx < pre_map.length) {
                        int terrainId = codeToTerrain(pre_map[idx]);
                        CustomLuaRoom.set(level, roomX, roomY, terrainId);
                    }
                }
            }

            for (Door door : connected.values()) {
                door.set(  Door.Type.REGULAR );
            }

        }
    }

}
