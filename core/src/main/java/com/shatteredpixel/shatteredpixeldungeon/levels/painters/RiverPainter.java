package com.shatteredpixel.shatteredpixeldungeon.levels.painters;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Patch;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.EntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ExitRoom;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
public class RiverPainter extends RegularPainter {

    // 河流参数
    private float riverWidth = 16f;  // 增加河流宽度占比
    private float riverPathSmoothness = 3f;
    private float meanderStrength = 2.65f;  // 增加蜿蜒强度
    private int numBranches = 3;  // 添加支流数量
    private float branchProbability = 0.75f;  // 支流生成概率

    // 小径参数
    private float pathWidth = 0.5f;  // 增加小径宽度
    private float pathSmoothness = 56f;

    // 河岸植被参数
    private float vegetationFill = 0.65f;  // 增加植被填充率
    private float vegetationSmoothness = 3f;

    @Override
    public boolean paint(Level level, ArrayList<Room> rooms) {
        if (!super.paint(level, rooms)) {
            return false;
        }

        long seed = Dungeon.seed;

        // 生成主河道
        boolean[] riverPath = generateMainRiver(level);

        // 生成支流
        for (int i = 0; i < numBranches; i++) {
            if (Random.Float() < branchProbability) {
                boolean[] branchPath = generateBranch(level, riverPath);
                mergeRiverPaths(riverPath, branchPath);
            }
        }

        // 确保河流不穿过特殊房间
        for (Room r : rooms) {
            if (isSpecialRoom(r)) {
                clearRiverFromRoom(level, riverPath, r);
            }
        }

        // 绘制河流
        for (int i = 0; i < level.length(); i++) {
            if (riverPath[i] && level.map[i] == Terrain.EMPTY) {
                level.map[i] = Terrain.WATER;
            }
        }

        paintPaths(level, rooms);
        paintVegetation(level, rooms);

        Dungeon.seed = seed;
        return true;
    }

    private boolean[] generateMainRiver(Level level) {
        boolean[] riverPath = new boolean[level.length()];
        int width = level.width();
        int height = level.height();

        // 从左到右生成主河道
        int currentY = height / 2;
        int prevY = currentY;

        for (int x = 0; x < width; x++) {
            Random.Long(Dungeon.seed + x);

            if (Random.Float() < meanderStrength) {
                int direction = Random.Int(-1, 2);
                int deltaY = Math.min(3, Math.max(-3, direction));  // 增加变化幅度
                currentY += deltaY;
                currentY = Math.max(2, Math.min(height - 3, currentY));

                // 平滑过渡
                if (x > 0) {
                    for (int y = Math.min(prevY, currentY); y <= Math.max(prevY, currentY); y++) {
                        if (y >= 0 && y < height) {
                            int cell = x + y * width;
                            riverPath[cell] = true;
                            // 增加河流宽度
                            for (int n : PathFinder.NEIGHBOURS8) {
                                int neighbor = cell + n;
                                if (neighbor >= 0 && neighbor < riverPath.length) {
                                    riverPath[neighbor] = true;
                                }
                            }
                        }
                    }
                }
            }

            // 绘制更宽的河流主体
            for (int dy = -2; dy <= 2; dy++) {  // 增加河流宽度
                int y = currentY + dy;
                if (y >= 0 && y < height) {
                    int cell = x + y * width;
                    riverPath[cell] = true;
                }
            }

            prevY = currentY;
        }

        smoothRiverEdges(level, riverPath);
        return riverPath;
    }

