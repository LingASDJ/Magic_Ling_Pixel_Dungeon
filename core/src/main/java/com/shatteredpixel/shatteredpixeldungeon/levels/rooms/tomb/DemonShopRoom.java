 /*
* Pixel Dungeon
* Copyright (C) 2012-2015 Oleg Dolya
*
* Shattered Pixel Dungeon
* Copyright (C) 2014-2024 Evan Debenham
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.tomb;
import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.PaswordBadges;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.tomb.Gore;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Torch;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LamellarArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.MailArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfNoWater;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.EasterEgg;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.SakaFishSketon;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfGolems;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LockSword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.hollow.CustomLuaRoom;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
public class DemonShopRoom extends CustomLuaRoom.FullLuaCustomRoom {
    protected ArrayList<Item> itemsToSpawn;
    {
        width = 15;
        height = 15;
        map_lua_file = Assets.Map_Luas.Tomb_DemonRoom_MapLua;
    }
    @Override
    public void paint(Level level) {
        super.paint(level);
        // 初始化并生成商品列表，限制最大40个商品
        if (itemsToSpawn == null){
            itemsToSpawn = generateItems();
        }
        // 强制限制商品总数不超过40
        while (itemsToSpawn.size() > 40) {
            itemsToSpawn.remove(itemsToSpawn.size() - 1);
        }
        // 放置恶魔商店NPC
        placeDemonShopkeeper(level);
        // 在基座及基座附近摆放所有商品
        placePedestalItems(level);
        // 设置房间门为普通门
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }
    }
    // 放置恶魔商店NPC
    protected void placeDemonShopkeeper( Level level ) {
        int shopPos = (top + 7) * level.width() + left +7;
        Mob n = new Gore();
        n.pos = shopPos;
        level.mobs.add(n);
    }
    // 核心：在TERRAIN.PEDESTAL基座上摆放商品，多余物品在基座附近空地摆放
    protected void placePedestalItems( Level level ){
        if (itemsToSpawn == null || itemsToSpawn.isEmpty()) return;
        // 1. 收集房间内所有基座地形坐标
        List<Point> pedestalPoints = getPoints().stream()
                .filter(p -> level.map[level.pointToCell(p)] == Terrain.PEDESTAL)
                .collect(Collectors.toList());
        // 2. 优先所有基座摆放商品
        for (Point pedPoint : pedestalPoints) {
            if (itemsToSpawn.isEmpty()) break;
            int cell = level.pointToCell(pedPoint);
            Item item = itemsToSpawn.remove(0);
            level.drop( item, cell ).type = Heap.Type.FOR_SALE;
        }
        // 3. 剩余商品在基座相邻空地摆放（无怪物、无物品、可通行）
        if (!itemsToSpawn.isEmpty()){
            List<Point> nearbyEmptyPoints = new ArrayList<>();
            // 遍历所有基座，收集周边8格有效空地
            for (Point pedPoint : pedestalPoints) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) continue;
                        Point checkPoint = new Point(pedPoint.x + dx, pedPoint.y + dy);
                        // 判定坐标在房间内、为空地形、无物品、无怪物
                        if (inside(checkPoint)) {
                            int cell = level.pointToCell(checkPoint);
                            if ((level.map[cell] == Terrain.EMPTY || level.map[cell] == Terrain.EMPTY_SP)
                                    && level.heaps.get(cell) == null && level.findMob(cell) == null){
                                nearbyEmptyPoints.add(checkPoint);
                            }
                        }
                    }
                }
            }
            // 去重并摆放剩余物品
            List<Point> validPoints = nearbyEmptyPoints.stream().distinct().collect(Collectors.toList());
            for (Point emptyPoint : validPoints) {
                if (itemsToSpawn.isEmpty()) break;
                int cell = level.pointToCell(emptyPoint);
                Item item = itemsToSpawn.remove(0);
                level.drop( item, cell ).type = Heap.Type.FOR_SALE;
            }
        }
        // 兜底：极端情况剩余物品，在房间内安全位置摆放
        if (!itemsToSpawn.isEmpty()){
            for (Point p : getPoints()){
                int cell = level.pointToCell(p);
                if ((level.map[cell] == Terrain.EMPTY_SP || level.map[cell] == Terrain.EMPTY || level.map[cell] == Terrain.PEDESTAL)
                        && level.heaps.get(cell) == null && level.findMob(cell) == null){
                    level.drop( itemsToSpawn.remove(0), cell ).type = Heap.Type.FOR_SALE;
                }
                if (itemsToSpawn.isEmpty()) break;
            }
        }
        // 异常捕获：防止物品摆放失败
        if (!itemsToSpawn.isEmpty()){
            ShatteredPixelDungeon.reportException(new RuntimeException("DemonShop failed to place all items!"));
        }
    }
    // 复刻原版商店商品生成逻辑
    protected static ArrayList<Item> generateItems() {
        ArrayList<Item> itemsToSpawn = new ArrayList<>();
        MeleeWeapon w;
        LockSword w2 = new LockSword();
        switch (Dungeon.depth) {
            case 5:case 6: default:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[1]);
                itemsToSpawn.add( Generator.random(Generator.misTiers[1]).quantity(2).identify(false) );
                itemsToSpawn.add( new LeatherArmor().identify(false) );
                if(!Badges.isUnlocked(Badges.Badge.ANCITY_THREE)){
                    if(Random.Int(1)<1){
                        //50%
                        w2.lvl = Random.Int(0, 301);
                        itemsToSpawn.add(w2.identify(false));
                    }
                }else if(Random.Int(10)<1 ) {
                    //10%
                    w2.lvl = Random.Int(0, 301);
                    itemsToSpawn.add(w2.identify(false));
                }
                break;
            case 11:case 12:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[2]);
                itemsToSpawn.add( Generator.random(Generator.misTiers[2]).quantity(2).identify(false) );
                itemsToSpawn.add( new MailArmor().identify(false) );
                if(!Badges.isUnlocked(Badges.Badge.ANCITY_THREE)){
                    if(Random.Int(1)<1){
                        //50%
                        w2.lvl = Random.Int(100, 301);
                        itemsToSpawn.add(w2.identify(false));
                    }
                }else if(Random.Int(200)<15) {
                    //7.5%
                    w2 = new LockSword();
                    w2.lvl = Random.Int(100, 301);
                    itemsToSpawn.add(w2.identify(false));
                }
                break;
            case 16:case 19:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[3]);
                itemsToSpawn.add( Generator.random(Generator.misTiers[3]).quantity(2).identify(false) );
                if(Random.Int(10) == 0){
                    itemsToSpawn.add( new LeatherArmor().identify(false) );
                } else {
                    itemsToSpawn.add( new ScaleArmor().identify(false) );
                }
                if(Badges.isUnlocked(Badges.Badge.ANCITY_THREE)){
                    w2.lvl = Random.Int(300, 501);
                    itemsToSpawn.add(w2.identify(false));
                }else {
                    w2 = new LockSword();
                    w2.lvl = Random.Int(200, 501);
                    itemsToSpawn.add( w2.identify(false) );
                }
                break;
            case 20: case 21:case 25:
                w = (MeleeWeapon) Generator.random(Generator.wepTiers[4]);
                itemsToSpawn.add( Generator.random(Generator.misTiers[4]).quantity(2).identify(false) );
                if(Random.Int(10) == 0){
                    itemsToSpawn.add( new LamellarArmor().identify(false) );
                } else {
                    itemsToSpawn.add( new PlateArmor().identify(false) );
                }
                itemsToSpawn.add( new Torch() );
                itemsToSpawn.add( new Torch() );
                itemsToSpawn.add( new Torch() );
                break;
        }
        w.enchant(null);
        w.cursed = false;
        w.level(0);
        w.identify(false);
        itemsToSpawn.add(w);
        itemsToSpawn.add( TippedDart.randomTipped(2) );
        if(!Statistics.bossRushMode){
            itemsToSpawn.add( new Alchemize().quantity(Random.IntRange(2, 3)));
        }
        if(Statistics.bossRushMode || Statistics.RandMode){
            if(Random.Int(10)<=3){
                itemsToSpawn.add( new ScrollOfGolems());
            }
        }
        if(Dungeon.isChallenged(Challenges.AQUAPHOBIA)){
            itemsToSpawn.add(new PotionOfNoWater());
            itemsToSpawn.add(new PotionOfNoWater());
        }
        if(RegularLevel.holiday == RegularLevel.WestHoliday.EASTER){
            itemsToSpawn.add(new EasterEgg());
        }
        Bag bag = ChooseBag(Dungeon.hero.belongings);
        if (bag != null) {
            itemsToSpawn.add(bag);
        }
        itemsToSpawn.add( new PotionOfHealing() );
        itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.POTION ) );
        itemsToSpawn.add( Generator.randomUsingDefaults( Generator.Category.POTION ) );
        if(Dungeon.isChallenged(DHXD)){
            itemsToSpawn.add(new OilPotion());
        } else if(lanterfireactive) {
            if(Challenges.activeChallenges() > 6){
                itemsToSpawn.add(new OilPotion());
                itemsToSpawn.add(new OilPotion());
            } else {
                itemsToSpawn.add(new OilPotion());
            }
        }
        //小恶魔奖励
        if(Statistics.dwarfKill){
            itemsToSpawn.add( new CurseInfusion() );
            Item brew;
            switch (Random.Int(6)){
                default:
                case 1: brew = new WaterSoul();   break;
                case 2: brew = new BlizzardBrew(); break;
                case 3: brew = new CausticBrew();    break;
                case 4: brew = new InfernalBrew();   break;
                case 5: brew = new ShockingBrew();   break;
            }
            itemsToSpawn.add( brew );
            Item w21;
            switch (Random.Int(5)){
                default:
                case 0: w21 = new ScrollOfSirensSong(); break;
                case 1: w21 = new ScrollOfChallenge(); break;
                case 2: w21 = new ScrollOfMetamorphosis(); break;
                case 3: w21 = new ScrollOfAntiMagic();    break;
                case 4: w21 = new ScrollOfPsionicBlast();   break;
            }
            itemsToSpawn.add( w21 );
            Ankh ankhPlus = new Ankh();
            ankhPlus.blessed = true;
            itemsToSpawn.add( ankhPlus );
        }
        itemsToSpawn.add( new ScrollOfIdentify() );
        itemsToSpawn.add( new ScrollOfRemoveCurse() );
        itemsToSpawn.add( new ScrollOfMagicMapping() );
        for (int i=0; i < 2; i++)
            itemsToSpawn.add( Random.Int(2) == 0 ?
                    Generator.randomUsingDefaults( Generator.Category.POTION ) :
                    Generator.randomUsingDefaults( Generator.Category.SCROLL ) );
        itemsToSpawn.add( new SmallRation() );
        itemsToSpawn.add( new SmallRation() );
        PaswordBadges.loadGlobal();
        List<PaswordBadges.Badge> passwordbadges = PaswordBadges.filtered( true );
        if(passwordbadges.contains(PaswordBadges.Badge.RESET_DAY)) {
            if (Random.Int(4) == 0) {
                itemsToSpawn.add(new SakaFishSketon());
            }
        }
        switch (Random.Int(4)){
            case 0:
                itemsToSpawn.add( new Bomb() );
                break;
            case 1:
            case 2:
                itemsToSpawn.add( new Bomb.DoubleBomb() );
                break;
            case 3:
                itemsToSpawn.add( new Honeypot() );
                break;
        }
        if(!Statistics.bossRushMode){
            itemsToSpawn.add( new Ankh() );
        }
        itemsToSpawn.add( new StoneOfAugmentation() );
        TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
        if (hourglass != null && hourglass.isIdentified() && !hourglass.cursed){
            int bags = 0;
            switch (Dungeon.depth) {
                case 6:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
                case 11:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
                case 16:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
                case 20: case 21:
                    bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
            }
            for(int i = 1; i <= bags; i++){
                itemsToSpawn.add( new TimekeepersHourglass.sandBag());
                hourglass.sandBags ++;
            }
        }
        Item rare;
        switch (Random.Int(10)){
            case 0:
                rare = Generator.random( Generator.Category.WAND );
                rare.level( 0 );
                break;
            case 1:
                rare = Generator.random(Generator.Category.RING);
                rare.level( 0 );
                break;
            case 2:
                rare = Generator.random( Generator.Category.ARTIFACT );
                break;
            default:
                rare = new Stylus();
        }
        rare.cursed = false;
        rare.cursedKnown = true;
        itemsToSpawn.add( rare );
        // 统一打乱商品顺序
        Random.pushGenerator(Random.Long());
        Random.shuffle(itemsToSpawn);
        Random.popGenerator();
        return itemsToSpawn;
    }
    // 复刻原版背包选择逻辑
    protected static Bag ChooseBag(Belongings pack){
        if(Dungeon.isDLC(Conducts.Conduct.DEV)) return null;
        HashMap<Bag, Integer> bags = new HashMap<>();
        if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) bags.put(new VelvetPouch(), 1);
        if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) bags.put(new ScrollHolder(), 0);
        if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) bags.put(new PotionBandolier(), 0);
        if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) bags.put(new MagicalHolster(), 0);
        if (bags.isEmpty()) return null;
        for (Item item : pack.backpack.items) {
            for (Bag bag : bags.keySet()){
                if (bag.canHold(item)){
                    bags.put(bag, bags.get(bag)+1);
                }
            }
        }
        Bag bestBag = null;
        for (Bag bag : bags.keySet()){
            if (bestBag == null){
                bestBag = bag;
            } else if (bags.get(bag) > bags.get(bestBag)){
                bestBag = bag;
            }
        }
        if (bestBag instanceof VelvetPouch){
            Dungeon.LimitedDrops.VELVET_POUCH.drop();
        } else if (bestBag instanceof ScrollHolder){
            Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
        } else if (bestBag instanceof PotionBandolier){
            Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
        } else if (bestBag instanceof MagicalHolster){
            Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
        }
        return bestBag;
    }
    // 保留原有门连接规则
    @Override
    public boolean canConnect(Point p) {
        int midX = left + 7;
        int midY = top + 7;
        if (p.x == midX && p.y == top) return true;
        if (p.x == midX && p.y == bottom) return true;
        if (p.x == left && p.y == midY) return true;
        if (p.x == right && p.y == midY) return true;
        return false;
    }
    @Override
    public int maxConnections(int direction) {
        return 1;
    }
}