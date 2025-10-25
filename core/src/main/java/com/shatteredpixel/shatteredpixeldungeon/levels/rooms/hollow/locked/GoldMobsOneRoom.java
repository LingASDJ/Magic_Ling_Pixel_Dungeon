package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.locked;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOld;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MeatPie;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Switch;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfBlink;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GoldMobsOneRoom extends CustomLuaRoom {

    private ArrayList<Integer> perimeterCells;
    private boolean guardSpawned = false;
    private static Guard existingGuard = null;
    private boolean guardExists = false;
    private static final String GUARD_PATROL_INDEX = "guard_patrol_index";
    private static final String GUARD_PATROL_PATH = "guard_patrol_path";

    {
        width = 11;
        height = 11;
        map_lua_file = Assets.Map_Luas.LockedOneRoom_MapLua;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);

        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int c = (top + 3) * level.width() + left + 5;
        level.drop(HighChestRules(),c);

        if (!guardSpawned) {
            perimeterCells = new ArrayList<>();
            for (int x = left + 1; x < right - 1; x++) {
                perimeterCells.add(level.pointToCell(new Point(x, top + 1)));
                perimeterCells.add(level.pointToCell(new Point(x, bottom - 1)));
            }
            for (int y = top + 2; y < bottom - 1; y++) {
                perimeterCells.add(level.pointToCell(new Point(left + 1, y)));
                perimeterCells.add(level.pointToCell(new Point(right - 1, y)));
            }

            if (existingGuard != null) {
                guardExists = true;
            } else {
                for (Mob mob : level.mobs) {
                    if (mob instanceof Guard) {
                        existingGuard = (Guard) mob;
                        guardExists = true;
                        break;
                    }
                }
            }

            if (!guardExists) {
                try {
                    Guard guard = new Guard();
                    guard.setPatrolPath(new ArrayList<>(perimeterCells));
                    guard.pos = perimeterCells.get(Random.index(perimeterCells));
                    level.mobs.add(guard);
                    existingGuard = guard;
                    guardSpawned = true;
                } catch (Exception e) {
                    System.err.println("Failed to create guard: " + e.getMessage());
                }
            }
        }
    }

    private Item HighChestRules() {
        Item item;

        ArrayList<Item> highValueItems = new ArrayList<>();
        ArrayList<Item> midValueItems = new ArrayList<>();
        ArrayList<Item> lowValueItems = new ArrayList<>();

        highValueItems.add(new AllSearchIQuest.HollowLantern());
        highValueItems.add(new AllSearchIQuest.HollowCityProps());
        highValueItems.add(new AllSearchIQuest.HollowGoldCards());

        midValueItems.add(new Food());
        midValueItems.add(new MeatPie());
        midValueItems.add(new Switch());

        lowValueItems.add(new StoneOfAggression());
        lowValueItems.add(new StoneOfBlink());

        int randomValue = Random.Int(100);

        if (randomValue < 25) {
            item = highValueItems.get(Random.index(highValueItems));
        } else if (randomValue < 85) {
            item = midValueItems.get(Random.index(midValueItems));
        } else {
            item = lowValueItems.get(Random.index(lowValueItems));
        }

        return item;
    }

    public static class Guard extends TribemanOld {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public Guard() {
            super();
        }

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
        }

        public String info(){
            StringBuilder desc = new StringBuilder(description());
            if(isOldDay){
                return Messages.get(Mob.class, "desc_old");
            }
            for (Buff b : buffs(ChampionEnemy.class)){
                desc.append("\n\n_").append(Messages.titleCase(b.name())).append("_\n").append(b.desc());
            }
            return desc.toString();
        }

        public static boolean seenBefore = false;
        @Override
        protected boolean act() {
            if (patrolPath != null && !patrolPath.isEmpty()) {
                int to = patrolPath.get(patrolIndex);
                if (pos != to) {
                    if (canMove(to)) {
                        beckon(to);
                    } else {
                        for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                            int nextPos = pos + PathFinder.NEIGHBOURS8[i];
                            if (level.passable[nextPos] && !isWall(nextPos)) {
                                beckon(nextPos);
                                break;
                            }
                        }
                    }
                } else {
                    patrolIndex = (patrolIndex + 1) % patrolPath.size();
                }
            }
            return super.act();
        }

        private boolean canMove(int to) {
            return level.passable[to] && !isWall(to);
        }

        private boolean isWall(int pos) {
            return level.map[pos] == Terrain.WALL;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(GUARD_PATROL_INDEX, patrolIndex);
            if (patrolPath != null) {
                String[] pathStr = new String[patrolPath.size()];
                for (int i = 0; i < patrolPath.size(); i++) {
                    pathStr[i] = String.valueOf(patrolPath.get(i));
                }
                bundle.put(GUARD_PATROL_PATH, pathStr);
            }
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            patrolIndex = bundle.getInt(GUARD_PATROL_INDEX);
            if (bundle.contains(GUARD_PATROL_PATH)) {
                String[] pathStr = bundle.getStringArray(GUARD_PATROL_PATH);
                patrolPath = new ArrayList<>();
                for (String s : pathStr) {
                    try {
                        patrolPath.add(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing patrol path: " + e.getMessage());
                    }
                }
            }
        }
    }
}
