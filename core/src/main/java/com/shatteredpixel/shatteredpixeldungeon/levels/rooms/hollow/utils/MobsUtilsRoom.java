package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameC02;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MobsUtilsRoom extends CustomLuaRoom {

    private ArrayList<Integer> perimeterCells;

    {
        width = 11;
        height = 11;
        map_lua_file = Assets.Map_Luas.LockedOneRoom_MapLua;
    }

    @Override
    public void paint(Level level) {
        super.paint(level);
        level.addItemToSpawn(new GoldenKey(Dungeon.depth));

        Point center = new Point((left + right) / 2, (top + bottom) / 2);
        int c = (top + 3) * level.width() + left + 5;
        level.drop(HighChestRules(), c).type = Heap.Type.GREEN_CHSET;

        perimeterCells = new ArrayList<>();
        for (int x = left + 1; x < right - 1; x++) {
            perimeterCells.add(level.pointToCell(new Point(x, top + 1)));
            perimeterCells.add(level.pointToCell(new Point(x, bottom - 1)));
        }
        for (int y = top + 2; y < bottom - 1; y++) {
            perimeterCells.add(level.pointToCell(new Point(left + 1, y)));
            perimeterCells.add(level.pointToCell(new Point(right - 1, y)));
        }

        Mob guard;
        switch (Random.Int(3)) {
            case 1:
                guard = new B_Guard();
                ((B_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
            case 2:
                guard = new C_Guard();
                ((C_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
            default:
                guard = new A_Guard();
                ((A_Guard) guard).setPatrolPath(new ArrayList<>(perimeterCells));
                guard.pos = perimeterCells.get(Random.index(perimeterCells));
                level.mobs.add(guard);
                break;
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

        midValueItems.add(new AllSearchIQuest.CrystalHeartChoco());
        midValueItems.add(new AllSearchIQuest.CreateWorldHeartModel());
        midValueItems.add(new AllSearchIQuest.GhostBlueModel());
        midValueItems.add(new AllSearchIQuest.GreenDamModel());
        midValueItems.add(new AllSearchIQuest.GreenStingModel());

        lowValueItems.add(new AllSearchIQuest.THEATER_CARDS());
        lowValueItems.add(new AllSearchIQuest.HOLLOW_SUGARS());
        lowValueItems.add(new AllSearchIQuest.GREEN_PRISM());
        lowValueItems.add(new AllSearchIQuest.GNOLL_WOOD());
        lowValueItems.add(new AllSearchIQuest.FOUR_KIDS());

        float randomValue = Random.Float();

        if (randomValue > 0.75f) {
            Random.shuffle(highValueItems);
            item = highValueItems.get(Random.index(highValueItems));
        } else if (randomValue > 0.40f) {
            Random.shuffle(midValueItems);
            item = midValueItems.get(Random.index(midValueItems));
        } else {
            Random.shuffle(lowValueItems);
            item = lowValueItems.get(Random.index(lowValueItems));
        }

        return item;
    }

    public static class A_Guard extends TribemanOld {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public A_Guard() {
            super();
            properties.add(Property.SEARCH);
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

        private final String GUARD_PATROL_INDEX = "guard_patrol_index";
        private final String GUARD_PATROL_PATH = "guard_patrol_path";

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

    public static class B_Guard extends Acidic {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public B_Guard() {
            super();
            HP = HT = 180;
            properties.add(Property.SEARCH);
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

        private final String GUARD_PATROL_INDEX = "guard_patrol_index";
        private final String GUARD_PATROL_PATH = "guard_patrol_path";

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

    public static class C_Guard extends BruteBot {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public C_Guard() {
            super();
            HP = HT = 140;
            defenseSkill = 18;
            properties.add(Property.SEARCH);
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

        private final String GUARD_PATROL_INDEX = "guard_patrol_index";
        private final String GUARD_PATROL_PATH = "guard_patrol_path";

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

    public static class GreenSlingSP extends GreenSlting {

        {
            HP = HT = 90;
            defenseSkill = 12;
        }

        @Override
        public int attackSkill( Char target ) {
            return 34;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(4, 8);
        }

    }

    public static class FlameC02SP extends FlameC02 {
        @Override
        public int attackSkill( Char target ) {
            return 45;
        }
    }

    public static class DM275RPG_SP extends DM275 {
        {
            HP = HT = 190;
            defenseSkill = 19;
            properties.remove(Property.LARGE);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 21, 30 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 50;
        }

    }

    public static class DM275X extends OldDM300 {
        {
            HP = HT = 160;
            properties.remove(Property.LARGE);
        }

        @Override
        public void notice() {

        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 21, 30 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 50;
        }

    }

}
