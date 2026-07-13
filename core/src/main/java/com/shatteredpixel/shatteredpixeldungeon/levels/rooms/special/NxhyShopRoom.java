//
// Decompiled by Jadx - 756ms
//
package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.DHXD;
import static com.shatteredpixel.shatteredpixeldungeon.Statistics.lanterfireactive;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Conducts;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Nxhy;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LamellarArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.LeatherArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ScaleArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.ElementYearBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.GrassKingBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.books.bookslist.YellowSunBooks;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.lightblack.OilPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.CausticBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.InfernalBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.ShockingBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.WaterSoul;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfMastery;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfChallenge;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.CurseInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.MagicalInfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.PhaseShift;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.WildEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAugmentation;
import com.shatteredpixel.shatteredpixeldungeon.items.thanks.DistressSignalNesting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class NxhyShopRoom extends SpecialRoom {
    private ArrayList<Item> itemsToSpawn;

    public int minWidth() {
        return Math.max(8, (int) (Math.sqrt((double) itemCount()) + 3.0d));
    }

    public int minHeight() {
        return Math.max(8, (int) (Math.sqrt((double) itemCount()) + 3.0d));
    }

    public int itemCount() {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        return this.itemsToSpawn.size();
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        placeShopkeeper(level);
        placeItems(level);
        for (Room.Door door : this.connected.values()) {
            door.set(Room.Door.Type.REGULAR);
        }
    }

    protected void placeShopkeeper(Level level) {
        int pos = level.pointToCell(center());

        Mob nxhy = new Nxhy();
        nxhy.pos = pos;
        level.mobs.add(nxhy);
    }

    protected void placeItems(Level level) {
        if (this.itemsToSpawn == null) {
            this.itemsToSpawn = generateItems();
        }
        Point itemPlacement = new Point(entrance());
        if (itemPlacement.y == this.top) {
            itemPlacement.y++;
        } else if (itemPlacement.y == this.bottom) {
            itemPlacement.y--;
        } else if (itemPlacement.x == this.left) {
            itemPlacement.x++;
        } else {
            itemPlacement.x--;
        }
        for (Item item : this.itemsToSpawn) {
            if(Dungeon.depth > 26){
                item.upgrade();
                if(Random.Int(10) == 1){
                    item.level += (Random.Int(3));
                }
            }
            if (itemPlacement.x == this.left + 1 && itemPlacement.y != this.top + 1) {
                itemPlacement.y--;
            } else if (itemPlacement.y == this.top + 1 && itemPlacement.x != this.right - 1) {
                itemPlacement.x++;
            } else if (itemPlacement.x != this.right - 1 || itemPlacement.y == this.bottom - 1) {
                itemPlacement.x--;
            } else {
                itemPlacement.y++;
            }
            int cell = level.pointToCell(itemPlacement);
            if (level.heaps.get(cell) != null) {
                do {
                    cell = level.pointToCell(random());
                } while (level.heaps.get(cell) != null || level.findMob(cell) != null);
            }
            level.drop(item, cell).type = Heap.Type.FOR_SALE;
        }
    }

    public static Item HighPotion() {
        Item w;
        switch (Random.Int(6)){
            default:
            case 1: w = new WaterSoul();   break;
            case 2: w = new BlizzardBrew(); break;
            case 3: w = new CausticBrew();    break;
            case 4: w = new InfernalBrew();   break;
            case 5: w = new ShockingBrew();   break;
        }
        return w;
    }

    public static Item HighScroll() {
        Item w;
        switch (Random.Int(5)){
            default:
            case 0: w = new ScrollOfSirensSong(); break;
            case 1: w = new ScrollOfChallenge(); break;
            case 2: w = new ScrollOfMetamorphosis(); break;
            case 3: w = new ScrollOfAntiMagic();    break;
            case 4: w = new ScrollOfPsionicBlast();   break;
        }
        return w;
    }

    protected static ArrayList<Item> generateItems() {
        ArrayList<Item> itemsToSpawn = new ArrayList<>();

        try {
            // 根据深度生成特定装备
            Item w = Generator.random(Generator.wepTiers[4]);
            w.cursed = true;
            w.level(0);
            w.identify();
            itemsToSpawn.add(w);

            // 添加其他物品

            if(Dungeon.depth > 26){
                Armor armor = new LamellarArmor();
                armor.identify();
                armor.level = Random.NormalIntRange(2,4);
                itemsToSpawn.add(armor);
            } else if(Dungeon.depth > 20){
                itemsToSpawn.add(new PlateArmor().identify());
            } else if(Dungeon.depth > 16) {
                itemsToSpawn.add(new ScaleArmor().identify());
            } else if(Dungeon.depth > 10) {
                itemsToSpawn.add(new LeatherArmor().identify());
            } else {
                itemsToSpawn.add(new ClothArmor().identify());
            }

            TippedDart dart = TippedDart.randomTipped(Random.NormalIntRange(2,4));
            dart.enchantment = Weapon.Enchantment.random();
            itemsToSpawn.add(dart);

            itemsToSpawn.add(TippedDart.randomTipped(2));


            itemsToSpawn.add(new ScrollOfTransmutation());
            itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.POTION));
            itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.WAND));
            itemsToSpawn.add(Generator.randomUsingDefaults(Generator.Category.WAND));

            // 随机模式下添加特定物品
            if (Statistics.RandMode) {
                itemsToSpawn.add(new ElixirOfMight());
                itemsToSpawn.add(new PotionOfMastery());
            }

            // 挑战模式下添加特定物品
            if (Dungeon.isChallenged(DHXD) || lanterfireactive) {
                int oilPotionCount = Challenges.activeChallenges() > 6 && lanterfireactive ? 2 : 1;
                for (int i = 0; i < oilPotionCount; i++) {
                    itemsToSpawn.add(new OilPotion());
                }
            }

            // 添加通用物品
            itemsToSpawn.add(new ScrollOfIdentify());
            itemsToSpawn.add(new ScrollOfRemoveCurse());
            itemsToSpawn.add(new ScrollOfMagicMapping());

            // 根据随机值添加特定食物或书籍
            int type = Random.Int(5);
            switch (type) {
                case 0:
                case 1:
                case 2:
                default:
                    itemsToSpawn.add(new Food());
                    break;
                case 3:
                case 4:
                    itemsToSpawn.add(new YellowSunBooks().quantity(1));

                    if(Random.Float()<0.5f){
                        itemsToSpawn.add(new ElementYearBooks().quantity(1));
                    } else {
                        itemsToSpawn.add(new GrassKingBooks().quantity(1));
                    }

                    break;
            }

            // 添加其他通用物品
            itemsToSpawn.add(new PotionOfHealing());
            itemsToSpawn.add(new SmallRation());

            // 根据随机值添加炸弹或蜜罐
            int bombType = Random.Int(4);
            switch (bombType) {
                case 0:
                    itemsToSpawn.add(new Bomb());
                    break;
                case 1:
                case 2:
                    itemsToSpawn.add(new Bomb.DoubleBomb());
                    break;
                case 3:
                    itemsToSpawn.add(new Honeypot());
                    break;
            }

            // 添加强化石
            itemsToSpawn.add(new StoneOfAugmentation());

            // 沙漏模式添加特定物品
            TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
            if (hourglass != null) {
                int bagsToAdd = 0;
                switch (Dungeon.depth) {
                    case 6:
                        bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.2f);
                        break;
                    case 11:
                        bagsToAdd = (int) Math.ceil((3 - hourglass.sandBags) * 0.25f);
                        break;
                    case 16:
                        bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.5f);
                        break;
                    case 20:
                    case 21:
                        bagsToAdd = (int) Math.ceil((5 - hourglass.sandBags) * 0.8f);
                        break;
                }
                for (int i = 0; i < bagsToAdd; i++) {
                    itemsToSpawn.add(new TimekeepersHourglass.sandBag());
                    hourglass.sandBags++;
                }
            }

            // 根据随机值添加罕见物品
            Item rare;
            int rareType = Random.Int(10);
            switch (rareType) {
                case 0:
                    rare = Generator.random(Generator.Category.WAND);
                    break;
                case 1:
                    rare = Generator.random(Generator.Category.RING);
                    break;
                default:
                    rare = Random.Int(3) == 0 ? new Stylus() : Generator.random(Generator.Category.ARTIFACT);
                    break;
            }
            rare.level(0);
            rare.cursed = false;
            rare.cursedKnown = true;
            itemsToSpawn.add(rare);
            // 求救信号套组
            if (!Statistics.SOSisAlreadySpawn && Random.Float() < 0.3f){
                itemsToSpawn.add(new DistressSignalNesting());
                Statistics.SOSisAlreadySpawn = true;
            }

            // 选择一个合适的包
            Bag bag = ChooseBag(Dungeon.hero.belongings);
            if (bag != null) {
                itemsToSpawn.add(bag);
            }

            // 随机添加两个药剂或卷轴
            for (int i = 0; i < 2; i++) {
                itemsToSpawn.add(Random.Int(2) == 0 ?
                        Generator.randomUsingDefaults(Generator.Category.POTION) :
                        Generator.randomUsingDefaults(Generator.Category.SCROLL));
            }

            // 升级物品和随机增加等级
            if (Dungeon.depth > 26) {
                for (Item item : itemsToSpawn) {
                    item.upgrade();
                    if (Random.Int(100) <= 21) {
                        item.level += Random.Int(2,4);
                    }
                }
                itemsToSpawn.add(HighScroll());
                itemsToSpawn.add(HighPotion());
                itemsToSpawn.add(new UnstableBrew());
                itemsToSpawn.add(new CurseInfusion());
                itemsToSpawn.add(new MagicalInfusion());
                itemsToSpawn.add(new WildEnergy());
                itemsToSpawn.add(new PhaseShift());
            }

            // 打乱物品顺序
            Random.shuffle(itemsToSpawn);

        } catch (Exception e) {
            // 添加错误处理机制
            throw new RuntimeException("Failed to generate shop items: " + e.getMessage(), e);
        }

        // 检查物品数量是否超出限制
        if (itemsToSpawn.size() > 63) {
            throw new RuntimeException("Shop attempted to carry more than 63 items!");
        }

        return itemsToSpawn;
    }



    protected static Bag ChooseBag(Belongings pack){
        if(Dungeon.isDLC(Conducts.Conduct.DEV)) return null;
        //generate a hashmap of all valid bags.
        HashMap<Bag, Integer> bags = new HashMap<>();
        if (!Dungeon.LimitedDrops.VELVET_POUCH.dropped()) bags.put(new VelvetPouch(), 1);
        if (!Dungeon.LimitedDrops.SCROLL_HOLDER.dropped()) bags.put(new ScrollHolder(), 0);
        if (!Dungeon.LimitedDrops.POTION_BANDOLIER.dropped()) bags.put(new PotionBandolier(), 0);
        if (!Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped()) bags.put(new MagicalHolster(), 0);

        if (bags.isEmpty()) return null;

        //count up items in the main bag
        for (Item item : pack.backpack.items) {
            for (Bag bag : bags.keySet()){
                if (bag.canHold(item)){
                    bags.put(bag, bags.get(bag)+1);
                }
            }
        }

        //find which bag will result in most inventory savings, drop that.
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
}