    private boolean[] generateBranch(Level level, boolean[] mainRiver) {
        boolean[] branchPath = new boolean[level.length()];
        int width = level.width();
        int height = level.height();

        // 找到主河道上的随机点作为支流起点
        ArrayList<Integer> riverPoints = new ArrayList<>();
        for (int i = 0; i < mainRiver.length; i++) {
            if (mainRiver[i]) {
                riverPoints.add(i);
            }
        }

        if (riverPoints.isEmpty()) return branchPath;

        int startPoint = Random.element(riverPoints);
        int startX = startPoint % width;
        int startY = startPoint / width;

        // 随机选择支流方向
        boolean horizontal = Random.Float() < 0.5f;
        int direction = Random.Float() < 0.5f ? 1 : -1;

        // 生成支流
        int currentX = startX;
        int currentY = startY;
        int steps = Random.Int(5, 15);  // 支流长度

        for (int step = 0; step < steps; step++) {
            Random.Long(Dungeon.seed + step);

            if (Random.Float() < meanderStrength * 0.7f) {  // 支流蜿蜒程度稍低
                if (horizontal) {
                    currentX += direction;
                    currentY += Random.Int(-1, 2);
                } else {
                    currentY += direction;
                    currentX += Random.Int(-1, 2);
                }
            }

            // 确保在地图范围内
            currentX = Math.max(1, Math.min(width - 2, currentX));
            currentY = Math.max(1, Math.min(height - 2, currentY));

            // 绘制支流
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int x = currentX + dx;
                    int y = currentY + dy;
                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        int cell = x + y * width;
                        branchPath[cell] = true;
                    }
                }
            }
        }

        return branchPath;
    }

    private void mergeRiverPaths(boolean[] mainRiver, boolean[] branch) {
        for (int i = 0; i < mainRiver.length; i++) {
            if (branch[i]) {
                mainRiver[i] = true;
            }
        }
    }

    private void smoothRiverEdges(Level level, boolean[] riverPath) {
        boolean[] smoothed = new boolean[riverPath.length];

        // 多次平滑处理
        for (int iteration = 0; iteration < 2; iteration++) {
            for (int i = 0; i < riverPath.length; i++) {
                int count = 0;
                for (int n : PathFinder.NEIGHBOURS9) {
                    if (i + n >= 0 && i + n < riverPath.length && riverPath[i + n]) {
                        count++;
                    }
                }
                smoothed[i] = count >= 4;  // 降低阈值以保持更多细节
            }
            System.arraycopy(smoothed, 0, riverPath, 0, riverPath.length);
        }
    }

    private void paintPaths(Level level, ArrayList<Room> rooms) {
        boolean[] pathMap = Patch.generate(level.width(), level.height(),
                pathWidth, (int) pathSmoothness, true);

        for (int i = 0; i < level.length(); i++) {
            if (pathMap[i] && level.map[i] == Terrain.EMPTY) {
                if (isNearWater(level, i)) {
                    level.map[i] = Terrain.EMPTY_SP;
                }
            }
        }

        // 在河流附近添加更多小径
        for (int i = 0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WATER) {
                for (int n : PathFinder.NEIGHBOURS8) {
                    int neighbor = i + n;
                    if (neighbor >= 0 && neighbor < level.length() &&
                            level.map[neighbor] == Terrain.EMPTY) {
                        if (Random.Float() < 0.3f) {  // 增加小径生成概率
                            level.map[neighbor] = Terrain.EMPTY_SP;
                        }
                    }
                }
            }
        }
    }

    private void paintVegetation(Level level, ArrayList<Room> rooms) {
        boolean[] vegetation = Patch.generate(level.width(), level.height(),
                vegetationFill, (int) vegetationSmoothness, true);

        for (int i = 0; i < level.length(); i++) {
            if (vegetation[i] && level.map[i] == Terrain.EMPTY) {
                if (isNearWater(level, i)) {
                    level.map[i] = Random.Float() < 0.7f ?  // 增加高草概率
                            Terrain.WATER : Terrain.GRASS;
                }
            }
        }
    }

    private boolean isNearWater(Level level, int cell) {
        for (int n : PathFinder.NEIGHBOURS8) {
            int neighbor = cell + n;
            if (neighbor >= 0 && neighbor < level.length() &&
                    level.map[neighbor] == Terrain.WATER) {
                return true;
            }
        }
        return false;
    }

    private boolean isSpecialRoom(Room room) {
        return room instanceof EntranceRoom ||
                room instanceof ExitRoom ||
                room instanceof SpecialRoom;
    }

    private void clearRiverFromRoom(Level level, boolean[] riverPath, Room room) {
        for (Point p : room.getPoints()) {
            int cell = level.pointToCell(p);
            if (room.inside(p) && cell >= 0 && cell < riverPath.length) {
                riverPath[cell] = false;
                // 清理房间周围的河流
                for (int n : PathFinder.NEIGHBOURS8) {
                    int neighbor = cell + n;
                    if (neighbor >= 0 && neighbor < riverPath.length) {
                        Point np = level.cellToPoint(neighbor);
                        if (room.inside(np)) {
                            riverPath[neighbor] = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void decorate(Level level, ArrayList<Room> rooms) {
        // 添加河岸装饰
        for (int i = 0; i < level.length(); i++) {
            if (level.map[i] == Terrain.WATER) {
                for (int n : PathFinder.NEIGHBOURS8) {
                    int neighbor = i + n;
                    if (neighbor >= 0 && neighbor < level.length() &&
                            level.map[neighbor] == Terrain.EMPTY) {
                        if (Random.Float() < 0.15f) {  // 增加装饰概率
                            level.map[neighbor] = Random.Float() < 0.6f ?
                                    Terrain.WATER : Terrain.GRASS;
                        }
                    }
                }
            }
        }
    }
}