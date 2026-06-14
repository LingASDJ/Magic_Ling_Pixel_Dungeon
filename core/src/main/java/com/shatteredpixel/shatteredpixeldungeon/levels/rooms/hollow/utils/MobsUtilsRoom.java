package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.utils;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.depth;
import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.level;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.HalomethaneFire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.HalomethaneBurning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ShieldBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Acidic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.BruteBot;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.FlameC02;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.OldDM300;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.SRPDHBLR;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ShieldHuntsman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Spinner;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.gold.TribemanOld;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.DM275;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.spical.GreenSlting;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.hollow.AllSearchIQuest;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedGolemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedShieldHuntsmanSpite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSkullShamanSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedSpinnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RedTorchManSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Red_SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
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

    private static Item HighChestRules() {
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


        @Override
        public void die( Object cause ) {
            super.die(cause);
            level.drop(new GoldenKey(depth), pos);
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

        @Override
        public void die( Object cause ) {
            super.die(cause);
            level.drop(new GoldenKey(depth), pos);
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

        @Override
        public void die( Object cause ) {
            super.die(cause);
            level.drop(new GoldenKey(depth), pos);
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

    private static Item GetRedRules() {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Float> weights = new ArrayList<>();
        items.add(new AllSearchIQuest.CrystalHeartChoco());
        weights.add(7.0f);

        items.add(new AllSearchIQuest.CreateWorldHeartModel());
        weights.add(8.0f);

        items.add(new AllSearchIQuest.GhostBlueModel());
        weights.add(9.0f);

        items.add(new AllSearchIQuest.GreenDamModel());
        weights.add(11.0f);

        items.add(new AllSearchIQuest.GreenStingModel());
        weights.add(13.0f);

        items.add(new AllSearchIQuest.THEATER_CARDS());
        weights.add(15.0f);

        items.add(new AllSearchIQuest.HOLLOW_SUGARS());
        weights.add(17.0f);

        items.add(new AllSearchIQuest.GREEN_PRISM());
        weights.add(19.0f);

        items.add(new AllSearchIQuest.GNOLL_WOOD());
        weights.add(21.0f);

        items.add(new AllSearchIQuest.FOUR_KIDS());
        weights.add(23.0f);

        float totalWeight = 0f;
        for (float weight : weights) {
            totalWeight += weight;
        }

        float randomValue = Random.Float() * totalWeight;

        float currentWeight = 0f;
        for (int i = 0; i < items.size(); i++) {
            currentWeight += weights.get(i);
            if (randomValue <= currentWeight) {
                return items.get(i);
            }
        }

        Random.shuffle(items);

        return items.get(items.size() - 1);
    }


    //血红系列
    public static class RedGolem extends Golem {

        public RedGolem() {
            spriteClass = RedGolemSprite.class;
            int Int = Random.Int(65,90);
            HT = Int;
            HP = Int;
            defenseSkill = 15;
            EXP = 3;
            maxLvl = 45;
            properties.remove(Property.LARGE);
            properties.add(Property.SEARCH);
            properties.add(Property.INORGANIC);
            immunities.add(Paralysis.class);
        }

        @Override
        public void onZapComplete(){
            zap();
            next();
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange(0, 5);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange(10, 38);
        }

        @Override
        public int attackSkill(Char target) {
            return 40;
        }

        private void zap() {
            spend(1f);
            if (hit(this, this.enemy, true)) {
                if (this.enemy == Dungeon.hero && Random.Int(2) == 0) {
                    Buff.affect(Dungeon.hero, Burning.class).reignite(Dungeon.hero, 4.0f);
                    Buff.affect(Dungeon.hero, Poison.class).set(Random.Int(5, 7));
                    Sample.INSTANCE.play("sounds/debuff.mp3");
                }
                int dmg = Random.NormalIntRange(20, 30);
                this.enemy.damage(dmg, new DM100.LightningBolt());
                if (this.enemy == Dungeon.hero && !this.enemy.isAlive()) {
                    Dungeon.fail(getClass());
                    GLog.n(Messages.get(this, "frost_kill", new Object[0]), new Object[0]);
                    return;
                }
                return;
            }
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(GetRedRules(), pos);
            droppedGold.type = Heap.Type.BLACK;
            droppedGold.sprite.view( droppedGold );
        }

    }

    public static class RedSnake extends Snake implements Callback {
        private int cooldown = 0;
        {
            spriteClass = Red_SnakeSprite.class;
            HP = HT = 165;
            defenseSkill = 0;
            maxLvl = 45;
            properties.add(Property.SEARCH);
        }

        @Override
        public boolean act() {

            if(cooldown>0){
                cooldown--;
            }

            return super.act();
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 15, 32 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 30;
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }

        protected boolean doAttack( Char enemy ) {

            if (Dungeon.level.adjacent( pos, enemy.pos )) {

                return super.doAttack( enemy );

            } else {

                if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                    sprite.zap( enemy.pos );
                    return false;
                } else {
                    zap();
                    return true;
                }
            }
        }

        private static final float TIME_TO_ZAP	= 3f;

        private void zap() {
            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {

                if (enemy == Dungeon.hero && Random.Int( 2 ) == 0 && cooldown == 0) {
                    GameScene.add( Blob.seed( enemy.pos, 80, ParalyticGas.class ) );
                    Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                    cooldown = 45;
                }

                int dmg = Random.NormalIntRange( 4, 12 );
                enemy.damage( dmg, new DM100.LightningBolt());
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }
        }

        public void onZapComplete() {
            zap();
            next();
        }

        @Override
        public void call() {
            next();
        }

        private static final String COOLDOWN = "cooldown";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(COOLDOWN, cooldown);
        }


        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            cooldown = bundle.getInt(COOLDOWN);
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(GetRedRules(), pos);
            droppedGold.type = Heap.Type.BLACK;
            droppedGold.sprite.view( droppedGold );
        }

    }

    public static class RedTorchHuntsman extends SRPDHBLR {

        {
            spriteClass = RedTorchManSprite.class;
            HT = 175;
            HP = 175;
            defenseSkill = 16;
            EXP = 15;
            maxLvl = 45;
            properties.add(Property.SEARCH);
        }

        public int attackSkill(Char target) {
            return 42;
        }

        public int damageRoll() {
            return Random.NormalIntRange(30, 50);
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            int dmg = super.attackProc(enemy, damage);
            if (Dungeon.level.flamable[enemy.pos]) {
                GameScene.add(Blob.seed(enemy.pos, 15, HalomethaneFire.class));
            }
            if (enemy.buff(HalomethaneBurning.class) == null) {
                Buff.affect(enemy, HalomethaneBurning.class).reignite(enemy);
            }
            return dmg;
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(GetRedRules(), pos);
            droppedGold.type = Heap.Type.BLACK;
            droppedGold.sprite.view( droppedGold );
        }
    }

    public static class RedMagicShieldMan extends ShieldHuntsman {

        protected boolean hasRaged = false;

        {
            spriteClass = RedShieldHuntsmanSpite.class;
            HT = HP = 200;
            defenseSkill = 15;
            EXP = 19;
            state = WANDERING;
            baseSpeed = 1.5f;
            maxLvl = 45;
            properties.add(Property.SEARCH);
        }

        @Override
        public int attackProc(Char enemy, int damage){
            if(hasRaged && damage>=enemy.HP){
                Buff.affect(this, DeadSora.class).incShield(5);
            }
            return super.attackProc(enemy, damage);
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 20, 55 );
        }

        @Override
        public int attackSkill( Char target ) {
            return 60;
        }

        @Override
        public float attackDelay() {
            return hasRaged ? 0.25f : 0.5f;
        }

        @Override
        protected Char chooseEnemy() {
            for (Mob mob : Dungeon.level.mobs) {
                if (!(mob == this) && mob.alignment != Alignment.NEUTRAL && !mob.isInvulnerable(getClass()) && fieldOfView[mob.pos]) {
                    return mob;
                }
            }
            return super.chooseEnemy();
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(HighChestRules(), pos);
            droppedGold.type = Heap.Type.GREEN_CHSET;
            droppedGold.sprite.view( droppedGold );
        }

        @Override
        public synchronized boolean isAlive() {
            if (super.isAlive()){
                return true;
            } else {
                if (!hasRaged && HP <= (HT*0.75f)){
                    triggerEnrage();
                }
                return !buffs(DeadSora.class).isEmpty();
            }
        }

        protected void triggerEnrage(){
            Buff.affect(this, DeadSora.class).setShield(HT/2 + 40);
            spend( TICK );
            hasRaged = true;
        }

        public static class DeadSora extends ShieldBuff {

            {
                type = buffType.POSITIVE;
            }

            @Override
            public boolean act() {

                if (target.HP > 0){
                    detach();
                    return true;
                }

                absorbDamage( 2);

                if (shielding() <= 0){
                    target.die(null);
                }

                spend( TICK );

                return true;
            }

            @Override
            public int icon () {
                return BuffIndicator.FURY;
            }

            @Override
            public String toString () {
                return Messages.get(this, "name");
            }

            @Override
            public String desc () {
                return Messages.get(this, "desc", shielding());
            }

            {
                immunities.add(Terror.class);
            }
        }

        private static final String HAS_RAGED = "has_raged";
        private static String FOCUS_COOLDOWN = "focus_cooldown";
        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(HAS_RAGED, hasRaged);
            bundle.put( FOCUS_COOLDOWN, focusCooldown );
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            hasRaged = bundle.getBoolean(HAS_RAGED);
            focusCooldown = bundle.getInt( FOCUS_COOLDOWN );
        }

    }

    public static class RedSpider extends Spinner {

        {
            spriteClass = RedSpinnerSprite.class;

            HP = HT = 195;
            defenseSkill = 16;

            EXP = 7;
            maxLvl = 45;
            properties.add(Property.SEARCH);
        }

        @Override
        protected void applyWebToCell(int cell) {
            GameScene.add(Blob.seed(cell, 10, Electricity.class));
        }

        @Override
        public void damage(int dmg, Object src, DamageType type) {
            int grassCells = 0;
            for (int i : PathFinder.NEIGHBOURS9) {
                if (Dungeon.level.map[pos+i] == Terrain.FURROWED_GRASS
                        || Dungeon.level.map[pos+i] == Terrain.HIGH_GRASS){
                    grassCells++;
                }
            }
            //first adjacent grass cell reduces damage taken by 30%, each one after reduces by another 10%
            if (grassCells > 0) dmg = Math.round(dmg * (8-grassCells)/10f);

            super.damage(dmg, src, type);
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            return damage; //does not apply poison
        }

        {
            immunities.add(Electricity.class);
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(GetRedRules(), pos);
            droppedGold.type = Heap.Type.BLACK;
            droppedGold.sprite.view( droppedGold );
        }

    }

    public static class RedShaman extends Mob implements Callback {

        public RedShaman() {
            spriteClass = RedSkullShamanSprite.class;
            HP = HT = 185;
            defenseSkill = 18;
            EXP = 6;
            maxLvl = 45;

            properties.add(Property.ELECTRIC);
            properties.add(Property.SEARCH);
        }

        public int attackProc(Char target, int damage) {
            damage = super.attackProc(target, damage);

            int effectRoll = Random.Int(4) + 5;
            if (effectRoll > 2) {
                if (effectRoll >= 6 && target.buff(Burning.class) == null) {
                    if (Dungeon.level.flamable[target.pos]) {
                        GameScene.add(Blob.seed(target.pos, 12, HalomethaneFire.class));
                    }
                    Buff.affect(target, Burning.class).reignite(target);
                } else {
                    Buff.affect(target, Poison.class).set((float) (effectRoll - 2));
                }
            }

            return damage;
        }

        public int attackSkill(Char target) {
            return 42;
        }

        protected boolean canAttack(Char target) {
            return (new Ballistica(this.pos, target.pos, Ballistica.PROJECTILE)).collisionPos == target.pos;
        }

        public int damageRoll() {
            return Random.NormalIntRange(18, 54);
        }

        protected boolean doAttack(Char target) {
            int distance = Dungeon.level.distance(this.pos, target.pos);
            boolean visible = true;

            if (distance <= 1) {
                return super.doAttack(target);
            } else {
                boolean inSight = this.fieldOfView[this.pos] || this.fieldOfView[target.pos];

                if (inSight) {
                    this.sprite.zap(target.pos);
                }

                this.spend(1.0F);
                if (hit(this, target, true)) {
                    int baseDamage = Random.NormalIntRange(13, 18);
                    int finalDamage = baseDamage;

                    if (Dungeon.level.water[target.pos] && !target.flying) {
                        finalDamage = (int) ((float) baseDamage * 1.5F);
                    }

                    target.damage(finalDamage, this);
                    target.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                    target.sprite.flash();

                    if (target == Dungeon.hero) {
                        Camera.main.shake(2.0F, 0.3F);
                        if (!target.isAlive()) {
                            Dungeon.fail(this.getClass());
                            GLog.n(Messages.get(this, "zap_kill"));
                        }
                    }
                } else {
                    target.sprite.showStatus(16776960, target.defenseVerb());
                }

                if (inSight) {
                    visible = false;
                }

                return visible;
            }
        }

        public int drRoll() {
            return Random.NormalIntRange(2, 7);
        }

        @Override
        public void call() {
            next();
        }

        @Override
        public void die( Object cause ) {
            super.die(cause);
            Heap droppedGold = Dungeon.level.drop(GetRedRules(), pos);
            droppedGold.type = Heap.Type.BLACK;
            droppedGold.sprite.view( droppedGold );
        }

    }


    public static class Red_A extends MobsUtilsRoom.RedGolem {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
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

    public static class Red_B extends MobsUtilsRoom.RedSnake {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
        }

        @Override
        public boolean act() {
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

    public static class Red_C extends MobsUtilsRoom.RedTorchHuntsman {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
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

    public static class Red_D extends MobsUtilsRoom.RedMagicShieldMan {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
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

    public static class Red_E extends MobsUtilsRoom.RedSpider {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
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

    public static class Red_F extends MobsUtilsRoom.RedShaman {
        private int patrolIndex = 0;
        private ArrayList<Integer> patrolPath;

        public void setPatrolPath(ArrayList<Integer> path) {
            this.patrolPath = path;
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

}
